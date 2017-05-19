package com.thaontm.mangayomu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.model.bean.MangaDetail;
import com.thaontm.mangayomu.model.bean.MangaOverview;
import com.thaontm.mangayomu.model.bean.SearchedManga;
import com.thaontm.mangayomu.model.provider.Callback;
import com.thaontm.mangayomu.model.provider.KakalotMangaProvider;
import com.thaontm.mangayomu.utils.BusyIndicatorManager;
import com.thaontm.mangayomu.view.fragment.MySearchMangaRecyclerViewAdapter;

import java.util.List;

import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements MySearchMangaRecyclerViewAdapter.OnItemClickListener, KakalotMangaProvider.KakalotMangaProviderListener {
    static final String KEYWORD = "keyword";
    private RecyclerView recyclerView = null;
    private KakalotMangaProvider kakalotMangaProvider;

    private Toolbar mToolbar;
    private MaterialSearchView searchView;
    private BusyIndicatorManager busyIndicatorManager;
    private TextView tvNoItemsFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView = ButterKnife.findById(this, R.id.list);
        kakalotMangaProvider = new KakalotMangaProvider(this);
        tvNoItemsFound = (TextView) findViewById(R.id.tvNoItemsFound);

        // init BusyIndicatorManager
        busyIndicatorManager = new BusyIndicatorManager(this);

        // do searching
        //// show loading indicator
        busyIndicatorManager.showBusyIndicator();
        //// search
        final Intent intent = getIntent();
        String keyword = intent.getStringExtra(KEYWORD);
        kakalotMangaProvider.search(keyword, new Callback<List<SearchedManga>>() {
            @Override
            public void onSuccess(final List<SearchedManga> result) {
                // hide loading indicator
                busyIndicatorManager.hideBusyIndicator();
                // update view
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateViewAfterSearching(result);
                    }
                });
            }

            @Override
            public void onError(Throwable what) {
            }
        });

        // toolbar
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // set title
        setTitle(keyword);

        // search view
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                // change title
                setTitle(query);
                // do searching
                //// show loading indicator
                busyIndicatorManager.showBusyIndicator();
                //// search
                kakalotMangaProvider.search(query, new Callback<List<SearchedManga>>() {
                    @Override
                    public void onSuccess(final List<SearchedManga> result) {
                        // hide loading indicator
                        busyIndicatorManager.hideBusyIndicator();
                        // update view
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateViewAfterSearching(result);
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable what) {
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_bar, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    private void updateViewAfterSearching(List<SearchedManga> result) {
        if (recyclerView == null || tvNoItemsFound == null) return;
        // show no items found
        if (result.size() == 0) {
            tvNoItemsFound.setVisibility(View.VISIBLE);
        } else {
            tvNoItemsFound.setVisibility(View.GONE);
        }
        // update recycler view
        recyclerView.setAdapter(new MySearchMangaRecyclerViewAdapter(result, SearchActivity.this));
    }

    @Override
    public void onItemClick(MangaOverview mangaOverview) {
        kakalotMangaProvider.getMangaDetail(mangaOverview.getBaseUrl(), new Callback<MangaDetail>() {
            @Override
            public void onSuccess(final MangaDetail result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SearchActivity.this, MangaDetailActivity.class);
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
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.NETWORK_ERR_MESSAGE), Toast.LENGTH_SHORT).show();
            }
        });
    }
}