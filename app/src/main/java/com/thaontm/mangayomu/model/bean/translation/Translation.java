package com.thaontm.mangayomu.model.bean.translation;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by thao on 5/18/2017.
 * Copyright thao 2017.
 */

public class Translation {
    @SerializedName("translatedText")
    @Getter
    @Setter
    String translatedText;
}
