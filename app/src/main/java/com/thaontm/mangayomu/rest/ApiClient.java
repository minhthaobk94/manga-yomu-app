package com.thaontm.mangayomu.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by thao on 5/18/2017.
 * Copyright thao 2017.
 */

public class ApiClient {
    public static final String BASE_URL = "https://www.googleapis.com/language/translate/v2?source=en&target=vi&key=AIzaSyAJGBILuWZOKQ5j8HGf9AxDUnfPufwqn6E";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
