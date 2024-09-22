package com.example.iot_lab3_20201497.api;

import com.example.iot_lab3_20201497.models.LoginRequest;
import com.example.iot_lab3_20201497.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    //Código con ayuda de chatgpt
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

}
