package com.example.android.quizapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class ReportActivity extends AppCompatActivity{

    private String testeeName;

    private String[] providedAnswers;
    private String[] correctAnswers;

    private String[] logUsedCapitals;
    private boolean[] logCheckedBoxes;
    private boolean[] logCorrectCheckBoxes;

    private TextView[] countriesTextViews;
    private TextView[] answersTextViews;
    private TextView[] correctAnswersTextViews;

    private ArrayList<String> usedCountriesList;
    private ArrayList<String> usedRegionsList;

    private int currentScore;
    private int maxScorePossible;
    private int correctColor;
    private int wrongColor;

    private TextView nameResultsTextView;
    private TextView nameReportTextView;
    private TextView finalScoreTextView;

    private static final int MIN_SCORE_CONGRATULATIONS = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        correctColor = getResources().getColor(R.color.correctAnswerColor);
        wrongColor = getResources().getColor(R.color.wrongAnswerColor);

        nameResultsTextView = (TextView) findViewById(R.id.name_results_text_view);

        nameReportTextView = (TextView) findViewById(R.id.name_text_view_report);

        finalScoreTextView = (TextView) findViewById(R.id.current_score_view_report);

        testeeName = getIntent().getStringExtra("KEY_TESTEE_NAME");
        nameReportTextView.setText(getString(R.string.player_name_results,testeeName));
        nameResultsTextView.setText(getString(R.string.player_name_score,testeeName));

        currentScore = getIntent().getIntExtra("KEY_TESTEE_SCORE", 0);

        maxScorePossible = getIntent().getIntExtra("KEY_MAX_SCORE",0);

        providedAnswers = getIntent().getStringArrayExtra("KEY_PROVIDED_ANSWERS");

        correctAnswers = getIntent().getStringArrayExtra("KEY_CORRECT_ANSWERS");

        usedCountriesList = getIntent().getStringArrayListExtra("KEY_USED_COUNTRIES");

        usedRegionsList = getIntent().getStringArrayListExtra("USED_REGIONS_LIST");

        logUsedCapitals = getIntent().getStringArrayExtra("KEY_LIST_CAPITALS_MULTIPLE");

        logCheckedBoxes =  getIntent().getBooleanArrayExtra("KEY_CHECKED_BOXES_MULTIPLE");

        logCorrectCheckBoxes = getIntent().getBooleanArrayExtra("KEY_CORRECT_CHECKED_BOXES_MULTIPLE");

        finalScoreTextView.setText(getString(R.string.final_score,currentScore, maxScorePossible));

        countriesTextViews = new TextView[]{findViewById(R.id.text_view_country_0), findViewById(R.id.text_view_country_1),
                findViewById(R.id.text_view_country_2),findViewById(R.id.text_view_country_3),findViewById(R.id.text_view_country_4),
                findViewById(R.id.text_view_country_5),findViewById(R.id.text_view_country_6),findViewById(R.id.text_view_country_7),
                findViewById(R.id.text_view_country_8),findViewById(R.id.text_view_country_9)};

        answersTextViews = new TextView[]{findViewById(R.id.text_view_answer_0), findViewById(R.id.text_view_answer_1),
                findViewById(R.id.text_view_answer_2),findViewById(R.id.text_view_answer_3),findViewById(R.id.text_view_answer_4),
                findViewById(R.id.text_view_answer_5),findViewById(R.id.text_view_answer_6),findViewById(R.id.text_view_answer_7),
                findViewById(R.id.text_view_answer_8),findViewById(R.id.text_view_answer_9)};

        correctAnswersTextViews = new TextView[]{findViewById(R.id.text_view_correct_answer_0), findViewById(R.id.text_view_correct_answer_1),
                findViewById(R.id.text_view_correct_answer_2),findViewById(R.id.text_view_correct_answer_3),findViewById(R.id.text_view_correct_answer_4),
                findViewById(R.id.text_view_correct_answer_5),findViewById(R.id.text_view_correct_answer_6),findViewById(R.id.text_view_correct_answer_7),
                findViewById(R.id.text_view_correct_answer_8),findViewById(R.id.text_view_correct_answer_9)};

        for(int i = 0; i < countriesTextViews.length;i++){
            countriesTextViews[i].setText(usedCountriesList.get(i));
            answersTextViews[i].setText(providedAnswers[i]);
            correctAnswersTextViews[i].setText(correctAnswers[i]);
            if(providedAnswers[i].equals(correctAnswers[i]))
                answersTextViews[i].setTextColor(correctColor);
            else
                answersTextViews[i].setTextColor(wrongColor);
        }

    }



    public void next(View view){
        Intent startNextReport = new Intent(this, ReportActivity2.class);
        startNextReport.putExtra("KEY_TESTEE_NAME", testeeName);
        startNextReport.putExtra("KEY_TESTEE_SCORE", currentScore);
        startNextReport.putExtra("KEY_MAX_SCORE", maxScorePossible);
        startNextReport.putExtra("KEY_PROVIDED_ANSWERS", providedAnswers);
        startNextReport.putExtra("KEY_CORRECT_ANSWERS", correctAnswers);
        startNextReport.putStringArrayListExtra("KEY_USED_COUNTRIES", usedCountriesList);
        startNextReport.putExtra("KEY_LIST_CAPITALS_MULTIPLE", logUsedCapitals);
        startNextReport.putExtra("KEY_CHECKED_BOXES_MULTIPLE", logCheckedBoxes);
        startNextReport.putExtra("KEY_CORRECT_CHECKED_BOXES_MULTIPLE", logCorrectCheckBoxes);
        startNextReport.putStringArrayListExtra("USED_REGIONS_LIST", usedRegionsList);
        startActivity(startNextReport);
    }


    /**
     * Restart the quiz for the current player
     * @param view
     */
    public void restart(View view){
        Intent startQuizIntent = new Intent (this, QuizActivity.class);
        startQuizIntent.putExtra("KEY_TESTEE_NAME", testeeName);
        startActivity(startQuizIntent);
    }

    /**
     * allows player to share quiz result via e-mail
     * @param view
     */
    public void share(View view){
        String mySubject = getString(R.string.player_result_share_subject,testeeName);
        String myMessage;

        //determine message to be included in e-mail: congratulate player or encourage player.
        if (currentScore>=MIN_SCORE_CONGRATULATIONS)
            myMessage = getString(R.string.share_message_congratulate, testeeName, currentScore, maxScorePossible);
        else
            myMessage = getString(R.string.share_message_encourage, testeeName, currentScore, maxScorePossible);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, mySubject);
        intent.putExtra(Intent.EXTRA_TEXT, myMessage);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);}
    }

    /**
     *Disable functionality of back button, to prevent the user from going back to previous
     * screens during test
     */
    @Override
    public void onBackPressed(){

    }

}



