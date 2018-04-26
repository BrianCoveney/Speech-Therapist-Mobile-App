package com.brian.speechtherapistapp.repository;


import android.util.Log;

import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.models.ChildList;
import com.brian.speechtherapistapp.repository.persistors.MongoRemoteConnector;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ChildRepositoryImpl implements IChildRepository {
    private Child child;
    private MongoCollection childCollection;
    private MongoDatabase database;
    private static final String DB_NAME = "speech";
    private static final String DB_COLLECTION = "children";
    private static final String LOG_TAG = ChildRepositoryImpl.class.getSimpleName();

    @Inject
    public ChildRepositoryImpl() {
        try {
            // MongoClient mongoClientConnection = MongoLocalConnection.databaseConnectionLocal();

            MongoClient mongoClientConnection = MongoRemoteConnector.databaseConnectionRemote();

            database = mongoClientConnection.getDatabase(DB_NAME);
            childCollection = database.getCollection(DB_COLLECTION);
        } catch (MongoException e) {
            Log.d(LOG_TAG, "Exception" + e);
        }
    }

    @Override
    public Child getChild(int id) {
        Child child = null;
        MongoCursor<Document> cursor = childCollection.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                int childId = document.getInteger("child_id");
                String firstName = document.getString("first_name");
                String secondName = document.getString("second_name");
                String email = document.getString("email");

                child = Child.builder(childId, firstName, secondName, email).build();
            }
        } finally {
            cursor.close();
        }
        return child;
    }

    @Override
    public void saveChild(ChildList childList) {
        final Document document = new Document();
        for (Child c : childList.getChildList()) {
            Child child = Child.builder(c.getId(), c.getFirstName(), c.getSecondName(), c.getEmail())
                    .withBirthday(c.getBirthday())
                    .withPassword(c.getPassword())
                    .build();
            document.put("child_id", child.getId());
            document.put("first_name", child.getFirstName());
            document.put("second_name", child.getSecondName());
            document.put("email", child.getEmail());
            document.put("birthday", child.getBirthday());
            document.put("password", child.getPassword());
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                childCollection.insertOne(document);
            }
        });
        thread.start();
    }

    @Override
    public List<Child> getChildListFromDB() {
        List<Child> childList = new ArrayList<>();

        FindIterable<Document> databaseRecords = database.getCollection("children").find();
        MongoCursor<Document> cursor = databaseRecords.iterator();

        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                String firstName = document.getString("first_name");
                String secondName = document.getString("second_name");
                String email = document.getString("email");

                // create child with values set from DB
                Child child = Child.builder(0, firstName, secondName, email).build();

                childList.add(child);
            }
        } finally {
            cursor.close();
        }
        return childList;
    }

}
