package com.brian.speechtherapistapp.repository;


import android.util.Log;

import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.models.ChildList;
import com.brian.speechtherapistapp.repository.persistors.MongoRemoteConnector;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

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

//             MongoClient mongoClientConnection = MongoLocalConnection.databaseConnectionLocal();

              MongoClient mongoClientConnection = MongoRemoteConnector.databaseConnectionRemote();

            database = mongoClientConnection.getDatabase(DB_NAME);
            childCollection = database.getCollection(DB_COLLECTION);
        } catch (MongoException e) {
            Log.d(LOG_TAG, "Exception" + e);
        }
    }

    @Override
    public Child getChild(String id) {
        if (child == null) {
            child = Child.builder(id, "Brian", "Coveney", "bri@gmail.com")
                    .build();
        }
        return child;
    }

    @Override
    public void saveChild(ChildList childList) {
        final Document document = new Document();
        for (Child c : childList.getChildList()) {
            this.child = Child.builder(c.getId(), c.getFirstName(), c.getSecondName(), c.getEmail())
                              .build();
            document.put("first_name", this.child.getFirstName());
            document.put("second_name", this.child.getSecondName());
            document.put("test_name", "test name");

            Log.i(LOG_TAG, "Word: " + this.child.getFirstName());
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                childCollection.insertOne(document);
            }
        });
        thread.start();
    }
}
