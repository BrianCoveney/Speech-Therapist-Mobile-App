package com.brian.speechtherapistapp.network;

import com.brian.speechtherapistapp.models.RetroChild;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IWebAPIService {
    @GET("/children")
    Call<List<RetroChild>> getAllChildren();
}
