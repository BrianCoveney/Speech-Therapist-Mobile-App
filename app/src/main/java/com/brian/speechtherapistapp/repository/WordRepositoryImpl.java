package com.brian.speechtherapistapp.repository;

import android.util.Log;

import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.models.Word;
import com.brian.speechtherapistapp.repository.persistors.MongoRemoteConnector;
import com.brian.speechtherapistapp.view.IGameView;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import javax.inject.Inject;


public class WordRepositoryImpl implements IWordRepository {
    private Word word;
    private IGameView gameOneView;
    private MongoCollection childCollection;
    private MongoDatabase database;
    private static final String DB_NAME = "speech";
    private static final String DB_COLLECTION = "children";
    private static final String LOG_TAG = WordRepositoryImpl.class.getSimpleName();

    @Inject
    public WordRepositoryImpl() {
        try {

//            MongoClient mongoClientConnection = MongoLocalConnection.databaseConnectionLocal();

            MongoClient mongoClientConnection = MongoRemoteConnector.databaseConnectionRemote();

            database = mongoClientConnection.getDatabase(DB_NAME);
            childCollection = database.getCollection(DB_COLLECTION);
        } catch (MongoException e) {
            Log.d(LOG_TAG, "Exception" + e);
        }
    }

    @Override
    public Word getWord(String id) {
        if (word == null) {
            word = new Word();
            word.setId(id);
        }
        return word;
    }

    @Override
    public void saveWord(Word w) {

        this.word = new Word();
        this.word.setId(w.getId());
        this.word.setWord(w.getWord());

        Child child = Child.builder(2, "fname", "lname", "email")
                .build();

        final Document document = new Document();
        try {
            document.put("child_id", child.getId());
            document.put("first_name", child.getFirstName());
            document.put("second_name", child.getSecondName());
            document.put("email", child.getEmail());
        }catch (MongoException e){
            Log.d(LOG_TAG, "exception: " + e);
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                childCollection.insertOne(document);
            }
        });
    }
}
