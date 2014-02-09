package com.fsteller.mobile.android.teammatesapp.utils;

import android.text.TextWatcher;

import java.util.ArrayList;

/**
 * Created by fhernandezs on 23/01/14.
 */
public class Text {

    public static String[] mergeArrays(final String[]... arrayParams) {
        final ArrayList<String> result = new ArrayList<String>();

        for (String[] params : arrayParams)
            for (String s : params)
                if (!result.contains(s))
                    result.add(s);
        return result.toArray(new String[result.size()]);
    }

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
