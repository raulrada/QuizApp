package com.example.android.quizapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReportActivity2 extends AppCompatActivity {

    private static final int MIN_SCORE_CONGRATULATIONS = 9;
    private String testeeName;
    private String[] providedAnswers;
    private String[] correctAnswers;
    private String[] logUsedCapitals;
    private boolean[] logCheckedBoxes;
    private boolean[] logCorrectCheckBoxes;
    private ArrayList<String> usedCountriesList;
    private ArrayList<String> usedRegionsList;
    private int currentScore;
    private int maxScorePossible;
    private int wrongColor;
    private int correctColor;
    private TextView nameResultsTextView;
    private TextView nameReportTextView;
    private TextView finalScoreTextView;
    private TextView[] regionsTextViews;
    private TextView[] capitalsTextViews;
    private CheckBox[] playerCheckBoxes;
    private CheckBox[] correctCheckBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_2);
        setup();
    }

    public void setup() {
        correctColor = getResources().getColor(R.color.correctAnswerColor);
        wrongColor = getResources().getColor(R.color.wrongAnswerColor);

        nameResultsTextView = (TextView) findViewById(R.id.name_results2_text_view);

        nameReportTextView = (TextView) findViewById(R.id.name_text_view_report_2);

        finalScoreTextView = (TextView) findViewById(R.id.current_score_view_report_2);

        testeeName = getIntent().getStringExtra("KEY_TESTEE_NAME");
        nameReportTextView.setText(getString(R.string.player_name_results, testeeName));
        nameResultsTextView.setText(getString(R.string.player_name_score, testeeName));

        currentScore = getIntent().getIntExtra("KEY_TESTEE_SCORE", 0);

        maxScorePossible = getIntent().getIntExtra("KEY_MAX_SCORE", 0);

        providedAnswers = getIntent().getStringArrayExtra("KEY_PROVIDED_ANSWERS");

        correctAnswers = getIntent().getStringArrayExtra("KEY_CORRECT_ANSWERS");

        usedCountriesList = getIntent().getStringArrayListExtra("KEY_USED_COUNTRIES");

        usedRegionsList = getIntent().getStringArrayListExtra("USED_REGIONS_LIST");

        logUsedCapitals = getIntent().getStringArrayExtra("KEY_LIST_CAPITALS_MULTIPLE");

        logCheckedBoxes = getIntent().getBooleanArrayExtra("KEY_CHECKED_BOXES_MULTIPLE");

        logCorrectCheckBoxes = getIntent().getBooleanArrayExtra("KEY_CORRECT_CHECKED_BOXES_MULTIPLE");

        finalScoreTextView.setText(getString(R.string.final_score, currentScore, maxScorePossible));

        regionsTextViews = new TextView[]{findViewById(R.id.text_view_region_0),
                findViewById(R.id.text_view_region_1)};

        for (int i = 0; i < regionsTextViews.length; i++)
            regionsTextViews[i].setText(usedRegionsList.get(i));

        capitalsTextViews = new TextView[]{findViewById(R.id.text_view_capital_0), findViewById(R.id.text_view_capital_1),
                findViewById(R.id.text_view_capital_2), findViewById(R.id.text_view_capital_3), findViewById(R.id.text_view_capital_4),
                findViewById(R.id.text_view_capital_5), findViewById(R.id.text_view_capital_6), findViewById(R.id.text_view_capital_7),
                findViewById(R.id.text_view_capital_8), findViewById(R.id.text_view_capital_9), findViewById(R.id.text_view_capital_10),
                findViewById(R.id.text_view_capital_11)};

        playerCheckBoxes = new CheckBox[]{findViewById(R.id.checbox_player_0), findViewById(R.id.checbox_player_1),
                findViewById(R.id.checbox_player_2), findViewById(R.id.checbox_player_3), findViewById(R.id.checbox_player_4),
                findViewById(R.id.checbox_player_5), findViewById(R.id.checbox_player_6), findViewById(R.id.checbox_player_7),
                findViewById(R.id.checbox_player_8), findViewById(R.id.checbox_player_9), findViewById(R.id.checbox_player_10),
                findViewById(R.id.checbox_player_11)};

        correctCheckBoxes = new CheckBox[]{findViewById(R.id.checkbox_correct_0), findViewById(R.id.checkbox_correct_1),
                findViewById(R.id.checkbox_correct_2), findViewById(R.id.checkbox_correct_3), findViewById(R.id.checkbox_correct_4),
                findViewById(R.id.checkbox_correct_5), findViewById(R.id.checkbox_correct_6), findViewById(R.id.checkbox_correct_7),
                findViewById(R.id.checkbox_correct_8), findViewById(R.id.checkbox_correct_9), findViewById(R.id.checkbox_correct_10),
                findViewById(R.id.checkbox_correct_11)};

        for (int i = 0; i < playerCheckBoxes.length; i++) {
            playerCheckBoxes[i].setChecked(logCheckedBoxes[i]);
            playerCheckBoxes[i].setEnabled(false); //checkboxes status should not be changed by user
            correctCheckBoxes[i].setChecked(logCorrectCheckBoxes[i]);
            correctCheckBoxes[i].setEnabled(false);//checkboxes status should not be changed by user
            capitalsTextViews[i].setText(logUsedCapitals[i]);
            if ((logCheckedBoxes[i] && logCorrectCheckBoxes[i]) || (!logCheckedBoxes[i] && !logCorrectCheckBoxes[i]))
                capitalsTextViews[i].setTextColor(correctColor);
            else
                capitalsTextViews[i].setTextColor(wrongColor);
        }
    }

    /**
     * move to previous part of the report
     *
     * @param view
     */
    public void back(View view) {
        Intent startPreviousReport = new Intent(this, ReportActivity.class);
        startPreviousReport.putExtra("KEY_TESTEE_NAME", testeeName);
        startPreviousReport.putExtra("KEY_TESTEE_SCORE", currentScore);
        startPreviousReport.putExtra("KEY_MAX_SCORE", maxScorePossible);
        startPreviousReport.putExtra("KEY_PROVIDED_ANSWERS", providedAnswers);
        startPreviousReport.putExtra("KEY_CORRECT_ANSWERS", correctAnswers);
        startPreviousReport.putStringArrayListExtra("KEY_USED_COUNTRIES", usedCountriesList);
        startPreviousReport.putExtra("KEY_LIST_CAPITALS_MULTIPLE", logUsedCapitals);
        startPreviousReport.putExtra("KEY_CHECKED_BOXES_MULTIPLE", logCheckedBoxes);
        startPreviousReport.putExtra("KEY_CORRECT_CHECKED_BOXES_MULTIPLE", logCorrectCheckBoxes);
        startPreviousReport.putStringArrayListExtra("USED_REGIONS_LIST", usedRegionsList);
        startActivity(startPreviousReport);
    }

    /**
     * Restart the quiz for the current player
     *
     * @param view
     */
    public void restart(View view) {
        Intent startQuizIntent = new Intent(this, QuizActivity.class);
        startQuizIntent.putExtra("KEY_TESTEE_NAME", testeeName);
        startActivity(startQuizIntent);
    }

    /**
     * allows player to share quiz result via e-mail
     *
     * @param view
     */
    public void share(View view) {
        String mySubject = getString(R.string.player_result_share_subject, testeeName);
        String myMessage;

        //determine message to be included in e-mail: congratulate player or encourage player.
        if (currentScore >= MIN_SCORE_CONGRATULATIONS)
            myMessage = getString(R.string.share_message_congratulate, testeeName, currentScore, maxScorePossible);
        else
            myMessage = getString(R.string.share_message_encourage, testeeName, currentScore, maxScorePossible);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, mySubject);
        intent.putExtra(Intent.EXTRA_TEXT, myMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * show player's final score in Toast
     * @param view
     */
    public void grade(View view){
        Toast.makeText(getApplicationContext(), getString(R.string.player_result_toast, testeeName, currentScore, maxScorePossible), Toast.LENGTH_SHORT).show();
    }

    /**
     * Disable functionality of back button, to prevent the user from going back to previous
     * screens during test
     */
    @Override
    public void onBackPressed() {
        Log.v("onBackPressed", "Phone back button disabled on purpose, to prevent user from" +
                " going back to previous questions / screens.");
    }
}
