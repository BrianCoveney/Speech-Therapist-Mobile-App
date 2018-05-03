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

import static com.brian.speechtherapistapp.util.Const.ParamsNames.CHILD_EMAIL;
import static com.brian.speechtherapistapp.util.Const.ParamsNames.CHILD_FIRST_NAME;
import static com.brian.speechtherapistapp.util.Const.ParamsNames.CHILD_SECOND_NAME;
import static com.mongodb.client.model.Filters.eq;

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
        Document document;
        if (child == null) {
            child = Child.builder(id, CHILD_FIRST_NAME, CHILD_SECOND_NAME, CHILD_EMAIL).build();
        } else {
            MongoCursor<Document> cursor = childCollection.find().iterator();
            try {
                while (cursor.hasNext()) {
                    document = cursor.next();
                    int childId = document.getInteger("child_id");
                    String firstName = document.getString("first_name");
                    String secondName = document.getString("second_name");
                    String email = document.getString("email");
                    String word = document.getString("word");

                    child = Child.builder(childId, firstName, secondName, email)
                            .withWord(word)
                            .build();
                }
            } finally {
                cursor.close();
            }
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
            document.put("child_id", child.getId()+1);
            document.put("first_name", child.getFirstName());
            document.put("second_name", child.getSecondName());
            document.put("email", child.getEmail());
            document.put("birthday", child.getBirthday());
            document.put("password", child.getPassword());
            document.put("word", "");
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
    public void updateWordSpoken(Child child, String currWord, String newWord) {
        childCollection.updateOne(eq("word", currWord),
                new Document("$set", new Document("word", newWord)));

//        child.setWordName(newWord);
    }

    @Override
    public List<Child> getChildListFromDB() {
        List<Child> childList = new ArrayList<>();

        FindIterable<Document> databaseRecords = database.getCollection("children").find();
        MongoCursor<Document> cursor = databaseRecords.iterator();
        Document document;
        try {
            while (cursor.hasNext()) {
                document = cursor.next();
                String firstName = document.getString("first_name");
                String secondName = document.getString("second_name");
                String email = document.getString("email");
                String word = document.getString("word");

                // create child with values set from DB
                Child child = Child.builder(0, firstName, secondName, email)
                        .withWord(word)
                        .build();

                childList.add(child);
            }
        } finally {
            cursor.close();
        }
        return childList;
    }

    @Override
    public Child getChildWithEmailIdentifier(String email) {
        Child child = null;

        FindIterable<Document> databaseRecords = childCollection.find(eq("email", email));
        MongoCursor<Document> cursor = databaseRecords.iterator();
        Document document;
        try {
            while (cursor.hasNext()) {
                document = cursor.next();
                String firstName = document.getString("first_name");
                String secondName = document.getString("second_name");
                String childsEmail = document.getString("email");
                child = Child.builder(0, firstName, secondName, childsEmail).build();
            }
        } finally {
            cursor.close();
        }
        return child;

    }

}
