package com.thaontm.mangayomu.model.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by thao on 1/13/2017.
 * Copyright thao 2017.
 */

@Data
public class MangaDetail extends MangaInfo implements Serializable{
    private String title;
    private String imageUrl;
    private String status;
    private String genres;
    private String author;
    private String description;
}
