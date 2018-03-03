package com.example.android.quizapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ReportActivity2 extends AppCompatActivity {

    private String testeeName = "";

    private int currentScore = 0;

    private int maxScorePossible = 0;

    private static final int MIN_SCORE_CONGRATULATIONS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_2);
    }


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
        if (currentScore >= MIN_SCORE_CONGRATULATIONS)
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

}
