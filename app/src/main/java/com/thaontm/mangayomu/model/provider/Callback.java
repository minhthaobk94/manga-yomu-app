package com.thaontm.mangayomu.model.provider;

/**
 * Created by thao on 3/19/2017.
 * Copyright thao 2017.
 */

public interface Callback<T> {
    void onSuccess(T result);

    void onError(Throwable what);
}
