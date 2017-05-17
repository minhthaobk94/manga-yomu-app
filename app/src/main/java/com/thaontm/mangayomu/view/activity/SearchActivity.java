package com.thaontm.mangayomu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

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
    private SearchView searchView;

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

        setTitle("Search");

        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setLayoutParams(new Toolbar.LayoutParams(Gravity.RIGHT));
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