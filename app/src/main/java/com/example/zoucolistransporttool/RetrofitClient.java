package com.example.zoucolistransporttool;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://10.0.2.2/api_ppe4/";


    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

