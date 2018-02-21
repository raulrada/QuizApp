package com.example.android.quizapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.*;


public class QuizActivity extends AppCompatActivity {


    TextView testeeNameTextView;
    TextView questionTextView;
    TextView questionNumberTextView;
    TextView currentScoreTextView;
    RadioGroup quizRadioGroup;
    RadioButton answer1RadioButton;
    RadioButton answer2RadioButton;
    RadioButton answer3RadioButton;
    RadioButton answer4RadioButton;
    Button submitButton;

    int currentQuestionNumber = 0;
    int currentScore = 0;
    int maxCurrentScore = 0;

    String selectedCountry;
    String selectedAnswer;
    String correctAnswer;
    String testeeName;

    boolean isSubmitEnabled;

    String[] countries;
    String[] capitals;
    String[] answers;
    String[] providedAnswers;
    String[] correctAnswers;

    HashMap<String, String> countriesCapitals;
    ArrayList<String> usedCountriesList;

    private static final String KEY_TESTEE_NAME = "TesteeName";
    private static final String KEY_CURRENT_QUESTION_NUMBER = "currentQuestionNumber";
    private static final String KEY_USED_COUNTRIES = "usedCountries";
    private static final String KEY_SELECTED_COUNTRY = "selectedCountry";
    private static final String KEY_ANSWERS = "answers";
    private static final String KEY_CORRECT_ANSWER = "correctAnswer";
    private static final String KEY_PROVIDED_ANSWERS = "providedAnswers";
    private static final String KEY_CORRECT_ANSWERS = "correctAnswers";
    private static final String KEY_SUBMIT_ENABLED = "submitEnabled";
    private static final String KEY_CURRENT_SCORE = "currentScore";
    private static final String KEY_MAX_CURRENT_SCORE = "maxCurrentScore";


    public static final int DELAY = 2000;
    public static final int NUMBER_OF_QUESTIONS = 10;
    public static final int NUMBER_OF_ANSWERS = 4;

    //Handler setDelay;
    //Runnable startDelay;
    //Intent startReportIntent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //setDelay = new Handler();

        providedAnswers = new String[NUMBER_OF_QUESTIONS];
        correctAnswers = new String[NUMBER_OF_QUESTIONS];

        testeeName = getIntent().getStringExtra("KEY_TESTEE_NAME");
        testeeNameTextView = (TextView) findViewById(R.id.name_text_view_quiz);
        questionNumberTextView = (TextView) findViewById(R.id.question_number_text_view);
        testeeNameTextView.setText(getString(R.string.player_name,testeeName));


        questionTextView = (TextView) findViewById(R.id.question_text_view);

        currentScoreTextView = (TextView) findViewById(R.id.current_score_view_quiz);

        quizRadioGroup = (RadioGroup) findViewById(R.id.radio_group_quiz);
        answer1RadioButton = (RadioButton) findViewById(R.id.answer1_rb);
        answer2RadioButton = (RadioButton) findViewById(R.id.answer2_rb);
        answer3RadioButton = (RadioButton) findViewById(R.id.answer3_rb);
        answer4RadioButton = (RadioButton) findViewById(R.id.answer4_rb);
        submitButton = (Button) findViewById(R.id.submit_button);

        countriesCapitals = getHashMapResource(this, R.xml.countriescapitalsmap);

        countries = countriesCapitals.keySet().toArray(new String[countriesCapitals.keySet().size()]);
        capitals = countriesCapitals.values().toArray(new String[countriesCapitals.values().size()]);
        answers = new String[NUMBER_OF_ANSWERS];

        usedCountriesList = new ArrayList<String>();

        if (savedInstanceState == null) populateQuestion();



        //@Abhishek: how can I change the code in the setOnClickListener to avoid messing up the quiz slide
        //on rotating the screen (the question number is not updated, and neither is the country name, whereas
        // the list of cities is updated)???
        // Why do the onSaveInstanceState and onRestoreInstanceState methods
        //not work properly? What am I doing wrong?

