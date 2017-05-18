package com.thaontm.mangayomu.view.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.model.bean.MangaOverview;
import com.thaontm.mangayomu.model.bean.SearchedManga;

import java.util.List;

/**
 * Created by thao on 5/5/2017.
 * Copyright thao 2017.
 */

public class MySearchMangaRecyclerViewAdapter extends RecyclerView.Adapter<MySearchMangaRecyclerViewAdapter.ViewHolder> {
    private final List<SearchedManga> mValues;
    private OnItemClickListener mListener;

    public MySearchMangaRecyclerViewAdapter(List<SearchedManga> mValues, OnItemClickListener mListener) {
        this.mValues = mValues;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_manga, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mMangaName.setText(mValues.get(position).getTitle());
        holder.mNewestChapter.setText(mValues.get(position).getNewestChapter());


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onItemClick(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public interface OnItemClickListener {
        void onItemClick(MangaOverview mangaOverview);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mMangaName;
        public final TextView mNewestChapter;
        public SearchedManga mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mMangaName = (TextView) view.findViewById(R.id.search_manga_title);
            mNewestChapter = (TextView) view.findViewById(R.id.newest_chapter);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mMangaName.getText() + "'";
        }
    }
}
