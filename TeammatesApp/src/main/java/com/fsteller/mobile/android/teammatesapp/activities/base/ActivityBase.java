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
 * Created by fsteller on 12/30/13.
 */
public abstract class ActivityBase extends Activity {

    //<editor-fold desc="Constants">

    protected static final int SOFT_INPUT_MODE_VISIBLE = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
    protected static final int SOFT_INPUT_MODE_HIDDEN = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;

    //</editor-fold>
    //<editor-fold desc="Variables">

    private InputMethodManager imm = null;
    protected final HideSoftInputClass mHideSoftInputClass = new HideSoftInputClass();
    protected TeammatesApp app;

    //</editor-fold>

    //<editor-fold desc="Public">

    public void setIsKeyBoardEnabled(final boolean enable) {
        getWindow().setSoftInputMode(enable ? SOFT_INPUT_MODE_VISIBLE : SOFT_INPUT_MODE_HIDDEN);
    }

    //</editor-fold>
    //<editor-fold desc="Overridden">

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG)
            VersionTools.enableStrictMode();

        this.app = (TeammatesApp) getApplication();
        this.imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        this.setIsKeyBoardEnabled(true);
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
        this.imm = null;
    }

    //</editor-fold>
    //<editor-fold desc="Protected">

    protected static boolean isNullOrEmpty(String txt) {
        return txt == null || txt.isEmpty();
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
