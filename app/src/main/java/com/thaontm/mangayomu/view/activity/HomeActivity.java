package com.thaontm.mangayomu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.model.bean.MangaDetail;
import com.thaontm.mangayomu.model.bean.MangaHome;
import com.thaontm.mangayomu.model.bean.MangaOverview;
import com.thaontm.mangayomu.model.provider.Callback;
import com.thaontm.mangayomu.model.provider.KakalotMangaProvider;
import com.thaontm.mangayomu.view.fragment.MangaOverviewFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements MangaOverviewFragment.OnMangaOverviewInteractionListener {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private SearchView searchView;
    private List<Fragment> fragments;
    private KakalotMangaProvider kakalotMangaProvider;
    private MangaOverviewFragment newMangaOverviewFragment;
    private MangaOverviewFragment popularMangaOverviewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        newMangaOverviewFragment = new MangaOverviewFragment();
        popularMangaOverviewFragment = new MangaOverviewFragment();
        kakalotMangaProvider = new KakalotMangaProvider();

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        kakalotMangaProvider.getHome(new Callback<MangaHome>() {
            @Override
            public void onSuccess(final MangaHome result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        newMangaOverviewFragment.setMangaList(result.getNewMangas());
                        popularMangaOverviewFragment.setMangaList(result.getPopularMangas());
                    }
                });
            }

            @Override
            public void onError(Throwable what) {

            }
        });
    }

    private void setupViewPager(ViewPager mViewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(newMangaOverviewFragment, "NEWEST");
        adapter.addFragment(popularMangaOverviewFragment, "POPULAR");
        mViewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_bar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                String keyword = query;
                intent.putExtra(SearchActivity.KEYWORD, keyword);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return true;
    }


    public void notifyDataChanged() {
        for (Fragment f : fragments) {
        }
    }

    @Override
    public void onMangaOverviewInteraction(MangaOverview item) {
        kakalotMangaProvider.getMangaDetail(item.getBaseUrl(), new Callback<MangaDetail>() {
            @Override
            public void onSuccess(final MangaDetail result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(HomeActivity.this, MangaDetailActivity.class);
                        intent.putExtra(MangaDetailActivity.MANGA_DETAIL, result);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onError(Throwable what) {

            }
        });

    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
