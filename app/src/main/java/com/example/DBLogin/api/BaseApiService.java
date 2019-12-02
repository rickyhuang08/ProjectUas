package com.example.DBLogin.api;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface BaseApiService {

        // Fungsi ini untuk memanggil API http://10.0.2.2/mahasiswa/login.php
        @Headers("Content-Type: application/json")
        @POST("login-user")
        Call<ResponseBody> loginRequest(@Body JsonObject body);

        // Fungsi ini untuk memanggil API http://10.0.2.2/mahasiswa/register.php
        @Headers("Content-Type: application/json")
        @POST("register-user")
        Call<ResponseBody> registerRequest(@Body JsonObject jsonObject);

        @Headers("Content-Type: application/json")
        @POST("check-forgot-password")
        Call<ResponseBody> checkForgotPassword(@Body JsonObject jsonObject);

        @Headers("Content-Type: application/json")
        @POST("forgot-password")
        Call<ResponseBody> forgotPassword(@Body JsonObject jsonObject);
    }


