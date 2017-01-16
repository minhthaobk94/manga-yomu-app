package com.thaontm.mangayomu.model.engine;

import com.thaontm.mangayomu.model.bean.MangaOverview;

import java.util.List;

/**
 * Created by thao on 1/12/2017.
 * Copyright thao 2017.
 */

public interface IMangaOverview {
    List<MangaOverview> fetchMangaOverviews (String httpLink);
}
