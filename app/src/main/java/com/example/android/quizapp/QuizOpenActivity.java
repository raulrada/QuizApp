package com.example.android.quizapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class QuizOpenActivity extends AppCompatActivity {

    private String testeeName;
    private String[] providedAnswers;
    private String[] correctAnswers;

    private ArrayList<String> usedCountriesList;

    private int currentScore;
    private int maxScorePossible;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_open);
    }
}
