package com.brian.speechtherapistapp.repository.persistors;

import android.util.Log;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class MongoDockerConnection {

    private static final String LOG_TAG = MongoDockerConnection.class.getSimpleName();
    private static final String HOST = "94.156.189.70";
    private static final int PORT = 27017;

    public MongoDockerConnection() { }

    public static MongoClient databaseDockerConnection() {

        MongoClient dbConnection = null;
        try {

            dbConnection = new MongoClient(HOST, PORT);

            if (dbConnection != null)
                Log.d(LOG_TAG,"Connected to MongoDB!");
        } catch (MongoException e) {
            Log.d(LOG_TAG,"Connection to MongoDB failed " + e);
        }
        return dbConnection;
    }
}
