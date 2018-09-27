package com.brian.speechtherapistapp.network;

import com.brian.speechtherapistapp.models.Child;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IWebAPIService {
    @GET("/children")
    Call<List<Child>> getAllChildren();
}
