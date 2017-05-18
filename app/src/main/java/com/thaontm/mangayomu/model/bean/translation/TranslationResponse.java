package com.thaontm.mangayomu.model.bean.translation;

import com.google.gson.annotations.SerializedName;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class TranslationResponse {
    @SerializedName("translations")
    @Getter
    @Setter
    List<Translation> translations;
}
