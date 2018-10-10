package com.brian.speechtherapistapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit;

    // BASE_URL for Emulator
    // private static final String BASE_URL = "http://10.0.2.2:80";

    // To get the BASE_URL for Device first find the  primary IP address of the local machine with:
    // ip route get 1 | awk '{print $NF;exit}'
    private static final String BASE_URL = "http://192.168.1.11:80";


    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
