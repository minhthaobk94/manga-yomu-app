package com.thaontm.mangayomu.rest;

import com.thaontm.mangayomu.model.bean.translation.TranslationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by thao on 5/18/2017.
 * Copyright thao 2017.
 */

public interface ApiInterface {
    @GET("")
    Call<TranslationResponse> getTranslationResponse(@Query("q") String text);
}
