package com.thaontm.mangayomu.model.bean;

import java.util.List;

import lombok.Data;

/**
 * Created by thao on 3/19/2017.
 * Copyright thao 2017.
 */

@Data
public class MangaHome extends MangaInfo {
    private List<MangaOverview> popularMangas;
    private List<MangaOverview> newMangas;

}
