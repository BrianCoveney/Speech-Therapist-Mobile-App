package com.brian.speechtherapistapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit;

    /**
     * Notes for testing in development:
     *
     * Android Studio Emulator URL: "http://10.0.2.2:80";
     *
     * _or_
     *
     * Get the URL of a Mobile Device - find the primary IP address of the local machine with:
     * ip route get 1 | awk '{print $NF;exit}'
     * e.g: 192.168.1.11:80
     *
     */
    private static String DEVELOPMENT_URL = "http://192.168.1.11:80";

    private static String PRODUCTION_URL = "https://speech.briancoveney.com:443";


    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(PRODUCTION_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
