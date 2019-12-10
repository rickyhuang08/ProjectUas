package com.example.DBLogin.api;

public class UtilsApi {

//    public static final String BASE_URL_API = "http://172.20.10.3:8080/api/user/";
//    public static final String BASE_URL_API = "http://192.168.1.41:8080/api/user/";
//    public static final String BASE_URL_API = "http://192.168.30.43:8080/api/user/";
    public static final String BASE_URL_API = "http://192.168.10.11:8080/api/user/";

    // Mendeklarasikan Interface BaseApiService
    public static com.example.DBLogin.api.BaseApiService getAPIService(){
        return com.example.DBLogin.api.RetrofitClient.getClient(BASE_URL_API).create(com.example.DBLogin.api.BaseApiService.class);
    }
}
