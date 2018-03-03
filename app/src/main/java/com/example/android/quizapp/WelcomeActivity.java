package com.example.android.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;


public class WelcomeActivity extends AppCompatActivity {

    private static final String KEY_TESTEE_NAME = "TesteeName";
    private static final String KEY_CHECKBOX = "checkbox";
    private static final String KEY_NAME_INTRODUCED = "nameIntroduced";
    private String testeeName;
    private boolean isCheckboxChecked = false;
    private boolean isNameIntroduced = false;
    private EditText nameEditText;
    private Button startQuizButton;
    private CheckBox agreeCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        startQuizButton = (Button) findViewById(R.id.start_quiz_button);
        agreeCheckBox = (CheckBox) findViewById(R.id.agree_checkbox);

        startQuizButton.setEnabled(false);

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            /**
             * Listener for the change of text in EditText.
             * In case both name is introduced and checkbox checked, user can start quiz,
             * otherwise the Start Quiz button is disabled
             * @param s
             * @param start
             * @param before
             * @param count
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                testeeName = nameEditText.getText().toString();
                if (!TextUtils.isEmpty(testeeName)) {
                    isNameIntroduced = true;
                } else {
                    isNameIntroduced = false;
                }

                if (isNameIntroduced && isCheckboxChecked) {
                    startQuizButton.setEnabled(true);
                } else {
                    startQuizButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /**
         * listener for CheckBox.
         * In case both name is introduced and checkbox checked, user can start quiz,
         * otherwise the Start Quiz button is disabled
         */
        agreeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isCheckboxChecked = true;
                } else {
                    isCheckboxChecked = false;
                }
                if (isNameIntroduced && isCheckboxChecked) {
                    startQuizButton.setEnabled(true);
                } else {
                    startQuizButton.setEnabled(false);
                }
            }
        });

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        testeeName = savedInstanceState.getString(KEY_TESTEE_NAME);
        displayEditText(testeeName, nameEditText);

        isCheckboxChecked = savedInstanceState.getBoolean(KEY_CHECKBOX);
        isNameIntroduced = savedInstanceState.getBoolean(KEY_NAME_INTRODUCED);

        //maintain the enabled / disabled status of the Start Quiz button
        if (isNameIntroduced && isCheckboxChecked) {
            startQuizButton.setEnabled(true);
        } else {
            startQuizButton.setEnabled(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(KEY_TESTEE_NAME, testeeName);
        savedInstanceState.putBoolean(KEY_NAME_INTRODUCED, isNameIntroduced);
        savedInstanceState.putBoolean(KEY_CHECKBOX, isCheckboxChecked);

        super.onSaveInstanceState(savedInstanceState);
    }


    /**
     * display text in EditText
     *
     * @param toDisplay text to be displayed in EditText
     * @param et        EditText in which toDisplay shall be displayed
     */
    public void displayEditText(String toDisplay, EditText et) {
        et.setText(toDisplay);
    }

    /**
     * Launches the quiz
     *
     * @param view
     */
    public void startQuiz(View view) {
        Intent startQuizIntent = new Intent(this, QuizActivity.class);
        startQuizIntent.putExtra("KEY_TESTEE_NAME", testeeName);
        startActivity(startQuizIntent);
    }
}
