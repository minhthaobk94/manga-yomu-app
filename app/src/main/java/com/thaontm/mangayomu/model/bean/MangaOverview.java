package com.thaontm.mangayomu.model.bean;

import lombok.Data;

@Data
public class MangaOverview extends MangaInfo {
    private String title;
    private String imageUrl;

    public MangaOverview() {
    }

    public MangaOverview(String baseUrl, String title, String imageUrl) {
        this.setBaseUrl(baseUrl);
        this.title = title;
        this.imageUrl = imageUrl;
    }
}
