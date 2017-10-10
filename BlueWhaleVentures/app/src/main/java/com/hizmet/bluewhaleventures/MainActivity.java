package com.hizmet.bluewhaleventures;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ImageView imageViewChevron_Left;

    Button buttonStartNow;
    TextView textViewTitle;
    TextView textViewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Imageview back to login activity
        imageViewChevron_Left = (ImageView) this.findViewById(R.id.imageView_chevron_left_main);
        imageViewChevron_Left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // Button start now
        buttonStartNow = (Button) this.findViewById(R.id.button_start_now_main);
        buttonStartNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExperimentsActivity.class);
                startActivity(intent);
            }
        });

        setCustomFontsOfElements();
    }

    private void setCustomFontsOfElements() {
        textViewTitle = (TextView) findViewById(R.id.textView_title_main);
        textViewTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Intro.ttf"));
        textViewInfo = (TextView) findViewById(R.id.textView_info_main);
        textViewInfo.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Helvetica-Regular.ttf"));
        buttonStartNow = (Button) findViewById(R.id.button_start_now_main);
        buttonStartNow.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Helvetica-Regular.ttf"));
    }
}
