package com.brian.speechtherapistapp.repository;


import android.util.Log;

import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.models.ChildList;
import com.brian.speechtherapistapp.presentation.IChildPresenter;
import com.brian.speechtherapistapp.repository.persistors.MongoDockerConnection;
import com.brian.speechtherapistapp.view.activities.GameActivity;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            MongoClient mongoClientConnection = MongoDockerConnection.databaseDockerConnection();
            database = mongoClientConnection.getDatabase(DB_NAME);
            childCollection = database.getCollection(DB_COLLECTION);
        } catch (MongoException e) {
            Log.d(LOG_TAG, "Exception" + e);
        }
    }

    @Override
    public void saveChild(ChildList childList) {
        Map<String, Integer> wordGlidingMap = new HashMap<>();
        wordGlidingMap.put("default_word", 0);
        final Document document = new Document();
        for (Child c : childList.getChildList()) {
            Child child = Child.builder(c.getFirstName(), c.getSecondName(), c.getEmail())
                    .withBirthday(c.getBirthday())
                    .withPassword(c.getPassword())
                    .build();
            document.put("first_name", child.getFirstName());
            document.put("second_name", child.getSecondName());
            document.put("email", child.getEmail());
            document.put("birthday", child.getBirthday());
            document.put("password", child.getPassword());
            document.put("word", "default_word");
            document.put("map_of_gliding_words", wordGlidingMap);
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
    public Child updateWordSpoken(String currWord, String newWord, String email) {
        Bson filter = new Document("email", email);
        Bson newValue = new Document("word", newWord);
        Bson updateOperationDocument = new Document("$set", newValue);
        childCollection.updateOne(filter, updateOperationDocument);

        Child child = getChildWithEmailIdentifier(email);
        return child;
    }

    @Override
    public void updateGlidingOfLiquidsMap(Map<String, Integer> glidingLiquidsMap, String email) {
        Bson filter = new Document("email", email);
        Bson newValue = new Document("map_of_gliding_words", glidingLiquidsMap);
        Bson updateResult = new Document("$set", newValue);
        childCollection.updateOne(filter, updateResult);
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
                Child child = Child.builder(firstName, secondName, email)
                        .withWord(word)
                        .build();

                childList.add(child);
            }
        } finally {
            cursor.close();
        }
        return childList;
    }


    /**
     * Returns a child object, fetched from the database, based on the email address passed in
     *
     * @see IChildRepository#getChildWithEmailIdentifier(String)
     * @see IChildPresenter#getChildWithEmail(String)
     * @see GameActivity.FetchFromDatabaseTask
     * @return Child
     */
    @Override
    public Child getChildWithEmailIdentifier(String email) {
        Child child = null;

        // We use mongo's iteration document to search our collection for the key values we are
        // interested in. Here the key is the mongo 'email' field and the value is the 'email'
        // String that has been passed into the method.
        FindIterable<Document> databaseRecords = childCollection.find(eq("email", email));

        // We create a mongo cursor object to find the next item in the database
        MongoCursor<Document> cursor = databaseRecords.iterator();

        // We create a bson document which we set equal to the cursor in the while loop.
        // This is a key value document, again with mongo fields as the key, and the
        // returned results as the values.
        Document document;
        try {
            while (cursor.hasNext()) {
                document = cursor.next();
                String firstName = document.getString("first_name");
                String secondName = document.getString("second_name");
                String childEmail = document.getString("email");
                String word = document.getString("word");

                // We create a map for the gliding words, which will be inserted as a nested map
                Map<String, Integer> wordGlidingMap = document.get("map_of_gliding_words", Map.class);

                // We create a child object containing the results of the above values
                child = Child.builder(firstName, secondName, childEmail)
                        .withWord(word)
                        .withGlidingWordMap(wordGlidingMap)
                        .build();
            }
        } finally {
            cursor.close();
        }
        return child;
    }


    @Override
    public Child getChildFromDB() {

        if (child == null) {
            child = Child.builder(CHILD_FIRST_NAME, CHILD_SECOND_NAME, CHILD_EMAIL).build();
        } else {

            FindIterable<Document> databaseRecords = database.getCollection("children").find();
            MongoCursor<Document> cursor = databaseRecords.iterator();
            Document document;
            try {
                while (cursor.hasNext()) {
                    document = cursor.next();
                    String firstName = document.getString("first_name");
                    String secondName = document.getString("second_name");
                    String childEmail = document.getString("email");
                    String word = document.getString("word");
                    child = Child.builder(firstName, secondName, childEmail)
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
    public void deleteChild(String email) {
        childCollection.deleteOne(eq("email", email));
    }

}
