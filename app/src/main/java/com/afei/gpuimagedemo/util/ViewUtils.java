package com.afei.gpuimagedemo.util;

import android.support.v4.view.ViewCompat;
import android.view.View;

public class ViewUtils {

    public static void doOnLayout(View view) {
        if (ViewCompat.isLaidOut(view) && view.isLayoutRequested()) {

        }
    }

}
