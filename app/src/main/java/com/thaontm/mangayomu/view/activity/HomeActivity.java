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
import com.roger.catloadinglibrary.CatLoadingView;
import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.model.bean.MangaDetail;
import com.thaontm.mangayomu.model.bean.MangaHome;
import com.thaontm.mangayomu.model.bean.MangaOverview;
import com.thaontm.mangayomu.model.bean.translation.Translation;
import com.thaontm.mangayomu.model.bean.translation.TranslationResponse;
import com.thaontm.mangayomu.model.provider.Callback;
import com.thaontm.mangayomu.model.provider.KakalotMangaProvider;
import com.thaontm.mangayomu.rest.ApiClient;
import com.thaontm.mangayomu.rest.ApiInterface;
import com.thaontm.mangayomu.view.fragment.MangaOverviewFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements MangaOverviewFragment.OnMangaOverviewInteractionListener {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MaterialSearchView searchView;
    private List<Fragment> fragments;
    private KakalotMangaProvider kakalotMangaProvider;
    private MangaOverviewFragment newMangaOverviewFragment;
    private MangaOverviewFragment popularMangaOverviewFragment;

    private CatLoadingView mCatLoadingView;

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

        // search view
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast.makeText(getApplicationContext(), "onQueryTextSubmit", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                String keyword = query;
                intent.putExtra(SearchActivity.KEYWORD, keyword);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Toast.makeText(getApplicationContext(), "onQueryTextChange", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // cat loading view
        mCatLoadingView = new CatLoadingView();
        if (mCatLoadingView.getDialog() != null) {
            mCatLoadingView.getDialog().setCanceledOnTouchOutside(false);
        } else {
            Toast.makeText(this, "NULL", Toast.LENGTH_SHORT).show();
        }

        //TestAPI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // show loading
        mCatLoadingView.show(getSupportFragmentManager(), "");
        // get data
        kakalotMangaProvider.getHome(new Callback<MangaHome>() {
            @Override
            public void onSuccess(final MangaHome result) {
                // hide loading
                mCatLoadingView.dismiss();
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

    private void TestAPI() {
        String TAG = "TestAPI";
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<TranslationResponse> call = apiService.getTranslationResponse("how are you");
        call.enqueue(new retrofit2.Callback<TranslationResponse>() {
            @Override
            public void onResponse(Call<TranslationResponse> call, Response<TranslationResponse> response) {
                List<Translation> translations = response.body().getTranslations();
                Toast.makeText(getApplicationContext(), translations.get(0).getTranslatedText(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<TranslationResponse> call, Throwable t) {

            }
        });
    }
}