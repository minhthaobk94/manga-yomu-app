package com.thaontm.mangayomu.model.provider;

import com.thaontm.mangayomu.model.bean.ChapterImage;
import com.thaontm.mangayomu.model.bean.MangaChapter;
import com.thaontm.mangayomu.model.bean.MangaDetail;
import com.thaontm.mangayomu.model.bean.MangaHome;
import com.thaontm.mangayomu.model.bean.MangaInfo;
import com.thaontm.mangayomu.model.bean.MangaOverview;
import com.thaontm.mangayomu.model.bean.SearchedManga;
import com.thaontm.mangayomu.utils.StringUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thao on 3/19/2017.
 * Copyright thao 2017.
 */

public class KakalotMangaProvider implements MangaProvider {

    public KakalotMangaProvider() {
    }

    @Override
    public void getHome(final Callback<MangaHome> callback) {
        final String url = "http://mangakakalot.com/";
        new Thread(new Runnable() {
            @Override
            public void run() {

                List<MangaOverview> newMangas = new ArrayList<>();
                List<MangaOverview> popularMangas = new ArrayList<>();
                Document document = null;
                try {
                    document = Jsoup.connect(url).get();
                    Element element = document.getElementById("contentstory");
                    Elements divElement = element.getElementsByClass("doreamon");
                    Elements classElements = element.getElementsByClass("cover");
                    Element divElement2 = classElements.first();
                    Elements detailElements = divElement2.select("a[href]");
                    String baseUrl = detailElements.attr("href");
                    Element rootElement = divElement.first();

                    for (Element element2 : rootElement.children()) {
                        Elements elements = element2.select("a[href]");
                        MangaOverview newMangaOverview = new MangaOverview();
                        if (elements.size() > 1) {
                            Element cover = elements.first().child(0);
                            Element title = elements.get(1);
                            String image = cover.attr("src");
                            String name = title.text();
                            newMangaOverview.setImageUrl(image);
                            newMangaOverview.setTitle(name);
                            newMangaOverview.setBaseUrl(getBaseUrl(element2));
                            newMangas.add(newMangaOverview);
                        }

                    }

                    Element popularElement = document.getElementById("owl-demo");
                    Elements elements = popularElement.getElementsByClass("item");

                    for (Element element3 : popularElement.children()) {
                        MangaOverview popularMangaOverview = new MangaOverview();

                        Elements elements1 = element3.select("a[href]");
                        if (elements1.size() > 1) {
                            Element cover = element3.child(0);
                            Element name = elements1.get(0);
                            Element chapter = elements1.get(1);

                            String image = cover.attr("src");
                            String title = name.text() + " " + chapter.text();
                            popularMangaOverview.setImageUrl(image);
                            popularMangaOverview.setTitle(title);
                            popularMangaOverview.setBaseUrl(getBaseUrl(element));
                            popularMangas.add(popularMangaOverview);
                        }

                    }
                    MangaHome mangaHome = new MangaHome();
                    mangaHome.setNewMangas(newMangas);
                    mangaHome.setPopularMangas(popularMangas);
                    MangaInfo mangaInfo = new MangaInfo();
                    mangaInfo.setBaseUrl(baseUrl);
                    callback.onSuccess(mangaHome);
                } catch (IOException e) {
                    callback.onError(new Exception("Check your internet connection !"));
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
                Document document = null;
                try {
                    document = Jsoup.connect(url).get();
                    Elements mangaInfoElements = document.getElementsByClass("manga-info-text");
                    Elements mangaImageUrlElements = document.getElementsByClass("manga-info-pic");
                    Element mangaDescriptionElement = document.getElementById("noidungm");

                    String title = getTitle(mangaInfoElements);
                    String imageUrl = getImageUrl(mangaImageUrlElements);
                    String author = getAuthor(mangaInfoElements);
                    String allOfGenres = getGenres(mangaInfoElements);
                    String status = getStatus(mangaInfoElements);
                    String description = getDescription(mangaDescriptionElement);

                    MangaDetail mangaDetail = new MangaDetail();
                    mangaDetail.setTitle(title);
                    mangaDetail.setAuthor(author);
                    mangaDetail.setGenres(allOfGenres);
                    mangaDetail.setDescription(description);
                    mangaDetail.setImageUrl(imageUrl);
                    mangaDetail.setStatus(status);
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
                Document document = null;
                List<MangaChapter> mangaChapters = new ArrayList<MangaChapter>();
                try {
                    document = Jsoup.connect(url).get();
                    Element chapterElement = document.getElementsByClass("chapter-list").first();
                    for (Element element : chapterElement.children()) {
                        MangaChapter mangaChapter = new MangaChapter();
                        Elements chapterElements = element.select("a[href]");
                        Element uploadDateElement = element.getElementsByTag("span").get(2);
                        if (chapterElements.size() > 0) {
                            Element rootElement = chapterElements.first();
                            String chapter = rootElement.text();
                            String uploadTime = uploadDateElement.text();
                            String chapterUrl = rootElement.select("a").attr("href");
                            mangaChapter.setChapterName(chapter);
                            mangaChapter.setUploadedTime(uploadTime);
                            mangaChapter.setBaseUrl(chapterUrl);
                            mangaChapters.add(mangaChapter);
                        }

                    }
                    callback.onSuccess(mangaChapters);
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onError(new Exception("Check your internet connection !"));
                }


            }
        }).start();

    }


    @Override
    public void getMangaChapterImages(final String url, final Callback<List<ChapterImage>> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document document = null;
                try {
                    document = Jsoup.connect(url).get();
                    Element element = document.getElementById("vungdoc");
                    Elements imgs = element.children();
                    List<ChapterImage> chapterImages = new ArrayList<>();
                    for (int i = 0; i < imgs.size(); i++) {
                        ChapterImage chapterImage = new ChapterImage();
                        Element image = imgs.get(i);
                        String imageUrl = image.attr("src");
                        chapterImage.setBaseUrl(imageUrl);
                        chapterImages.add(chapterImage);
                    }
                    callback.onSuccess(chapterImages);
                } catch (IOException e) {
                    callback.onError(new Exception("Check your internet connection !"));
                    e.printStackTrace();
                }
            }
        }).start();

    }


    @Override
    public void search(final String keyword, final Callback<List<SearchedManga>> callback) {
        new Thread(new Runnable() {
            Document document = null;

            @Override
            public void run() {
                try {
                    document = Jsoup.connect("http://mangakakalot.com/search/" + keyword).get();
                    Elements mangaElements = document.getElementsByClass("item-name");
                    Elements mangaUrlElements = mangaElements.select("a[href]");
                    Elements chapterElements = document.getElementsByClass("item-chapter");

                    List<SearchedManga> searchedMangas = new ArrayList<>();
                    for (int i = 0; i < mangaUrlElements.size(); i++) {
                        if (mangaUrlElements.size() > 0) {
                            Element searchMangaElement = mangaUrlElements.get(i);
                            String mangaUrl = searchMangaElement.select("a").attr("href");
                            String mangaTitle = searchMangaElement.text();

                            Element chapter = chapterElements.get(i);
                            String newestChapter = chapter.attr("title");

                            SearchedManga searchedManga = new SearchedManga();
                            searchedManga.setBaseUrl(mangaUrl);
                            searchedManga.setTitle(mangaTitle);
                            searchedManga.setNewestChapter(newestChapter);
                            searchedMangas.add(searchedManga);
                        }
                    }
                    callback.onSuccess(searchedMangas);
                } catch (IOException e) {
                    callback.onError(new Exception());
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private String getTitle(Elements elements) {
        Element titleElement = elements.first();
        String title = titleElement.getElementsByTag("h1").text();
        return title;
    }

    private String getAuthor(Elements elements) {
        Elements aTagElements = elements.select("a[href]");
        Element authorElement = aTagElements.first();
        return authorElement.text();
    }

    private String getGenres(Elements elements) {
        Elements liElements = elements.select("a[href]");
        String[] genres = new String[liElements.size() - 1];
        for (int i = 1; i < liElements.size(); i++) {
            String genre = liElements.get(i).text();
            genres[i - 1] = genre;
        }
        return StringUtils.joinString(genres);
    }

    private String getStatus(Elements elements) {
        Elements liElements = elements.select("li");
        Element status = liElements.get(2);
        return status.text().split(":")[1].trim();
    }

    private String getDescription(Element element) {
        String[] a = element.text().split(":");
        String description = "";
        if (a.length > 1) {
            description = a[1].trim();
        }
        return description;
    }

    private String getImageUrl(Elements elements) {
        Element imageUrl = elements.first().child(0);
        return imageUrl.attr("src");
    }

    private String getBaseUrl(Element element) {
        Elements baseUrlElements = element.getElementsByClass("itemupdate first").get(0).child(0).select("a[href]");
        return baseUrlElements.attr("href");
    }
}