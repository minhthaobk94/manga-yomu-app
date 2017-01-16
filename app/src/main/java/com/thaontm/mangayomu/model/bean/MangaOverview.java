package com.thaontm.mangayomu.model.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class MangaOverview implements Serializable {
    private int id;
    private String previewImageUrl;
    private String name;
    private List<String> genres;
    private String mDescription;
    private String mState;

    public String getGenresAsString() {
        return TextUtils.join(",", genres);
    }
}
