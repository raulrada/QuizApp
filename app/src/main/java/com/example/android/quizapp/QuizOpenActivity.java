package com.example.android.quizapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

public class QuizOpenActivity extends AppCompatActivity {

    private static final int NUMBER_OPEN_QUESTIONS = 2;
    private static final String KEY_TESTEE_NAME = "TesteeName";
    private static final String KEY_CURRENT_QUESTION_NUMBER = "currentQuestionNumber";
    private static final String KEY_CURRENT_OPEN_QUESTION_NUMBER = "currentOpenQuestionNumber";
    private static final String KEY_USED_COUNTRIES = "usedCountries";
    private static final String KEY_SELECTED_COUNTRY = "selectedCountry";
    private static final String KEY_CORRECT_ANSWER = "correctAnswer";
    private static final String KEY_PROVIDED_ANSWERS = "providedAnswers";
    private static final String KEY_CORRECT_ANSWERS = "correctAnswers";
    private static final String KEY_SUBMIT_ENABLED = "submitEnabled";
    private static final String KEY_CURRENT_SCORE = "currentScore";
    private static final String KEY_MAX_CURRENT_SCORE = "maxCurrentScore";
    private String testeeName;
    private String[] providedAnswers;
    private String[] correctAnswers;
    private String[] countries;
    private ArrayList<String> usedCountriesList;
    private HashMap<String, String> countriesCapitals;
    private int currentScore;
    private int maxCurrentScore;
    private boolean isSubmitEnabled;
    private int currentQuestionNumber;
    private int currentOpenQuestionsNumber = 0;
    private String selectedCountry;
    private String selectedAnswer;
    private String correctAnswer;
    private TextView questionNumberTextView;
    private TextView questionTextView;
    private TextView testeeNameTextView;
    private TextView currentScoreTextView;
    private EditText answerEditText;
    private Button submitButton;

