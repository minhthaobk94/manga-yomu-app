package com.thaontm.mangayomu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.model.bean.MangaDetail;
import com.thaontm.mangayomu.model.bean.MangaHome;
import com.thaontm.mangayomu.model.bean.MangaOverview;
import com.thaontm.mangayomu.model.provider.Callback;
import com.thaontm.mangayomu.model.provider.KakalotMangaProvider;
import com.thaontm.mangayomu.utils.BusyIndicatorManager;
import com.thaontm.mangayomu.view.fragment.MangaOverviewFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements MangaOverviewFragment.OnMangaOverviewInteractionListener, KakalotMangaProvider.KakalotMangaProviderListener {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MaterialSearchView searchView;
    private KakalotMangaProvider kakalotMangaProvider;
    private MangaOverviewFragment newMangaOverviewFragment;
    private MangaOverviewFragment popularMangaOverviewFragment;

    private BusyIndicatorManager mBusyIndicatorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        newMangaOverviewFragment = new MangaOverviewFragment();
        popularMangaOverviewFragment = new MangaOverviewFragment();
        kakalotMangaProvider = new KakalotMangaProvider(this);

        // init BusyIndicator
        mBusyIndicatorManager = new BusyIndicatorManager(this);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        // search view
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                String keyword = query;
                intent.putExtra(SearchActivity.KEYWORD, keyword);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // show loading
        mBusyIndicatorManager.showBusyIndicator();
        // get data
        kakalotMangaProvider.getHome(new Callback<MangaHome>() {
            @Override
            public void onSuccess(final MangaHome result) {
                // hide loading
                mBusyIndicatorManager.hideBusyIndicator();
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
                // hide loading
                mBusyIndicatorManager.hideBusyIndicator();
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
        getMenuInflater().inflate(R.menu.menu_search_bar, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onParsingError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mBusyIndicatorManager != null && mBusyIndicatorManager.isBusyIndicatorShowing()) {
                    mBusyIndicatorManager.hideBusyIndicator();
                }
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.NETWORK_ERR_MESSAGE), Toast.LENGTH_SHORT).show();
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