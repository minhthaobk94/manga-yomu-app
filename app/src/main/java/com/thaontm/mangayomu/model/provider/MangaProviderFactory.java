package com.thaontm.mangayomu.model.provider;

import com.pixplicity.easyprefs.library.Prefs;
import com.thaontm.mangayomu.model.bean.ProviderConstants;

public class MangaProviderFactory {
    private static MangaProviderFactory instance;
    private MangaProvider mangaProvider;

    private MangaProviderFactory () {
        if (Prefs.getInt(ProviderConstants.CURRENT_PROVIDER, ProviderConstants.KAKALOT_PROVIDER) == ProviderConstants.MANGAPARK_PROVIDER) {
            mangaProvider = new MangaParkProvider();
        } else {
            mangaProvider = new KakalotMangaProvider();
        }
    }

    public static MangaProviderFactory getInstance() {
        if (instance == null) {
            instance = new MangaProviderFactory();
        }
        return instance;
    }

    public void changeProvider() {
        instance = new MangaProviderFactory();
    }

    public MangaProvider getMangaProvider() {
        return mangaProvider;
    }
}
