package com.example.android.quizapp;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

public class QuizOpenActivity extends AppCompatActivity {

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

    private String selectedCountry;
    private String selectedAnswer;
    private String correctAnswer;

    private TextView questionNumberTextView;
    private TextView questionTextView;
    private TextView testeeNameTextView;
    private TextView currentScoreTextView;
    private EditText answerEditText;
    private Button submitButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_open);

        testeeName = getIntent().getStringExtra("KEY_TESTEE_NAME");
        testeeNameTextView = (TextView) findViewById(R.id.name_text_view_quiz_open);
        testeeNameTextView.setText(getString(R.string.player_name,testeeName));

        currentScore = getIntent().getIntExtra("KEY_TESTEE_SCORE", 0);
        maxCurrentScore = getIntent().getIntExtra("KEY_MAX_SCORE",0);
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

    }


    /**
     *Populates layout with a country capital question
     */
    public void populateQuestion(){

        submitButton.setEnabled(false); //disable Submit button until an answer is selected
        isSubmitEnabled = false;

        currentQuestionNumber+=1;
        questionNumberTextView.setText(getString(R.string.question, String.valueOf(currentQuestionNumber)));

        currentScoreTextView.setText(getString(R.string.current_score,String.valueOf(currentScore), String.valueOf(maxCurrentScore)));

        //select a country name not already used, and then add it to the ArrayList with used country names;
        selectedCountry = selectValue(usedCountriesList, countries);
        usedCountriesList.add(selectedCountry);

        questionTextView.setText(getString(R.string.ask_question,selectedCountry));

        //store the correct answer to the question
        correctAnswer = countriesCapitals.get(selectedCountry);

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
