package com.thaontm.mangayomu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.model.bean.MangaOverview;

import org.jsoup.helper.StringUtil;

/**
 * Created by thao on 1/13/2017.
 * Copyright thao 2017.
 */

public class MangaDetailFragment extends Fragment {
    MangaOverview mangaDetail;
    private TextView mTvTitle;
    private TextView mTvGenres;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mangaDetail = getArguments().getParcelable("Manga");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_manga_info, null, false);

        mTvTitle = (TextView) view.findViewById(R.id.tvTitle);
        mTvGenres = (TextView) view.findViewById(R.id.tvGenres);

        mTvTitle.setText(mangaDetail.getName());
        mTvGenres.setText(StringUtil.join(mangaDetail.getGenres(), "\n"));
        return view;
    }
}
