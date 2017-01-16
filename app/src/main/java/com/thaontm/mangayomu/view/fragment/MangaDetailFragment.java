package com.thaontm.mangayomu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.model.bean.MangaDetail;

/**
 * Created by thao on 1/13/2017.
 * Copyright thao 2017.
 */

public class MangaDetailFragment extends Fragment {
    MangaDetail mangaDetail = new MangaDetail();
    private TextView mTvGenres;
    private TextView mTvState;
    private TextView mTvDescription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_manga_info, null, false);
        mangaDetail.setMDescription("Description here");
        mangaDetail.setMGenres("Genres: ...");
        mangaDetail.setMState("Status here");

        mTvDescription = (TextView) view.findViewById(R.id.tvDescription);
        mTvGenres = (TextView) view.findViewById(R.id.tvGenres);
        mTvState = (TextView) view.findViewById(R.id.tvState);

        mTvDescription.setText(mangaDetail.getMDescription());
        mTvGenres.setText(mangaDetail.getMGenres());
        mTvState.setText(mangaDetail.getMState());
        return view;
    }
}
