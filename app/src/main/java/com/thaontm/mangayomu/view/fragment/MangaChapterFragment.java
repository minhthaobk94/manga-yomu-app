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
import com.thaontm.mangayomu.model.bean.MangaChapter;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MangaChapterFragment extends Fragment {

    private static final String MANGA_BASE_URL = "base_url";
    private static final String ARG_COLUMN_COUNT = "column-count";
    public static MangaChapterFragment instance = null;
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<MangaChapter> mangaChapters;
    private MyChapterRecyclerViewAdapter myChapterRecyclerViewAdapter;
    private RecyclerView recyclerView;
    public MangaChapterFragment() {
        instance = this;
    }

    public static MangaChapterFragment newInstance() {
        return new MangaChapterFragment();
    }

    public void setMangaChapters(List<MangaChapter> mangaChapters) {
        if (recyclerView != null) {
            recyclerView.setAdapter(new MyChapterRecyclerViewAdapter(mangaChapters, mListener));
        } else {
            this.mangaChapters = mangaChapters;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            if (mangaChapters != null) {
                myChapterRecyclerViewAdapter = new MyChapterRecyclerViewAdapter(mangaChapters, mListener);
                mangaChapters = null;
                recyclerView.setAdapter(myChapterRecyclerViewAdapter);
            }
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(MangaChapter item);
    }

}