        //delay moving on to the next question / next activity for as long as the toast with the
        //result of the question is being displayed
//        submitButton.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                int selected = quizRadioGroup.getCheckedRadioButtonId();
//                RadioButton b = findViewById(selected);
//                selectedAnswer = (String) b.getText();
//
//                providedAnswers[currentQuestionNumber - 1] = selectedAnswer;
//                correctAnswers[currentQuestionNumber - 1] = correctAnswer;
//
//                if (correctAnswer.equals(selectedAnswer))
//                    Toast.makeText(getApplicationContext(),getText(R.string.correct),Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(getApplicationContext(),getText(R.string.wrong),Toast.LENGTH_SHORT).show();
//
//                startReportIntent = new Intent(v.getContext(), ReportActivity.class);
//                startReportIntent.putExtra("KEY_TESTEE_NAME", testeeName);
//                startReportIntent.putExtra("KEY_TESTEE_SCORE", currentScore);
//                startReportIntent.putExtra("KEY_MAX_SCORE",maxCurrentScore);
//                startReportIntent.putExtra("KEY_PROVIDED_ANSWERS",providedAnswers);
//                startReportIntent.putExtra("KEY_CORRECT_ANSWERS", correctAnswers);
//                startReportIntent.putStringArrayListExtra("KEY_USED_COUNTRIES",usedCountriesList);
//
//                startDelay = new Runnable() {
//                    @Override
//                    public void run() {
//                        if (currentQuestionNumber < NUMBER_OF_QUESTIONS)
//                            populateQuestion();
//                        else {
//
//                            startActivity(startReportIntent);
//                        }
//                    }
//                };
//                setDelay.postDelayed(startDelay, DELAY);
//            }
//        });


