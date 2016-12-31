package com.thaontm.mangayomu.view;

import android.app.Activity;
import android.widget.SearchView;

/**
 * Created by thao on 12/31/2016.
 * Copyright thao 2017.
 */

public class ActionBarSearchView extends Activity implements SearchView.OnQueryTextListener {
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
