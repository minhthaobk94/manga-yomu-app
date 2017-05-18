package com.thaontm.mangayomu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.model.bean.MangaDetail;
import com.thaontm.mangayomu.model.bean.MangaOverview;
import com.thaontm.mangayomu.model.bean.SearchedManga;
import com.thaontm.mangayomu.model.provider.Callback;
import com.thaontm.mangayomu.model.provider.KakalotMangaProvider;
import com.thaontm.mangayomu.view.fragment.MySearchMangaRecyclerViewAdapter;

import java.util.List;

import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements MySearchMangaRecyclerViewAdapter.OnItemClickListener {
    static final String KEYWORD = "keyword";
    private RecyclerView recyclerView = null;
    private List<MangaOverview> mangaOverviews = null;
    private KakalotMangaProvider kakalotMangaProvider;
    private MangaOverview mangaOverview;

    private Toolbar mToolbar;
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView = ButterKnife.findById(this, R.id.list);
        kakalotMangaProvider = new KakalotMangaProvider();

        final Intent intent = getIntent();
        String keyword = intent.getStringExtra(KEYWORD);
        kakalotMangaProvider.search(keyword, new Callback<List<SearchedManga>>() {
            @Override
            public void onSuccess(final List<SearchedManga> result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MySearchMangaRecyclerViewAdapter adapter = new MySearchMangaRecyclerViewAdapter(result, SearchActivity.this);
                        recyclerView.setAdapter(adapter);
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
                // Toast.makeText(getApplicationContext(), "onQueryTextSubmit", Toast.LENGTH_SHORT).show();
                // change title
                setTitle(query);
                // do searching
                kakalotMangaProvider.search(query, new Callback<List<SearchedManga>>() {
                    @Override
                    public void onSuccess(final List<SearchedManga> result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MySearchMangaRecyclerViewAdapter adapter = new MySearchMangaRecyclerViewAdapter(result, SearchActivity.this);
                                recyclerView.setAdapter(adapter);
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
                // Toast.makeText(getApplicationContext(), "onQueryTextChange", Toast.LENGTH_SHORT).show();
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
}