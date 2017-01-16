package com.thaontm.mangayomu.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.view.adapter.MangaDetailViewPagerAdapter;
import com.thaontm.mangayomu.view.fragment.MangaDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thao on 1/13/2017.
 * Copyright thao 2017.
 */

public class MangaDetailActivity extends AppCompatActivity {
    private TabLayout mTabLayoutDetail;
    private MangaDetailViewPagerAdapter mDetailViewPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_scrollview);

        mTabLayoutDetail = (TabLayout) findViewById(R.id.detail_tabs);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MangaDetailFragment());
        fragments.add(new MangaDetailFragment());

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mDetailViewPagerAdapter = new MangaDetailViewPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mDetailViewPagerAdapter);
        mTabLayoutDetail.setupWithViewPager(mViewPager);
    }
}
