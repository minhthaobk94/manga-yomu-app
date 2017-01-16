package com.thaontm.mangayomu.model.engine;

import com.thaontm.mangayomu.model.bean.MangaOverview;
import com.thaontm.mangayomu.utils.MangaOverviewParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thao on 1/16/2017.
 * Copyright thao 2017.
 */

public class MangaOverviewService implements IMangaOverview {

    @Override
    public List<MangaOverview> fetchMangaOverviews(String httpLink) {
        List<MangaOverview> results = new ArrayList<>();
        Document doc;
        try {
            doc = Jsoup.parse(httpLink);
            Element content = doc.getElementById("tab-mostview");
            // Elements divs = content.getElementsByTag("img");

            MangaOverviewParser parser = new MangaOverviewParser();
            MangaOverview mangaOverview;

            Elements elements = content.children();
            for (Element div : elements) {
                mangaOverview = new MangaOverview();
                String imageSrc = parser.getImageSrc(div);
                String name = parser.getTitle(div);
                List<String> genres = parser.getGenres(div);

                mangaOverview.setPreviewImageUrl(imageSrc);
                mangaOverview.setName(name);
                mangaOverview.setGenres(genres);

                results.add(mangaOverview);
            }

        } catch (Exception e) {

        }

        return results;
    }
}
