package com.hizmet.bluewhaleventures;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    TextView textViewTitle;
    TextView textViewInfo;
    Button buttonStartNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTitle = (TextView) findViewById(R.id.textView_title_main);
        textViewTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Intro.ttf"));

        textViewInfo = (TextView) findViewById(R.id.textView_info_main);
        textViewInfo.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Helvetica-Regular.ttf"));

        buttonStartNow = (Button) findViewById(R.id.button_start_now_main);
        buttonStartNow.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Helvetica-Regular.ttf"));

    }
}
