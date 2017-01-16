package com.thaontm.mangayomu.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.model.bean.MangaOverview;
import com.thaontm.mangayomu.view.activity.HomeActivity;
import com.thaontm.mangayomu.view.activity.MangaDetailActivity;

import java.util.List;

/**
 * Created by thao on 12/29/2016.
 * Copyright thao 2017.
 */

public class MangaOverviewAdapter extends RecyclerView.Adapter<MangaOverviewAdapter.MangaOverviewHolder> {
    private List<MangaOverview> mangaOverviewList;
    private Context context;

    class MangaOverviewHolder extends RecyclerView.ViewHolder {
        ImageView imageManga;
        TextView txtName;
        TextView txtGenres;

        MangaOverviewHolder(View itemView) {
            super(itemView);
            imageManga = (ImageView) itemView.findViewById(R.id.image_item);
            txtName = (TextView) itemView.findViewById(R.id.item_name);
            txtGenres = (TextView) itemView.findViewById(R.id.item_genres);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("manga", (MangaOverview)txtName.getTag());
                    Intent intent = new Intent(context, MangaDetailActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }

    public MangaOverviewAdapter(List<MangaOverview> mangaOverviewList, Context context) {
        this.mangaOverviewList = mangaOverviewList;
        this.context = context;
    }

    @Override
    public MangaOverviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_manga, parent, false);
        return new MangaOverviewHolder(view);
    }

    @Override
    public void onBindViewHolder(MangaOverviewHolder holder, int position) {
        MangaOverview mangaOverview = mangaOverviewList.get(position);
        Picasso.with(context)
                .load(mangaOverview.getPreviewImageUrl())
                .into(holder.imageManga);
        holder.txtName.setText(mangaOverview.getName());
        holder.txtGenres.setText(mangaOverview.getGenres());
        holder.txtName.setTag(mangaOverview);
    }


    @Override
    public int getItemCount() {
        return mangaOverviewList.size();
    }

}
