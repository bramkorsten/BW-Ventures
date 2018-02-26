package com.hizmet.bluewhaleventures;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Map;

public class QuestionActivity extends AppCompatActivity {
    private Map questionData;
    private String questionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
    }

    public String getQuestionIdFromParent() {
        return questionId;
    }
}
