package com.brian.speechtherapistapp.repository.persistors;

import android.util.Log;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;

public class MongoRemoteConnector {

    public static MongoClient databaseConnectionRemote() {
        final String LOG_TAG = MongoRemoteConnector.class.getSimpleName();
        final String DB_MONGO_USER = "speechUser";
        final String DB_MONGO_PASS = "bossdog12";
        final String DB_NAME = "speech";
        final String DB_MONGO_IP = "ec2-54-202-69-181.us-west-2.compute.amazonaws.com";
        final String DB_PORT = "8080";

        MongoClient dbConnection = null;

        try {
            String mongoURI = "mongodb://" + DB_MONGO_USER + ":" + DB_MONGO_PASS + "@" +
                    DB_MONGO_IP +":"+ DB_PORT +"/" + DB_NAME;
            dbConnection = new MongoClient( new MongoClientURI(mongoURI));
            if (dbConnection != null)
                Log.d(LOG_TAG,"Connected to MongoDB!");
        } catch (MongoException e) {
            Log.d(LOG_TAG,"Connection to MongoDB failed " + e);
        }

        return dbConnection;
    }

}
