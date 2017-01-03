package com.thaontm.mangayomu.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.model.bean.MangaOverview;
import com.thaontm.mangayomu.view.adapter.MangaOverviewAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private List<MangaOverview> mangaOverviewList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MangaOverviewAdapter mangaOverviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);

        mangaOverviewAdapter = new MangaOverviewAdapter(mangaOverviewList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mangaOverviewAdapter);

        MangaOverview overview = new MangaOverview();
        overview.setPreviewImageUrl("https://3.bp.blogspot.com/-7a4U_6Qy3Do/WCGj0Q5YoSI/AAAAAAAAQK8/pAQWQdOY-00I3DfgSAwmt-3-P3C8e8iPQCLcB/s1600/P_20161108_154451.jpg");
        overview.setName("One Piece");
        overview.setGenres("Phieu luu");
        mangaOverviewList.add(overview);

        MangaOverview overview1 = new MangaOverview();
        overview1.setPreviewImageUrl("https://3.bp.blogspot.com/-7a4U_6Qy3Do/WCGj0Q5YoSI/AAAAAAAAQK8/pAQWQdOY-00I3DfgSAwmt-3-P3C8e8iPQCLcB/s1600/P_20161108_154451.jpg");
        overview1.setName("Naruto");
        overview1.setGenres("Phieu luu");
        mangaOverviewList.add(overview1);

        MangaOverview overview2 = new MangaOverview();
        overview2.setPreviewImageUrl("https://3.bp.blogspot.com/-7a4U_6Qy3Do/WCGj0Q5YoSI/AAAAAAAAQK8/pAQWQdOY-00I3DfgSAwmt-3-P3C8e8iPQCLcB/s1600/P_20161108_154451.jpg");
        overview2.setName("One Piece 2");
        overview2.setGenres("Phieu luu");
        mangaOverviewList.add(overview2);


    }

}
