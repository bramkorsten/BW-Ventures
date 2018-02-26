package com.hizmet.bluewhaleventures;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView imageViewChevron_Left;

    private Button buttonStartNow;

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
                Intent intent = new Intent(v.getContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
