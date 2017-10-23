package com.hizmet.bluewhaleventures.classes;

import android.view.View;

/**
 * Created by Bram Korsten on 10/21/2017.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
