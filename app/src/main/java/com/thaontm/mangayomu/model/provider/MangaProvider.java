package com.thaontm.mangayomu.model.provider;

import com.thaontm.mangayomu.model.bean.ChapterImage;
import com.thaontm.mangayomu.model.bean.MangaChapter;
import com.thaontm.mangayomu.model.bean.MangaDetail;
import com.thaontm.mangayomu.model.bean.MangaHome;
import com.thaontm.mangayomu.model.bean.SearchedManga;

import java.util.List;

/**
 * Created by thao on 3/19/2017.
 * Copyright thao 2017.
 */

public interface MangaProvider {
    /**
     * Contains newest, popular... mangas overview.
     */
    void getHome(Callback<MangaHome> callback);

    /**
     * Detail of a manga such as title, description, author...
     */
    void getMangaDetail(String url, Callback<MangaDetail> callback);

    /**
     * Chapters of a manga.
     */
    void getMangaChapters(String url, Callback<List<MangaChapter>> callback);

    /**
     * Chapter images of a chapter. Each chapters have a several images that can show in read manga screen.
     */
    void getMangaChapterImages(String url, Callback<List<ChapterImage>> callback);

    /**
     * Search all of mangas with keyword
     */
    void search(String keyword, Callback<List<SearchedManga>> callback);
}
