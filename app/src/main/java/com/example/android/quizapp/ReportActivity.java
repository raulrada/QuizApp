package com.example.android.quizapp;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class ReportActivity extends AppCompatActivity{

    String testeeName;

    String[] providedAnswers;
    String[] correctAnswers;

    int currentScore;
    int maxScorePossible;

    TextView nameResultsTextView;
    TextView questionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        nameResultsTextView = (TextView) findViewById(R.id.name_results_text_view);
        testeeName = getIntent().getStringExtra("KEY_TESTEE_NAME");
        nameResultsTextView.setText(getString(R.string.player_name_results,testeeName));

        questionTextView = (TextView) findViewById(R.id.question_text_view_results);


        currentScore = getIntent().getIntExtra("KEY_TESTEE_SCORE", 0);

        maxScorePossible = getIntent().getIntExtra("KEY_MAX_SCORE",0);


        providedAnswers = getIntent().getStringArrayExtra("KEY_PROVIDED_ANSWERS");


        correctAnswers = getIntent().getStringArrayExtra("KEY_CORRECT_ANSWERS");



    }

    /**
     *Disable functionality of back button, to prevent the user from going back to previous
     * screens during test
     */
    @Override
    public void onBackPressed(){

    }

}



