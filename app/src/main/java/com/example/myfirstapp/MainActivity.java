package com.example.myfirstapp;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.app.FragmentManager;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText userNameEditText;
    private EditText ageEditText;
    private TextView birthDateTextView;
    private EditText selfDescriptionEditText;
    private EditText occupationEditText;

    private Button btnSubmit;
    private Button btnBirthDate;
    private TextView errorMsgTextView;

    private Pattern pattern;

    private Calendar currentDate;
    private int mDay;
    private int mMonth;
    private int mYear;

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        userNameEditText = findViewById(R.id.userNameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        ageEditText.setEnabled(false);
        selfDescriptionEditText = findViewById(R.id.selfDescriptionEditText);
        occupationEditText = findViewById(R.id.occupationEditText);

        birthDateTextView = findViewById(R.id.birthDateTextView);

        btnSubmit = findViewById(R.id.submitButtonId);
        btnBirthDate = findViewById(R.id.birthDateButtonId);
        errorMsgTextView = findViewById(R.id.errorMsgTextView);

        pattern = Pattern.compile(Constants.regexEmailPattern);

        currentDate = Calendar.getInstance();

        manager = getFragmentManager();
    }

    public void onBirthDateClick(View view) {
        mYear = currentDate.get(Calendar.YEAR);
        mMonth = currentDate.get(Calendar.MONTH);
        mDay = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear + 1, dayOfMonth);
                    int age = calculateCurrentAge(newDate);
                    ageEditText.setText(String.valueOf(age));
                    birthDateTextView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
    }


    public void onSubmit(View view) {
        // TODO: remove before pushing to master
        if (isValidForm()) {
            Intent intent = new Intent(MainActivity.this, TabActivity.class);
            intent.putExtra(Constants.KEY_NAME, nameEditText.getText().toString());
            intent.putExtra(Constants.KEY_EMAIL, emailEditText.getText().toString());
            intent.putExtra(Constants.KEY_USERNAME, userNameEditText.getText().toString());
            intent.putExtra(Constants.KEY_BDATE, birthDateTextView.getText().toString());
            intent.putExtra(Constants.KEY_AGE, ageEditText.getText().toString());
            intent.putExtra(Constants.KEY_DESCRIPTION, selfDescriptionEditText.getText().toString());
            intent.putExtra(Constants.KEY_OCCUPATION, occupationEditText.getText().toString());

            startActivity(intent);
       }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private int calculateCurrentAge(Calendar inputBirthDate) {
        LocalDate today = LocalDate.now();
        LocalDate birthDay = LocalDate.of(inputBirthDate.get(Calendar.YEAR),
                                         inputBirthDate.get(Calendar.MONTH),
                                         inputBirthDate.get(Calendar.DAY_OF_MONTH));
        Period period = Period.between(birthDay, today);
        return period.getYears();
    }

    /**
     * Returning true if the field is valid, otherwise false.
     * @param editText
     * @param errorMessage
     * @param errorConstant
     * @return true or false.
     */
    private boolean validateField(EditText editText,
                                  StringBuilder errorMessage,
                                  String errorConstant) {
        if (editText == null || editText.getText().toString().length() == 0) {
            errorMessage.append(errorConstant);
        }

        if (editText.equals(emailEditText) && emailEditText.getText().toString().length() != 0) {
            Matcher matcher = pattern.matcher(emailEditText.getText().toString());
            if (!matcher.matches()) {
                errorMessage.append(Constants.EMAIL_IS_INVALID);
            }
        }

        if (editText.equals(ageEditText) && ageEditText.getText().toString().length() != 0) {
            if (Integer.parseInt(ageEditText.getText().toString()) < Constants.AGE_LIMIT) {
                    errorMessage.append(Constants.UNDER_18_NOT_ALLOWED);

            }
        }

        errorMsgTextView.setText(errorMessage);

        // errorMsgTextView = 0 meaning no errors
        // Returning true if the field is valid
        if (errorMsgTextView.getText().toString().length() == 0) {
            return true;
        }
        return false;
    }

    private boolean isValidForm() {
        StringBuilder errorMessage = new StringBuilder();

        final boolean isValidName = validateField(nameEditText, errorMessage, Constants.NAME_EMPTY_OR_NULL);
        final boolean isValidEmail = validateField(emailEditText, errorMessage, Constants.EMAIL_EMPTY_OR_NULL);
        final boolean isValidUserName = validateField(userNameEditText, errorMessage, Constants.USERNAME_EMPTY_OR_NULL);
        final boolean isValidBirthDate = validateBirthDate(errorMessage);
        final boolean isValidAge = validateField(ageEditText, errorMessage, Constants.AGE_EMPTY_OR_NULL);
        final boolean isValidDescription = validateField(selfDescriptionEditText, errorMessage, Constants.DESCRIPTION_IS_EMPTY_OR_NULL);
        final boolean isValidOccupation = validateField(occupationEditText, errorMessage, Constants.OCCUPATION_IS_EMPTY_OR_NULL);

        return isValidName && isValidEmail && isValidUserName && isValidBirthDate && isValidAge
                && isValidDescription && isValidOccupation;
    }

    public boolean validateBirthDate(StringBuilder errorMessage) {
        if (birthDateTextView == null || birthDateTextView.getText().toString().length() == 0) {
            errorMessage.append(Constants.BIRTH_DATE_EMPTY_OR_NULL);
        }
        errorMsgTextView.setText(errorMessage);

        // errorMsgTextView = 0 meaning no errors
        // Returning true if the field is valid
        if (errorMsgTextView.getText().toString().length() == 0) {
            return true;
        }
        return false;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(Constants.KEY_BDATE)) {
            birthDateTextView.setText((String)savedInstanceState.get(Constants.KEY_BDATE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.KEY_BDATE, birthDateTextView.getText().toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        clearFormFields();
    }

    private void clearFormFields() {
        nameEditText.setText("");
        emailEditText.setText("");
        userNameEditText.setText("");
        ageEditText.setText("");
        birthDateTextView.setText("");
        selfDescriptionEditText.setText("");
        occupationEditText.setText("");
    }
}
