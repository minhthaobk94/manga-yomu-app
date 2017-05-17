package com.thaontm.mangayomu.model.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by thao on 3/19/2017.
 * Copyright thao 2017.
 */

@Data
public class MangaChapter extends MangaInfo implements Serializable{
    private String chapterName;
    private String uploadedTime;
}
