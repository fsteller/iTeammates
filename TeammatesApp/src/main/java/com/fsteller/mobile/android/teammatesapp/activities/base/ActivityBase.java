package com.fsteller.mobile.android.teammatesapp.activities.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.fsteller.mobile.android.teammatesapp.BuildConfig;
import com.fsteller.mobile.android.teammatesapp.TeammatesApp;
import com.fsteller.mobile.android.teammatesapp.utils.VersionTools;
import com.google.analytics.tracking.android.EasyTracker;

/**
 * Created by fsteller on 12/30/13.
 */
public abstract class ActivityBase extends Activity {

    protected TeammatesApp app;
    protected static final int SOFT_INPUT_MODE_VISIBLE = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
    protected static final int SOFT_INPUT_MODE_HIDDEN = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG)
            VersionTools.enableStrictMode();

        this.app = (TeammatesApp) getApplication();
        this.getWindow().setSoftInputMode(SOFT_INPUT_MODE_HIDDEN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.app = null;
    }

    protected void showMessage(final String msg, final int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ActivityBase.this, msg, duration).show();
            }
        });
    }

    public void setIsKeyBoardEnabled(final boolean enable) {
        getWindow().setSoftInputMode(enable ? SOFT_INPUT_MODE_VISIBLE : SOFT_INPUT_MODE_HIDDEN);
    }

    protected static boolean isNullOrEmpty(String txt) {
        return txt == null || txt.isEmpty();
    }

}
