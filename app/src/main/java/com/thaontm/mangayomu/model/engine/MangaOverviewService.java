package com.thaontm.mangayomu.model.engine;

import android.content.Context;
import android.util.Log;

import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.model.bean.MangaOverview;
import com.thaontm.mangayomu.utils.MangaOverviewParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thao on 1/16/2017.
 * Copyright thao 2017.
 */

public class MangaOverviewService implements IMangaOverview {

    private Context context;

    public MangaOverviewService(Context context) {
        this.context = context;
    }

    @Override
    public List<MangaOverview> fetchMangaOverviews(String httpLink) {
        List<MangaOverview> results = new ArrayList<>();
        Document doc;
        try {
            InputStream in = context.getResources().openRawResource(R.raw.kissmanga);
            byte[] b = new byte[in.available()];
            in.read(b);
            doc = Jsoup.parse(new String(b));
            Element content = doc.getElementById("tab-mostview");

            MangaOverviewParser parser = new MangaOverviewParser();
            MangaOverview mangaOverview;

            if (content != null) {
                Elements elements = content.children();
                for (Element div : elements) {
                    mangaOverview = new MangaOverview();
                    String imageSrc = parser.getImageSrc(div);
                    String name = parser.getTitle(div);
                    List<String> genres = parser.getGenres(div);
                    Log.d(MangaOverviewService.class.getName(), "imageURL: " + imageSrc);

                    if (imageSrc != null && imageSrc.length() > 0) {
                        imageSrc = imageSrc.replace("./kissmanga_files/", "a").toLowerCase().replace('-', '_');
                        imageSrc = imageSrc.substring(0, imageSrc.lastIndexOf('.'));
                        mangaOverview.setPreviewImageUrl(imageSrc);
                        mangaOverview.setName(name);
                        mangaOverview.setGenres(genres);
                        results.add(mangaOverview);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
}
