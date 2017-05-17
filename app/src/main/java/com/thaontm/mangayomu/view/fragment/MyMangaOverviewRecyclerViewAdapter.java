package com.thaontm.mangayomu.view.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.model.bean.MangaOverview;
import com.thaontm.mangayomu.utils.StringUtils;
import com.thaontm.mangayomu.view.fragment.MangaOverviewFragment.OnMangaOverviewInteractionListener;

import java.util.List;


public class MyMangaOverviewRecyclerViewAdapter extends RecyclerView.Adapter<MyMangaOverviewRecyclerViewAdapter.ViewHolder> {

    private final List<MangaOverview> mValues;
    private final OnMangaOverviewInteractionListener mListener;

    public MyMangaOverviewRecyclerViewAdapter(List<MangaOverview> items, OnMangaOverviewInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mangaoverview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Picasso.with(holder.mMangaImage.getContext()).load(mValues.get(position).getImageUrl()).resize(400,600).centerCrop().into(holder.mMangaImage);
        holder.mMangaName.setText(StringUtils.shorten(mValues.get(position).getTitle(), 17));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onMangaOverviewInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mMangaImage;
        public final TextView mMangaName;
        public MangaOverview mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mMangaImage = (ImageView) view.findViewById(R.id.manga_image);
            mMangaName = (TextView) view.findViewById(R.id.manga_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mMangaName.getText() + "'";
        }
    }
}
