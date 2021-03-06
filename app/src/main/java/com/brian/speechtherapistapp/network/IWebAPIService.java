package com.brian.speechtherapistapp.network;

import com.brian.speechtherapistapp.models.ChildResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IWebAPIService {

    @GET("/children")
    Call<List<ChildResponse>> getAllChildren();

    @POST("/child/{first_name}/{second_name}/{email}")
    Call <ChildResponse> createChildWithField(@Path("first_name") String firstName,
                                              @Path("second_name") String secondName,
                                              @Path("email") String email);

    @DELETE("{email}")
    Call<ChildResponse> deleteChildWithField(@Path("email") String email);
}
