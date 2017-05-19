package com.thaontm.mangayomu.utils;

import android.app.Activity;
import android.util.Log;

public class BusyIndicatorManager {

    //region Property

    private Activity mCurrentActivity;
    private BusyIndicator mBusyIndicator;

    //endregion

    public BusyIndicatorManager(Activity activity) {
        this.mCurrentActivity = activity;
        this.mBusyIndicator = new BusyIndicator(mCurrentActivity);
    }

    //region INavigator implement

    public void showBusyIndicator() {
        if (mCurrentActivity != null && mBusyIndicator !=null && !mBusyIndicator.isShowing()) {
            mBusyIndicator.show();
        }
    }

    public void hideBusyIndicator() {
        if (isBusyIndicatorShowing()) {
            try {
                mBusyIndicator.dismiss();
            } catch (Exception e) {
                Log.e(BusyIndicatorManager.class.getSimpleName(), e.getMessage());
            } finally {
                mBusyIndicator = null;
            }
        }
    }

    public boolean isBusyIndicatorShowing() {
        return mBusyIndicator != null && mBusyIndicator.isShowing();
    }

    //endregion
}
