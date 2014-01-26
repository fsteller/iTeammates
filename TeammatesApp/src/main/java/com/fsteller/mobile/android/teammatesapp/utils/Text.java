package com.fsteller.mobile.android.teammatesapp.utils;

import android.text.TextWatcher;

/**
 * Created by fhernandezs on 23/01/14.
 */
public class Text {

    //<editor-fold desc="TextWatcher">

    public static abstract class AfterTextChangedWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    }

    //</editor-fold>
}
