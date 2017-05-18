package com.thaontm.mangayomu.rest;

import com.thaontm.mangayomu.model.bean.translation.TranslationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("v2/")
    Call<TranslationResponse> getTranslationResponse(@Query("key") String API_KEY, @Query("source") String source, @Query("target") String target, @Query("q") String text);
}