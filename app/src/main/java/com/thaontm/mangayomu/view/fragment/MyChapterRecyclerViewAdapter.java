package com.thaontm.mangayomu.view.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.model.bean.MangaChapter;
import com.thaontm.mangayomu.view.fragment.MangaChapterFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MangaChapter} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyChapterRecyclerViewAdapter extends RecyclerView.Adapter<MyChapterRecyclerViewAdapter.ViewHolder> {

    private final List<MangaChapter> mChapters;
    private final OnListFragmentInteractionListener mListener;

    public MyChapterRecyclerViewAdapter(List<MangaChapter> items, OnListFragmentInteractionListener listener) {
        mChapters = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_chapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mChapters.get(position);
        holder.mChapter.setText(mChapters.get(position).getChapterName());
        holder.mUploadTime.setText(mChapters.get(position).getUploadedTime());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mChapters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mChapter;
        public final TextView mUploadTime;
        public MangaChapter mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mChapter = (TextView) view.findViewById(R.id.manga_chapter);
            mUploadTime = (TextView) view.findViewById(R.id.manga_upload_time);
        }
    }
}
