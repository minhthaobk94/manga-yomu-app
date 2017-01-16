package com.thaontm.mangayomu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.model.bean.MangaOverview;
import com.thaontm.mangayomu.model.dao.SqlLiteDbHelper;
import com.thaontm.mangayomu.view.adapter.MangaOverviewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thao on 1/10/2017.
 * Copyright thao 2017.
 */

public class ListMangaFragment extends Fragment {
    RecyclerView recyclerView;
    private MangaOverviewAdapter mangaOverviewAdapter;
    private SqlLiteDbHelper mSqlLiteDbHelper;
    private List<MangaOverview> mMangas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reccycler_view, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        //List<MangaOverview> mangaOverviewList = new ArrayList<>();
        /*for (int i = 0; i < 100; i++) {
            MangaOverview mangaOverview = new MangaOverview();
            mangaOverview.setPreviewImageUrl("http://ibdp.huluim.com/video/12816667?size=320x180");
            mangaOverview.setName("Name " + i);
            mangaOverview.setGenres("Genres " + i);
            mangaOverviewList.add(mangaOverview);
        }*/
        initData();
        mangaOverviewAdapter = new MangaOverviewAdapter(mMangas, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mangaOverviewAdapter);

        return view;
    }

    public void initData(){
        mSqlLiteDbHelper = new SqlLiteDbHelper(getActivity());
        mSqlLiteDbHelper.openDataBase();
        mMangas = mSqlLiteDbHelper.GetMangas();
    }
}
