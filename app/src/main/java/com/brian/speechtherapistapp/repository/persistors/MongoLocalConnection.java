package com.brian.speechtherapistapp.repository.persistors;

import android.util.Log;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;


public class MongoLocalConnection {
    private static final String LOG_TAG = MongoLocalConnection.class.getSimpleName();
    private static final String EMULATOR_IP = "10.0.2.2";
    private static final String LOCAL_IP = "87.42.43.167";

    public MongoLocalConnection() { }

    public static MongoClient databaseConnectionLocal() {

        MongoClient dbConnection = null;
        try {
            dbConnection = new MongoClient(LOCAL_IP, 27017);
            if (dbConnection != null)
                Log.d(LOG_TAG,"Connected to MongoDB!");
        } catch (MongoException e) {
            Log.d(LOG_TAG,"Connection to MongoDB failed " + e);
        }
        return dbConnection;
    }
}
