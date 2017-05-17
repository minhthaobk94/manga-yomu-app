package com.thaontm.mangayomu.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.model.bean.MangaDetail;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thao on 4/11/2017.
 * Copyright thao 2017.
 */

public class MangaDetailFragment extends Fragment {
    private static final String MANGA_BASE_URL = "base_url";
    private MangaDetail mangaDetail;
    @BindView(R.id.author_name)
    TextView mAuthor;
    @BindView(R.id.manga_status)
    TextView mStatus;
    @BindView(R.id.manga_genres)
    TextView mGenres;
    @BindView(R.id.manga_description)
    TextView mDescription;

    public MangaDetailFragment() {
    }

    @SuppressWarnings("unused")
    public static MangaDetailFragment newInstance(MangaDetail mangaDetail) {
        MangaDetailFragment fragment = new MangaDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(MANGA_BASE_URL, mangaDetail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mangaDetail = (MangaDetail) getArguments().getSerializable(MANGA_BASE_URL);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manga_detail_info, container, false);
        ButterKnife.bind(view);
        mAuthor = (TextView) view.findViewById(R.id.author_name);
        mStatus = (TextView) view.findViewById(R.id.manga_status);
        mGenres = (TextView) view.findViewById(R.id.manga_genres);
        mDescription = (TextView) view.findViewById(R.id.manga_description);

        mAuthor.setText(mangaDetail.getAuthor());
        mStatus.setText(mangaDetail.getStatus());
        mGenres.setText(mangaDetail.getGenres());
        mDescription.setText(mangaDetail.getDescription());

        return view;
    }
}
