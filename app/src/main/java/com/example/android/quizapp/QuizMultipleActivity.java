package com.example.android.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class QuizMultipleActivity extends AppCompatActivity {

    private static final String[] REGIONS = {"Europe", "Asia", "the Americas", "Africa", "Oceania"};
    private static final int NUMBER_MULTIPLE_QUESTIONS = 2;
    private static final int NUMBER_OF_ANSWERS = 6;
    private static final String KEY_USED_REGIONS = "usedRegions";
    private static final String KEY_SELECTED_REGION = "selectedRegion";
    private static final String KEY_ANSWERS = "answers";
    private static final String KEY_CURRENT_MULTIPLE_QUESTION_NUMBER = "currentOpenQuestionNumber";
    private static final String KEY_TESTEE_NAME = "TesteeName";
    private static final String KEY_CURRENT_QUESTION_NUMBER = "currentQuestionNumber";
    private static final String KEY_PROVIDED_ANSWERS = "providedAnswers";
    private static final String KEY_CORRECT_ANSWERS = "correctAnswers";
    private static final String KEY_USED_COUNTRIES = "usedCountries";
    private static final String KEY_CURRENT_SCORE = "currentScore";
    private static final String KEY_MAX_CURRENT_SCORE = "maxCurrentScore";
    private static final String KEY_LOG_USED_CAPITALS = "logUsedCapitals";
    private static final String KEY_LOG_CHECKED_CHECKBOXES = "logCheckedCheckBoxes";
    private static final String KEY_LOG_CORRECT_CHECKED_CHECKBOXES = "logCorrectCheckedCheckBoxes";
    private CheckBox[] checkBoxes;
    private ArrayList<String> europeanCapitals;
    private ArrayList<String> asianCapitals;
    private ArrayList<String> americasCapitals;
    private ArrayList<String> africanCapitals;
    private ArrayList<String> oceaniaCapitals;
    private ArrayList<String> allCapitals;
    private int currentMultipleQuestionsNumber = 0;
    private String selectedRegion;
    private ArrayList<String> usedRegionsList;
    private HashMap<String, ArrayList<String>> regionsMap;
    private String[] answers;
    private String[] logUsedCapitals; //contains all potential answers for all questions
    private boolean[] logCheckedBoxes; //contains status of all checkboxes for all questions
    private boolean[] logCorrectCheckBoxes; //contains info whether the checkboxes should have been checked for all questions
    private TextView questionNumberTextView;
    private TextView questionTextView;
    private TextView testeeNameTextView;
    private TextView currentScoreTextView;
    private Button submitButton;
    private String testeeName;
    private String[] providedAnswers;
    private String[] correctAnswers;
    private int currentScore;
    private int maxCurrentScore;
    private int currentQuestionNumber;
    private ArrayList<String> usedCountriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_multiple);

        setup();
        if (savedInstanceState == null) populateQuestion();

        submitButton.setOnClickListener(new View.OnClickListener() {
            /**
             * listener for player's click action of the Submit button
             * @param v
             */
            @Override
            public void onClick(View v) {

                maxCurrentScore++;
                boolean isCorrect = true;

                //get the region where the presence of the capitals should be checked
                ArrayList<String> selectedRegionList = regionsMap.get(selectedRegion);

                //For each checkbox, if it is checked and the related capital is not in the selected region,
                //or if it is not checked, but the related capital is in teh selected region, a wrong answer is provided.
                // Otherwise, the answer is correct.

                for (int i = 0; i < checkBoxes.length; i++) {

                    logUsedCapitals[(currentMultipleQuestionsNumber - 1) * NUMBER_OF_ANSWERS + i] = checkBoxes[i].getText().toString();
                    logCheckedBoxes[(currentMultipleQuestionsNumber - 1) * NUMBER_OF_ANSWERS + i] = checkBoxes[i].isChecked();
                    logCorrectCheckBoxes[(currentMultipleQuestionsNumber - 1) * NUMBER_OF_ANSWERS + i] =
                            selectedRegionList.contains(checkBoxes[i].getText().toString());

                    if ((checkBoxes[i].isChecked() & (!selectedRegionList.contains(checkBoxes[i].getText().toString()))) ||
                            (!checkBoxes[i].isChecked() & selectedRegionList.contains(checkBoxes[i].getText().toString())))
                        isCorrect = false;
                }

                if (isCorrect) {
                    Toast.makeText(getApplicationContext(), getText(R.string.correct), Toast.LENGTH_SHORT).show();
                    currentScore++;
                } else
                    Toast.makeText(getApplicationContext(), getText(R.string.wrong), Toast.LENGTH_SHORT).show();

                currentScoreTextView.setText(getString(R.string.current_score, String.valueOf(currentScore), String.valueOf(maxCurrentScore)));


                if (currentMultipleQuestionsNumber < NUMBER_MULTIPLE_QUESTIONS)
                    populateQuestion();
                else {
                    Intent startReportActivity = new Intent(QuizMultipleActivity.this, ReportActivity.class);
                    startReportActivity.putExtra("KEY_TESTEE_NAME", testeeName);
                    startReportActivity.putExtra("KEY_TESTEE_SCORE", currentScore);
                    startReportActivity.putExtra("KEY_MAX_SCORE", maxCurrentScore);
                    startReportActivity.putExtra("KEY_PROVIDED_ANSWERS", providedAnswers);
                    startReportActivity.putExtra("KEY_CORRECT_ANSWERS", correctAnswers);
                    startReportActivity.putStringArrayListExtra("KEY_USED_COUNTRIES", usedCountriesList);
                    startReportActivity.putExtra("KEY_LIST_CAPITALS_MULTIPLE", logUsedCapitals);
                    startReportActivity.putExtra("KEY_CHECKED_BOXES_MULTIPLE", logCheckedBoxes);
                    startReportActivity.putExtra("KEY_CORRECT_CHECKED_BOXES_MULTIPLE", logCorrectCheckBoxes);
                    startReportActivity.putStringArrayListExtra("USED_REGIONS_LIST", usedRegionsList);
                    startActivity(startReportActivity);

                }
            }
        });
    }

    /**
     * initialize various variables used throughout the activity
     */
    public void setup() {
        checkBoxes = new CheckBox[]{findViewById(R.id.checkbox1),
                findViewById(R.id.checkbox2),
                findViewById(R.id.checkbox3),
                findViewById(R.id.checkbox4),
                findViewById(R.id.checkbox5),
                findViewById(R.id.checkbox6)};

        europeanCapitals = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.european_capitals)));
        asianCapitals = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.asian_capitals)));
        africanCapitals = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.african_capitals)));
        americasCapitals = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.americas_capitals)));
        oceaniaCapitals = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.oceania_capitals)));

        allCapitals = new ArrayList<String>();
        allCapitals.addAll(europeanCapitals);
        allCapitals.addAll(asianCapitals);
        allCapitals.addAll(africanCapitals);
        allCapitals.addAll(americasCapitals);
        allCapitals.addAll(oceaniaCapitals);

        questionTextView = (TextView) findViewById(R.id.question_text_view_multiple);
        questionNumberTextView = (TextView) findViewById(R.id.question_number_text_view_multiple);

        testeeName = getIntent().getStringExtra("KEY_TESTEE_NAME");
        testeeNameTextView = (TextView) findViewById(R.id.name_text_view_quiz_multiple);
        testeeNameTextView.setText(getString(R.string.player_name, testeeName));

        currentScore = getIntent().getIntExtra("KEY_TESTEE_SCORE", 0);
        maxCurrentScore = getIntent().getIntExtra("KEY_MAX_SCORE", 0);
        currentScoreTextView = (TextView) findViewById(R.id.current_score_view_quiz_multiple);

        providedAnswers = getIntent().getStringArrayExtra("KEY_PROVIDED_ANSWERS");

        correctAnswers = getIntent().getStringArrayExtra("KEY_CORRECT_ANSWERS");

        usedCountriesList = getIntent().getStringArrayListExtra("KEY_USED_COUNTRIES");

        currentQuestionNumber = maxCurrentScore;

        submitButton = (Button) findViewById(R.id.submit_button_multiple);

        usedRegionsList = new ArrayList<String>();

        answers = new String[NUMBER_OF_ANSWERS];

        logUsedCapitals = new String[NUMBER_OF_ANSWERS * NUMBER_MULTIPLE_QUESTIONS];
        logCheckedBoxes = new boolean[NUMBER_OF_ANSWERS * NUMBER_MULTIPLE_QUESTIONS];
        logCorrectCheckBoxes = new boolean[NUMBER_OF_ANSWERS * NUMBER_MULTIPLE_QUESTIONS];

        //HashMap mapping from the String region to the appropriate ArrayList
        regionsMap = new HashMap<String, ArrayList<String>>();
        regionsMap.put(REGIONS[0], europeanCapitals);
        regionsMap.put(REGIONS[1], asianCapitals);
        regionsMap.put(REGIONS[2], americasCapitals);
        regionsMap.put(REGIONS[3], africanCapitals);
        regionsMap.put(REGIONS[4], oceaniaCapitals);
    }

    /**
     * ask a question to the player, and provide potential answers
     */
    public void populateQuestion() {

        for (int i = 0; i < checkBoxes.length; i++)
            checkBoxes[i].setChecked(false);

        currentQuestionNumber++;
        currentMultipleQuestionsNumber++;

        questionNumberTextView.setText(getString(R.string.question, String.valueOf(currentQuestionNumber)));

        currentScoreTextView.setText(getString(R.string.current_score, String.valueOf(currentScore), String.valueOf(maxCurrentScore)));

        //select a region not previously used, and then add it to the ArrayList containing used regions
        selectedRegion = selectValue(usedRegionsList, REGIONS);
        usedRegionsList.add(selectedRegion);

        questionTextView.setText(getString(R.string.ask_question_multiple, selectedRegion));

        //randomly select capitals to be included in the question
        ArrayList<String> usedCapitalsList = new ArrayList<String>();
        for (int i = 0; i < answers.length; i++) {
            answers[i] = selectValue(usedCapitalsList, allCapitals);
            usedCapitalsList.add(answers[i]);
        }

        for (int i = 0; i < checkBoxes.length; i++)
            checkBoxes[i].setText(answers[i]);
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

    /**
     * Randomly selects a value from valuesArray which is not already in an ArrayList provided
     * as parameter
     *
     * @param usedValues  ArrayList of values already used and which cannot be selected from
     *                    valuesArray
     * @param valuesArray ArrayList of values from among which one shall be randomly selected
     * @return random String element from valuesArray
     */
    public String selectValue(ArrayList<String> usedValues, ArrayList<String> valuesArray) {
        boolean validValue = false;
        Random rand = new Random();
        String returnValue = "";

        while (!validValue) {
            int selectedIndex = rand.nextInt(valuesArray.size());
            if (!usedValues.contains(valuesArray.get(selectedIndex))) {
                validValue = true;
                returnValue = valuesArray.get(selectedIndex);
            }
        }
        return returnValue;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        usedRegionsList = savedInstanceState.getStringArrayList(KEY_USED_REGIONS);
        selectedRegion = savedInstanceState.getString(KEY_SELECTED_REGION);
        answers = savedInstanceState.getStringArray(KEY_ANSWERS);
        currentMultipleQuestionsNumber = savedInstanceState.getInt(KEY_CURRENT_MULTIPLE_QUESTION_NUMBER);

        questionTextView.setText(getString(R.string.ask_question_multiple, selectedRegion));
        for (int i = 0; i < checkBoxes.length; i++)
            checkBoxes[i].setText(answers[i]);

        testeeName = savedInstanceState.getString(KEY_TESTEE_NAME);

        currentQuestionNumber = savedInstanceState.getInt(KEY_CURRENT_QUESTION_NUMBER);
        questionNumberTextView.setText(getString(R.string.question, String.valueOf(currentQuestionNumber)));

        providedAnswers = savedInstanceState.getStringArray(KEY_PROVIDED_ANSWERS);
        correctAnswers = savedInstanceState.getStringArray(KEY_CORRECT_ANSWERS);
        usedCountriesList = savedInstanceState.getStringArrayList(KEY_USED_COUNTRIES);

        currentScore = savedInstanceState.getInt(KEY_CURRENT_SCORE);
        maxCurrentScore = savedInstanceState.getInt(KEY_MAX_CURRENT_SCORE);
        currentScoreTextView.setText(getString(R.string.current_score, String.valueOf(currentScore), String.valueOf(maxCurrentScore)));

        logUsedCapitals = savedInstanceState.getStringArray(KEY_LOG_USED_CAPITALS);
        logCheckedBoxes = savedInstanceState.getBooleanArray(KEY_LOG_CHECKED_CHECKBOXES);
        logCorrectCheckBoxes = savedInstanceState.getBooleanArray(KEY_LOG_CORRECT_CHECKED_CHECKBOXES);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putStringArrayList(KEY_USED_REGIONS, usedRegionsList);
        savedInstanceState.putString(KEY_SELECTED_REGION, selectedRegion);
        savedInstanceState.putStringArray(KEY_ANSWERS, answers);
        savedInstanceState.putInt(KEY_CURRENT_MULTIPLE_QUESTION_NUMBER, currentMultipleQuestionsNumber);
        savedInstanceState.putString(KEY_TESTEE_NAME, testeeName);
        savedInstanceState.putInt(KEY_CURRENT_QUESTION_NUMBER, currentQuestionNumber);
        savedInstanceState.putStringArray(KEY_PROVIDED_ANSWERS, providedAnswers);
        savedInstanceState.putStringArray(KEY_CORRECT_ANSWERS, correctAnswers);
        savedInstanceState.putStringArrayList(KEY_USED_COUNTRIES, usedCountriesList);
        savedInstanceState.putInt(KEY_CURRENT_SCORE, currentScore);
        savedInstanceState.putInt(KEY_MAX_CURRENT_SCORE, maxCurrentScore);
        savedInstanceState.putStringArray(KEY_LOG_USED_CAPITALS, logUsedCapitals);
        savedInstanceState.putBooleanArray(KEY_LOG_CHECKED_CHECKBOXES, logCheckedBoxes);
        savedInstanceState.putBooleanArray(KEY_LOG_CORRECT_CHECKED_CHECKBOXES, logCorrectCheckBoxes);
        super.onSaveInstanceState(savedInstanceState);
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
