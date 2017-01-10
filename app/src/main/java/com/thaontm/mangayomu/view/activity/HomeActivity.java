package com.thaontm.mangayomu.view.activity;

import android.app.SearchManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.model.bean.MangaOverview;
import com.thaontm.mangayomu.view.adapter.MangaOverviewAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private List<MangaOverview> mangaOverviewList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MangaOverviewAdapter mangaOverviewAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*
        Toolbar with Search and Login button
         */
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        /*
        Recycler View with sample data
         */

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);

        mangaOverviewAdapter = new MangaOverviewAdapter(mangaOverviewList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mangaOverviewAdapter);

        MangaOverview overview = new MangaOverview();
        overview.setPreviewImageUrl("http://ibdp.huluim.com/video/12816667?size=320x180");
        overview.setName("One Piece");
        overview.setGenres("Phieu luu");
        mangaOverviewList.add(overview);

        MangaOverview overview1 = new MangaOverview();
        overview1.setPreviewImageUrl("http://ibdp.huluim.com/video/12816667?size=320x180");
        overview1.setName("Naruto");
        overview1.setGenres("Phieu luu");
        mangaOverviewList.add(overview1);

        MangaOverview overview2 = new MangaOverview();
        overview2.setPreviewImageUrl("http://ibdp.huluim.com/video/12816667?size=320x180");
        overview2.setName("Naruto");
        overview2.setGenres("Phieu luu");
        mangaOverviewList.add(overview2);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_bar_menu, menu);
        /*
        Tao SearchView va plugin vao trong SearchManager
         */
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search_bar));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(HomeActivity.this, newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return true;
    }
}
