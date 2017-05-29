package com.thaontm.mangayomu.model.provider;

import com.thaontm.mangayomu.model.bean.ChapterImage;
import com.thaontm.mangayomu.model.bean.MangaChapter;
import com.thaontm.mangayomu.model.bean.MangaDetail;
import com.thaontm.mangayomu.model.bean.MangaHome;
import com.thaontm.mangayomu.model.bean.MangaOverview;
import com.thaontm.mangayomu.model.bean.SearchedManga;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MangaParkProvider implements MangaProvider {
    @Override
    public void getHome(final Callback<MangaHome> callback) {
        final String url = "http://mangapark.me/";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<MangaOverview> newMangas = getLastestMangas(url);
                    List<MangaOverview> popularMangas = getPopularMangas(url);

                    MangaHome mangaHome = new MangaHome();
                    mangaHome.setNewMangas(newMangas);
                    mangaHome.setPopularMangas(popularMangas);
                    callback.onSuccess(mangaHome);
                } catch (IOException e) {
                    callback.onError(new Exception("Please check your internet connection !"));
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void getMangaDetail(final String url, final Callback<MangaDetail> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url).get();
                    Element coverImageElement = document.select("div.cover > img").first();
                    Element mangaTitleElement = document.select("div.hd > h1 > a").first();
                    Element authorElement = document.select("table.attr > tbody > tr").get(4).select("td").first();
                    Element allOfGenresElement = document.select("table.attr > tbody > tr").get(6).select("td").first();
                    Element statusElement = document.select("table.attr > tbody > tr").get(9).select("td").first();
                    Element descriptionElement = document.select("p.summary").first();

                    // output data
                    MangaDetail mangaDetail = new MangaDetail();
                    mangaDetail.setTitle(mangaTitleElement.text());
                    mangaDetail.setAuthor(authorElement.text());
                    mangaDetail.setGenres(allOfGenresElement.text());
                    mangaDetail.setDescription(descriptionElement == null ? "" : descriptionElement.text());
                    mangaDetail.setImageUrl((coverImageElement.attr("abs:src")));
                    mangaDetail.setStatus(statusElement.text());
                    mangaDetail.setBaseUrl(url);
                    callback.onSuccess(mangaDetail);
                } catch (IOException e) {
                    callback.onError(new Exception("Check your internet connection !"));
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void getMangaChapters(final String url, final Callback<List<MangaChapter>> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<MangaChapter> mangaChapters = new ArrayList<>();
                try {
                    Document document = Jsoup.connect(url).get();
                    Elements chapterElements = document.select("div#list > div.stream > div.volume");

                    for (int i = 0; i < chapterElements.size(); i++) {
                        Elements volumeElements = chapterElements.get(i).select("li");

                        for (int j = 0; j < volumeElements.size(); j++) {
                            Element volumeTitleElement = volumeElements.get(j).select("span").first();
                            Element uploadTimeElement = volumeElements.get(j).select("i").first();
                            Element chapterUrlElement = volumeElements.get(j).select("em > a").get(4);

                            // output data
                            String chapterTitle = volumeTitleElement.text();
                            String uploadTime = uploadTimeElement.text();
                            String chapterUrl = chapterUrlElement.attr("abs:href");

                            MangaChapter mangaChapter = new MangaChapter();
                            mangaChapter.setChapterName(chapterTitle);
                            mangaChapter.setUploadedTime(uploadTime);
                            mangaChapter.setBaseUrl(chapterUrl);
                            mangaChapters.add(mangaChapter);
                            callback.onSuccess(mangaChapters);
                        }
                    }
                } catch (IOException e) {
                    callback.onError(new Exception("Please check your internet connection !"));
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void getMangaChapterImages(final String url, final Callback<List<ChapterImage>> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url).get();
                    Elements imageElements = document.select("section#viewer > div.canvas");

                    List<ChapterImage> chapterImages = new ArrayList<>();
                    for (int i = 0; i < imageElements.size(); i++) {
                        Element imageElement = imageElements.get(i).select("div.tool > a.img-num").first();

                        // output data
                        String imageUrl = imageElement.attr("abs:href");

                        ChapterImage chapterImage = new ChapterImage();
                        chapterImage.setBaseUrl(imageUrl);
                        chapterImages.add(chapterImage);
                    }
                    callback.onSuccess(chapterImages);
                } catch (IOException ex) {
                    callback.onError(new Exception("Check your internet connection !"));
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void search(final String keyword, final Callback<List<SearchedManga>> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://mangapark.me/search?q=" + keyword).get();
                    Elements searchedMangaElements = document.select("div.manga-list > div.item");

                    List<SearchedManga> searchedMangas = new ArrayList<>();
                    for (int i = 0; i < searchedMangaElements.size(); i++) {
                        Element titleElement = searchedMangaElements.get(i).select("table > tbody > tr > td > a.cover").first();
                        Element newestChapterElement = searchedMangaElements.get(i).select("table > tbody > tr > td > ul > li").first();

                        // output data
                        String baseUrl = titleElement.attr("abs:href"); // base url
                        String title = titleElement.attr("title"); // title
                        String newestChapter = newestChapterElement.text(); // newest chapter

                        SearchedManga searchedManga = new SearchedManga();
                        searchedManga.setBaseUrl(baseUrl);
                        searchedManga.setTitle(title);
                        searchedManga.setNewestChapter(newestChapter);
                        searchedMangas.add(searchedManga);
                    }
                    callback.onSuccess(searchedMangas);
                } catch (IOException e) {
                    callback.onError(new Exception("Please check your internet connection !"));
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private List<MangaOverview> getLastestMangas(final String url) throws IOException {
        List<MangaOverview> lastestMangas = new ArrayList<>();
        Document document = Jsoup.connect(url).get();
        Elements newestMangaElements = document.select("div.content > article > div.bd > div.item");

        MangaOverview mangaOverview = null;
        for (int i = 0; i < newestMangaElements.size(); i++) {
            Element baseUrlElement = newestMangaElements.get(i).select("a.cover").first();

            String baseUrl = baseUrlElement.attr("abs:href");
            String imgUrl = baseUrlElement.select("img").first().attr("abs:src");
            String title = baseUrlElement.attr("title");

            mangaOverview = new MangaOverview();
            mangaOverview.setBaseUrl(baseUrl);
            mangaOverview.setTitle(title);
            mangaOverview.setImageUrl(imgUrl);
            lastestMangas.add(mangaOverview);
        }
        return lastestMangas;
    }

    private List<MangaOverview> getPopularMangas(final String url) throws IOException {
        List<MangaOverview> popularMangas = new ArrayList<>();

        Document document = Jsoup.connect(url).get();
        Element popularContainer = document.getElementById("switch-cnt");
        Elements popularElements = popularContainer.getElementsByTag("li");

        MangaOverview mangaOverview = null;
        for (int i = 0; i < popularElements.size(); i++) {
            Element titleElement = popularElements.get(i).getElementsByClass("thm-effect radius").first();

            // output data
            String baseUrl = titleElement.attr("abs:href");
            String title = titleElement.attr("title");
            String imageUrl = titleElement.select("img").first().attr("abs:src");

            mangaOverview = new MangaOverview();
            mangaOverview.setBaseUrl(baseUrl);
            mangaOverview.setTitle(title);
            mangaOverview.setImageUrl(imageUrl);
            popularMangas.add(mangaOverview);
        }
        return popularMangas;
    }
}