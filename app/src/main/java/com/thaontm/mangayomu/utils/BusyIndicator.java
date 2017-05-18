package com.thaontm.mangayomu.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.thaontm.mangayomu.R;

public class BusyIndicator extends Dialog {

    //region Property

    private TextView mTextViewTitle;

    //endregion

    //region Constructor

    public BusyIndicator(Context context) {
        super(context);

        initialize();
    }

    protected BusyIndicator(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

        initialize();
    }

    public BusyIndicator(Context context, int themeResId) {
        super(context, themeResId);

        initialize();
    }

    //endregion

    //region Private methods

    private void initialize() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.busy_indicator);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setCancelable(false);
    }

    //endregion

}

