package com.thaontm.mangayomu.model.provider;

public class MangaProviderFactory {
    private static MangaProviderFactory instance;
    private MangaProvider mangaProvider;

    private MangaProviderFactory () {
        mangaProvider = new MangaParkProvider();
    }

    public static MangaProviderFactory getInstance() {
        if (instance == null) {
            instance = new MangaProviderFactory();
        }
        return instance;
    }

    public MangaProvider getMangaProvider() {
        return mangaProvider;
    }
}