        //When an answer is selected, enable the Submit button
        quizRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                submitButton.setEnabled(true);
                isSubmitEnabled = true;
            }
        });

    }

    /**
     *Populates layout with a country capital question
     */
    public void populateQuestion(){

        quizRadioGroup.clearCheck(); //make sure no radio button is selected from previous answer
        submitButton.setEnabled(false); //disable Submit button until an answer is selected
        isSubmitEnabled = false;

        ArrayList<String> usedCapitalsList = new ArrayList<String>();

        currentQuestionNumber+=1;
        questionNumberTextView.setText(getString(R.string.question, String.valueOf(currentQuestionNumber)));

        currentScoreTextView.setText(getString(R.string.current_score,String.valueOf(currentScore), String.valueOf(maxCurrentScore)));

        //select a country name not already used, and then add it to the ArrayList with used country names;
        selectedCountry = selectValue(usedCountriesList, countries);
        usedCountriesList.add(selectedCountry);


        questionTextView.setText(getString(R.string.ask_question,selectedCountry));

        //store the correct answer to the question
        correctAnswer = countriesCapitals.get(selectedCountry);

        //select the possible answers to the question
        usedCapitalsList.add(correctAnswer);

        answers[0] = correctAnswer; //the correct answer has to be among possible answers

        //for the other 3 positions in the possible answers array, determine capital names not already
        //used for the current question. Add such used capitals names to the ArrayList containing
        //capitals names already used for current question
        for(int i=1; i<answers.length; i++){
            answers[i] = selectValue(usedCapitalsList, capitals);
            usedCapitalsList.add(answers[i]);
        }

        //shuffle the order of the possible answers
        answers = shuffleArray(answers);

        answer1RadioButton.setText(answers[0]);
        answer2RadioButton.setText(answers[1]);
        answer3RadioButton.setText(answers[2]);
        answer4RadioButton.setText(answers[3]);
    }

    /**
     * Randomly selects a value from valuesArray which is not already in an ArrayList provided
     * as parameter
     * @param usedValues ArrayList of values already used and which cannot be selected from
     *                   valuesArray
     * @param valuesArray array containing values from among which one shall be randomly selected
     * @return random String element from valuesArray
     */
    public String selectValue(ArrayList<String> usedValues, String[] valuesArray){
        boolean validValue = false;
        Random rand = new Random();
        String returnValue = "";

        while(!validValue){
            int selectedIndex = rand.nextInt(valuesArray.length);
            if (!usedValues.contains(valuesArray[selectedIndex])){
                validValue = true;
                returnValue = valuesArray[selectedIndex];
            }
        }
        return returnValue;
    }

    /**
     * Shuffles elements in provided string array based on the Fisher - Yates shuffle
     * @param values array whose elements shall be shuffled
     * @return array of shuffled elements
     */
    public String[] shuffleArray(String[] values){
        Random rand = new Random();

        for(int i=values.length-1;i>0;i--){
            int index = rand.nextInt(i+1);
            String temp = values[i];
            values[i] = values[index];
            values[index] = temp;
        }
        return values;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        testeeName = savedInstanceState.getString(KEY_TESTEE_NAME);

        currentQuestionNumber = savedInstanceState.getInt(KEY_CURRENT_QUESTION_NUMBER);
        questionNumberTextView.setText(getString(R.string.question, String.valueOf(currentQuestionNumber)));

        usedCountriesList = savedInstanceState.getStringArrayList(KEY_USED_COUNTRIES);

        selectedCountry = savedInstanceState.getString(KEY_SELECTED_COUNTRY);
        questionTextView.setText(getString(R.string.ask_question,selectedCountry));

        answers = savedInstanceState.getStringArray(KEY_ANSWERS);
        answer1RadioButton.setText(answers[0]);
        answer2RadioButton.setText(answers[1]);
        answer3RadioButton.setText(answers[2]);
        answer4RadioButton.setText(answers[3]);

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

        currentScoreTextView.setText(getString(R.string.current_score,String.valueOf(currentScore), String.valueOf(maxCurrentScore)));

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putString(KEY_TESTEE_NAME,testeeName);
        savedInstanceState.putInt(KEY_CURRENT_QUESTION_NUMBER, currentQuestionNumber);
        savedInstanceState.putStringArrayList(KEY_USED_COUNTRIES, usedCountriesList);
        savedInstanceState.putString(KEY_SELECTED_COUNTRY, selectedCountry);
        savedInstanceState.putStringArray(KEY_ANSWERS, answers);
        savedInstanceState.putString(KEY_CORRECT_ANSWER, correctAnswer);
        savedInstanceState.putStringArray(KEY_PROVIDED_ANSWERS, providedAnswers);
        savedInstanceState.putStringArray(KEY_CORRECT_ANSWERS, correctAnswers);
        savedInstanceState.putBoolean(KEY_SUBMIT_ENABLED, isSubmitEnabled);
        savedInstanceState.putInt(KEY_CURRENT_SCORE, currentScore);
        savedInstanceState.putInt(KEY_MAX_CURRENT_SCORE, maxCurrentScore);

        super.onSaveInstanceState(savedInstanceState);
    }


    public void submitAnswer(View view){

        int selected = quizRadioGroup.getCheckedRadioButtonId();
        RadioButton b = findViewById(selected);
        selectedAnswer = (String) b.getText();

        providedAnswers[currentQuestionNumber - 1] = selectedAnswer;
        correctAnswers[currentQuestionNumber - 1] = correctAnswer;

        maxCurrentScore += 1;

        if (correctAnswer.equals(selectedAnswer)){
            Toast.makeText(getApplicationContext(),getText(R.string.correct),Toast.LENGTH_SHORT).show();
            currentScore += 1;}
        else
            Toast.makeText(getApplicationContext(),getText(R.string.wrong),Toast.LENGTH_SHORT).show();

        currentScoreTextView.setText(getString(R.string.current_score,String.valueOf(currentScore), String.valueOf(maxCurrentScore)));

        if (currentQuestionNumber < NUMBER_OF_QUESTIONS)
            populateQuestion();
        else {
            Intent startReportIntent = new Intent(this, ReportActivity.class);
            startReportIntent.putExtra("KEY_TESTEE_NAME", testeeName);
            startReportIntent.putExtra("KEY_TESTEE_SCORE", currentScore);
            startReportIntent.putExtra("KEY_MAX_SCORE",maxCurrentScore);
            startReportIntent.putExtra("KEY_PROVIDED_ANSWERS",providedAnswers);
            startReportIntent.putExtra("KEY_CORRECT_ANSWERS", correctAnswers);
            startReportIntent.putStringArrayListExtra("KEY_USED_COUNTRIES",usedCountriesList);
            startActivity(startReportIntent);
        }
    }


    /**
     * Method used to populate a HashMap from a res/xml file
     * source code from: https://gist.github.com/codebycliff/11198553
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
                }
                else if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("map")) {
                        boolean isLinked = parser.getAttributeBooleanValue(null, "linked", false);

                        map = isLinked
                                ? new LinkedHashMap<String, String>()
                                : new HashMap<String, String>();
                    }
                    else if (parser.getName().equals("entry")) {
                        key = parser.getAttributeValue(null, "key");

                        if (null == key) {
                            parser.close();
                            return null;
                        }
                    }
                }
                else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("entry")) {
                        map.put(key, value);
                        key = null;
                        value = null;
                    }
                }
                else if (eventType == XmlPullParser.TEXT) {
                    if (null != key) {
                        value = parser.getText();
                    }
                }
                eventType = parser.next();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return map;
    }


    /**
     *Disable functionality of back button, to prevent the user from going back to previous
     * screens during test
     */
    @Override
    public void onBackPressed(){

    }

}