    /**
     * Method used to populate a HashMap from a res/xml file
     * source code from: https://gist.github.com/codebycliff/11198553
     *
     * @param c
     * @param hashMapResId id of the xml file containing the map
     * @return populated HashMap
     */
    public static HashMap<String, String> getHashMapResource(Context c, int hashMapResId) {
        HashMap<String, String> map = null;
        XmlResourceParser parser = c.getResources().getXml(hashMapResId);

        String key = null, value = null;

        try {
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d("utils", "Start document");
                } else if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("map")) {
                        boolean isLinked = parser.getAttributeBooleanValue(null, "linked", false);

                        map = isLinked
                                ? new LinkedHashMap<String, String>()
                                : new HashMap<String, String>();
                    } else if (parser.getName().equals("entry")) {
                        key = parser.getAttributeValue(null, "key");

                        if (null == key) {
                            parser.close();
                            return null;
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("entry")) {
                        map.put(key, value);
                        key = null;
                        value = null;
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    if (null != key) {
                        value = parser.getText();
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_open);

        testeeName = getIntent().getStringExtra("KEY_TESTEE_NAME");
        testeeNameTextView = (TextView) findViewById(R.id.name_text_view_quiz_open);
        testeeNameTextView.setText(getString(R.string.player_name, testeeName));

        currentScore = getIntent().getIntExtra("KEY_TESTEE_SCORE", 0);
        maxCurrentScore = getIntent().getIntExtra("KEY_MAX_SCORE", 0);
        currentScoreTextView = (TextView) findViewById(R.id.current_score_view_quiz_open);
        currentQuestionNumber = maxCurrentScore;

        providedAnswers = getIntent().getStringArrayExtra("KEY_PROVIDED_ANSWERS");

        correctAnswers = getIntent().getStringArrayExtra("KEY_CORRECT_ANSWERS");

        usedCountriesList = getIntent().getStringArrayListExtra("KEY_USED_COUNTRIES");

        questionNumberTextView = (TextView) findViewById(R.id.question_number_text_view_open);

        questionTextView = (TextView) findViewById(R.id.question_text_view_open);

        answerEditText = (EditText) findViewById(R.id.answer_edit_text_open);

        submitButton = (Button) findViewById(R.id.submit_button_open);

        countriesCapitals = getHashMapResource(this, R.xml.countriescapitalsmap);

        countries = countriesCapitals.keySet().toArray(new String[countriesCapitals.keySet().size()]);

        if (savedInstanceState == null) populateQuestion();

        answerEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            /**
             * Force introduction of an answer in the EditText before enabling the Submit button
             * @param s
             * @param start
             * @param before
             * @param count
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selectedAnswer = answerEditText.getText().toString();
                if (!TextUtils.isEmpty(selectedAnswer)) {
                    submitButton.setEnabled(true);
                    isSubmitEnabled = true;
                } else {
                    submitButton.setEnabled(false);
                    isSubmitEnabled = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                providedAnswers[currentQuestionNumber - 1] = selectedAnswer;
                correctAnswers[currentQuestionNumber - 1] = correctAnswer;

                maxCurrentScore += 1;

                if (correctAnswer.equalsIgnoreCase(selectedAnswer)) {
                    Toast.makeText(getApplicationContext(), getText(R.string.correct), Toast.LENGTH_SHORT).show();
                    currentScore += 1;
                } else
                    Toast.makeText(getApplicationContext(), getText(R.string.wrong), Toast.LENGTH_SHORT).show();

                currentScoreTextView.setText(getString(R.string.current_score, String.valueOf(currentScore), String.valueOf(maxCurrentScore)));

                if (currentOpenQuestionsNumber < NUMBER_OPEN_QUESTIONS)
                    populateQuestion();
                else {
                    Intent startQuizMultipleActivity = new Intent(QuizOpenActivity.this, QuizMultipleActivity.class);
                    startQuizMultipleActivity.putExtra("KEY_TESTEE_NAME", testeeName);
                    startQuizMultipleActivity.putExtra("KEY_TESTEE_SCORE", currentScore);
                    startQuizMultipleActivity.putExtra("KEY_MAX_SCORE", maxCurrentScore);
                    startQuizMultipleActivity.putExtra("KEY_PROVIDED_ANSWERS", providedAnswers);
                    startQuizMultipleActivity.putExtra("KEY_CORRECT_ANSWERS", correctAnswers);
                    startQuizMultipleActivity.putStringArrayListExtra("KEY_USED_COUNTRIES", usedCountriesList);
                    startActivity(startQuizMultipleActivity);
                }

            }

        });

    }

    /**
     * Ask a question to the player and provide potential answers
     */
    public void populateQuestion() {

        submitButton.setEnabled(false); //disable Submit button until an answer is selected
        isSubmitEnabled = false;
        answerEditText.getText().clear();

        currentQuestionNumber += 1;
        currentOpenQuestionsNumber += 1;
        questionNumberTextView.setText(getString(R.string.question, String.valueOf(currentQuestionNumber)));

        currentScoreTextView.setText(getString(R.string.current_score, String.valueOf(currentScore), String.valueOf(maxCurrentScore)));

        //select a country name not already used, and then add it to the ArrayList with used country names;
        selectedCountry = selectValue(usedCountriesList, countries);
        usedCountriesList.add(selectedCountry);

        questionTextView.setText(getString(R.string.ask_question, selectedCountry));

        //store the correct answer to the question
        correctAnswer = countriesCapitals.get(selectedCountry);

    }

    /**
     * Randomly selects a value from valuesArray which is not already in an ArrayList provided
     * as parameter
     *
     * @param usedValues  ArrayList of values already used and which cannot be selected from
     *                    valuesArray
     * @param valuesArray array containing values from among which one shall be randomly selected
     * @return random String element from valuesArray
     */
    public String selectValue(ArrayList<String> usedValues, String[] valuesArray) {
        boolean validValue = false;
        Random rand = new Random();
        String returnValue = "";

        while (!validValue) {
            int selectedIndex = rand.nextInt(valuesArray.length);
            if (!usedValues.contains(valuesArray[selectedIndex])) {
                validValue = true;
                returnValue = valuesArray[selectedIndex];
            }
        }
        return returnValue;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        testeeName = savedInstanceState.getString(KEY_TESTEE_NAME);

        currentQuestionNumber = savedInstanceState.getInt(KEY_CURRENT_QUESTION_NUMBER);
        questionNumberTextView.setText(getString(R.string.question, String.valueOf(currentQuestionNumber)));

        currentOpenQuestionsNumber = savedInstanceState.getInt(KEY_CURRENT_OPEN_QUESTION_NUMBER);

        usedCountriesList = savedInstanceState.getStringArrayList(KEY_USED_COUNTRIES);

        selectedCountry = savedInstanceState.getString(KEY_SELECTED_COUNTRY);
        questionTextView.setText(getString(R.string.ask_question, selectedCountry));

        correctAnswer = savedInstanceState.getString(KEY_CORRECT_ANSWER);

        providedAnswers = savedInstanceState.getStringArray(KEY_PROVIDED_ANSWERS);
        correctAnswers = savedInstanceState.getStringArray(KEY_CORRECT_ANSWERS);

        isSubmitEnabled = savedInstanceState.getBoolean(KEY_SUBMIT_ENABLED);

        //restore the status of the Submit button
        if (isSubmitEnabled)
            submitButton.setEnabled(true);
        else
            submitButton.setEnabled(false);

        currentScore = savedInstanceState.getInt(KEY_CURRENT_SCORE);
        maxCurrentScore = savedInstanceState.getInt(KEY_MAX_CURRENT_SCORE);

        currentScoreTextView.setText(getString(R.string.current_score, String.valueOf(currentScore), String.valueOf(maxCurrentScore)));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString(KEY_TESTEE_NAME, testeeName);
        savedInstanceState.putInt(KEY_CURRENT_QUESTION_NUMBER, currentQuestionNumber);
        savedInstanceState.putInt(KEY_CURRENT_OPEN_QUESTION_NUMBER, currentOpenQuestionsNumber);
        savedInstanceState.putStringArrayList(KEY_USED_COUNTRIES, usedCountriesList);
        savedInstanceState.putString(KEY_SELECTED_COUNTRY, selectedCountry);
        savedInstanceState.putString(KEY_CORRECT_ANSWER, correctAnswer);
        savedInstanceState.putStringArray(KEY_PROVIDED_ANSWERS, providedAnswers);
        savedInstanceState.putStringArray(KEY_CORRECT_ANSWERS, correctAnswers);
        savedInstanceState.putBoolean(KEY_SUBMIT_ENABLED, isSubmitEnabled);
        savedInstanceState.putInt(KEY_CURRENT_SCORE, currentScore);
        savedInstanceState.putInt(KEY_MAX_CURRENT_SCORE, maxCurrentScore);

        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Disable functionality of back button, to prevent the user from going back to previous
     * screens during test
     */
    @Override
    public void onBackPressed() {

    }


}
