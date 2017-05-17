package com.thaontm.mangayomu.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.model.bean.MangaOverview;

import java.util.List;

public class MangaOverviewFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 2;
    private OnMangaOverviewInteractionListener mListener;
    private RecyclerView recyclerView = null;
    private List<MangaOverview> mangaOverviews = null;

    public MangaOverviewFragment() {
    }

    @SuppressWarnings("unused")
    public static MangaOverviewFragment newInstance(int columnCount) {
        MangaOverviewFragment fragment = new MangaOverviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mangaoverview_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            if (mangaOverviews != null) {
                recyclerView.setAdapter(new MyMangaOverviewRecyclerViewAdapter(mangaOverviews, mListener));
                mangaOverviews = null;
            }
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMangaOverviewInteractionListener) {
            mListener = (OnMangaOverviewInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMangaOverviewInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setMangaList(List<MangaOverview> mangaOverviews) {
        if (recyclerView != null)
            recyclerView.setAdapter(new MyMangaOverviewRecyclerViewAdapter(mangaOverviews, mListener));
        else
            this.mangaOverviews = mangaOverviews;
    }


    public interface OnMangaOverviewInteractionListener {
        // TODO: Update argument type and name
        void onMangaOverviewInteraction(MangaOverview item);
    }
}
