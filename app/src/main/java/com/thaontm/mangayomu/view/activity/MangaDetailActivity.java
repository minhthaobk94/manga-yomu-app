package com.thaontm.mangayomu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.model.bean.MangaChapter;
import com.thaontm.mangayomu.model.bean.MangaDetail;
import com.thaontm.mangayomu.model.provider.Callback;
import com.thaontm.mangayomu.model.provider.KakalotMangaProvider;
import com.thaontm.mangayomu.view.fragment.MangaChapterFragment;
import com.thaontm.mangayomu.view.fragment.MangaDetailFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thao on 1/13/2017.
 * Copyright thao 2017.
 */

public class MangaDetailActivity extends AppCompatActivity implements MangaChapterFragment.OnListFragmentInteractionListener, KakalotMangaProvider.KakalotMangaProviderListener {

    static final String MANGA_DETAIL = "manga_detail";
    static final String CHAPTER = "manga_chapter";
    @BindView(R.id.manga_image)
    ImageView mMangaImage;
    @BindView(R.id.detail_tabs)
    TabLayout mDetailTabs;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private MangaDetailFragment mangaDetailFragment;
    private MangaChapterFragment mangaChapterFragment;
    private MangaDetail mangaDetail;
    private MangaChapter mangaChapter;
    private KakalotMangaProvider kakalotMangaProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_detail);
        ButterKnife.bind(this);
        mangaDetail = (MangaDetail) getIntent().getSerializableExtra(MANGA_DETAIL);
        mangaChapter = (MangaChapter) getIntent().getSerializableExtra(CHAPTER);
        Picasso.with(this).load(mangaDetail.getImageUrl()).fit().into(mMangaImage);
        mangaChapterFragment = new MangaChapterFragment();

        kakalotMangaProvider = new KakalotMangaProvider(this);
        kakalotMangaProvider.getMangaChapters(mangaDetail.getBaseUrl(), new Callback<List<MangaChapter>>() {
            @Override
            public void onSuccess(final List<MangaChapter> result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mangaChapterFragment.setMangaChapters(result);
                    }
                });
            }

            @Override
            public void onError(Throwable what) {

            }
        });

        mangaDetailFragment = MangaDetailFragment.newInstance(mangaDetail);

        setupViewPager(mViewPager);
        mDetailTabs.setupWithViewPager(mViewPager);

        // toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initCollapsingToolbar();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupViewPager(ViewPager mViewPager) {
        HomeActivity.ViewPagerAdapter adapter = new HomeActivity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(mangaDetailFragment, "INFO");
        adapter.addFragment(mangaChapterFragment, "CHAPTER");
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onListFragmentInteraction(MangaChapter item) {
        Intent intent = new Intent(MangaDetailActivity.this, ReadMangaActivity.class);
        intent.putExtra(MangaDetailActivity.CHAPTER, item);
        startActivity(intent);
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(mangaDetail.getTitle());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public void onParsingError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.NETWORK_ERR_MESSAGE), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
