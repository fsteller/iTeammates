package com.fsteller.mobile.android.teammatesapp.activities.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.fsteller.mobile.android.teammatesapp.BuildConfig;
import com.fsteller.mobile.android.teammatesapp.TeammatesApp;
import com.fsteller.mobile.android.teammatesapp.utils.VersionTools;
import com.google.analytics.tracking.android.EasyTracker;

/**
 * Project: iTeammates
 * Subpackage: activities.base
 * <p/>
 * Description:
 * Created by fsteller on 12/30/13.
 */
public abstract class ActivityBase extends Activity {

    //<editor-fold desc="Constants">

    private static final int SOFT_INPUT_MODE_VISIBLE = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
    private static final int SOFT_INPUT_MODE_HIDDEN = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;

    //</editor-fold>
    //<editor-fold desc="Variables">
    protected final HideSoftInputClass mHideSoftInputClass = new HideSoftInputClass();
    protected TeammatesApp app;
    private InputMethodManager imm = null;

    //</editor-fold>

    //<editor-fold desc="Public">

    protected static boolean isNullOrEmpty(final String txt) {
        return txt == null || txt.isEmpty();
    }

    //</editor-fold>
    //<editor-fold desc="Overridden">

    private void setIsKeyBoardEnabled(final boolean enable) {
        getWindow().setSoftInputMode(enable ? SOFT_INPUT_MODE_VISIBLE : SOFT_INPUT_MODE_HIDDEN);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG)
            VersionTools.enableStrictMode();

        setIsKeyBoardEnabled(true);
        app = (TeammatesApp) getApplication();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop() {
        EasyTracker.getInstance(this).activityStop(this);
        super.onStop();
    }

    //</editor-fold>
    //<editor-fold desc="Protected">

    @Override
    protected void onDestroy() {
        this.app = null;
        this.imm = null;
        super.onDestroy();
    }

    protected void hideSoftKeyboard(final View view) {
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected void showMessage(final String msg, final int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ActivityBase.this, msg, duration).show();
            }
        });
    }

    //</editor-fold>

    //<editor-fold desc="Inner Classes">

    private final class HideSoftInputClass implements View.OnClickListener {

        @Override
        public void onClick(final View v) {
            hideSoftKeyboard(v);
        }
    }

    //</editor-fold>
}
