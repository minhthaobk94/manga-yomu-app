package com.thaontm.mangayomu.model.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class MangaOverview implements Serializable {
    private int id;
    private String previewImageUrl;
    private String name;
    private String genres;
    private String mDescription;
    private String mState;
}
