package com.thaontm.mangayomu.view.activity;

import android.app.SearchManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.model.bean.MangaOverview;
import com.thaontm.mangayomu.model.engine.MangaOverviewService;
import com.thaontm.mangayomu.view.adapter.ViewPagerAdapter;
import com.thaontm.mangayomu.view.fragment.ListMangaFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static final String MANGA_RESOUCE_URL = "http://kissmanga.com/";

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private TabLayout mTabLayout;
    private SearchView searchView;
    private List<Fragment> fragments;
    private ArrayList<MangaOverview> listManga = new ArrayList<>();
    private ArrayList<MangaOverview> mangaOverviewsTmp = new ArrayList<MangaOverview>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initData();
        mangaOverviewsTmp.addAll(listManga);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        fragments = new ArrayList<>();
        fragments.add(new ListMangaFragment());
        fragments.add(new ListMangaFragment());
        fragments.add(new ListMangaFragment());
        for (Fragment f : fragments) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("LIST_MANGA", listManga);
            f.setArguments(bundle);
        }

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_bar_menu, menu);

        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search_bar));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && newText.length() > 0) {
                    listManga.clear();
                    for (MangaOverview m : mangaOverviewsTmp) {
                        if (m.getName().toLowerCase().contains(newText.toLowerCase())) {
                            listManga.add(m);
                        }
                    }
                } else {
                    listManga.clear();
                    listManga.addAll(mangaOverviewsTmp);
                }
                notifyDataChanged();
                return true;
            }
        });
        return true;
    }

    public void initData() {
        new FetchMangaOverViewTask().execute();
    }

    public void notifyDataChanged() {
        for (Fragment f : fragments) {
            ((ListMangaFragment) f).notifyDataSetChanged();
        }
    }

    class FetchMangaOverViewTask extends AsyncTask<Void, Void, List<MangaOverview>> {
        @Override
        protected List<MangaOverview> doInBackground(Void... params) {
            MangaOverviewService service = new MangaOverviewService(HomeActivity.this);
            return service.fetchMangaOverviews(MANGA_RESOUCE_URL);
        }

        @Override
        protected void onPostExecute(List<MangaOverview> resutls) {
            listManga.clear();
            listManga.addAll(resutls);
            mangaOverviewsTmp.clear();
            mangaOverviewsTmp.addAll(listManga);
            notifyDataChanged();
        }
    }

}
