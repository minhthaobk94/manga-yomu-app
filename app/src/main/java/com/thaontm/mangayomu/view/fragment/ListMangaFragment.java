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
import com.thaontm.mangayomu.view.adapter.MangaOverviewAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by thao on 1/10/2017.
 * Copyright thao 2017.
 */

public class ListMangaFragment extends Fragment {
    RecyclerView recyclerView;
    private MangaOverviewAdapter mangaOverviewAdapter;
    private List<MangaOverview> mMangas;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        mMangas = bundle.getParcelableArrayList("LIST_MANGA");
        Collections.shuffle(mMangas);
    }

    public void notifyDataSetChanged() {
        if (recyclerView != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reccycler_view, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        mangaOverviewAdapter = new MangaOverviewAdapter(mMangas, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mangaOverviewAdapter);

        return view;
    }

}
