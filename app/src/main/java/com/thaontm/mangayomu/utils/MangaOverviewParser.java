package com.thaontm.mangayomu.utils;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

public class MangaOverviewParser {

	public MangaOverviewParser() {

	}

	public String getImageSrc(Element div) {
		Elements aTags = div.getElementsByTag("a");
		if (aTags.size() > 0) {
			Elements imgTags = aTags.get(0).getElementsByTag("img");
			if (imgTags.size() > 0) {
				String mangaImageSrc = imgTags.get(0).attr("src");
				return mangaImageSrc;
			}
		}
		return null;
	}

	public String getTitle(Element div) {
		Elements aTags = div.getElementsByTag("a");
		if (aTags.size() > 1) {
			Elements spanTags = aTags.get(1).getElementsByTag("span");
			if (spanTags.size() > 0) {
				String mangaTitle = spanTags.get(0).text();
				return mangaTitle;
			}
		}
		return null;
	}

	public List<String> getGenres(Element div) {
		Elements pTags = div.getElementsByTag("p");
		List<String> genres = new LinkedList<String>();
		if (pTags.size() > 0) {
			Element pTag = pTags.get(0);
			Elements aTags = pTag.getElementsByTag("a");
			for (Element aTag : aTags) {
				String genre = aTag.text();
				genres.add(genre);
			}
		}
		return genres;
	}
}
