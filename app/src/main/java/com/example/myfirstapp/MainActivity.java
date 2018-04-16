package com.example.myfirstapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText userNameEditText;
    private EditText ageEditText;
    private EditText birthDateEditText;

    private Button submitButton;
    private TextView errorMsgTextView;

    private Pattern pattern;

    private Calendar currentDate;
    private int mDay;
    private int mMonth;
    private int mYear;

    private DatePickerDialog birthDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        userNameEditText = findViewById(R.id.userNameEditText);
        ageEditText = findViewById(R.id.ageEditText);

        birthDateEditText = findViewById(R.id.birthDateEditText);
        birthDateEditText.setInputType(InputType.TYPE_NULL);
        birthDateEditText.requestFocus();

        submitButton = findViewById(R.id.submitButtonId);
        errorMsgTextView = findViewById(R.id.errorMsgTextView);

        pattern = Pattern.compile(Constants.regexEmailPattern);

        currentDate = Calendar.getInstance();

        setDateTimeField();
    }

    private void setDateTimeField() {
        birthDateEditText.setOnClickListener(this);

        final Calendar newCalendar = Calendar.getInstance();
        birthDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mYear = newDate.get(Calendar.YEAR);
                int age = currentDate.get(Calendar.YEAR) - mYear;
                ageEditText.setText(String.valueOf(age));
                birthDateEditText.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view) {
        if(view == birthDateEditText) {
            birthDatePickerDialog.show();
        }
    }


    public void onSubmit(View view) {

        if (isValidForm()) {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra(Constants.KEY_NAME, nameEditText.getText().toString());
            startActivity(intent);
        }

    }

    /**
     * Returning true if the field is valid, otherwise false.
     * @param editText
     * @param errorMessage
     * @param errorConstant
     * @return true or false.
     */
    private boolean validateForm(EditText editText,
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

        return validateForm(nameEditText, errorMessage, Constants.NAME_EMPTY_OR_NULL) &&
                validateForm(emailEditText, errorMessage, Constants.EMAIL_EMPTY_OR_NULL) &&
                validateForm(userNameEditText, errorMessage, Constants.USERNAME_EMPTY_OR_NULL) &&
                validateForm(ageEditText, errorMessage, Constants.AGE_EMPTY_OR_NULL) &&
                validateForm(birthDateEditText, errorMessage, Constants.BIRTH_DATE_EMPTY_OR_NULL);
    }
}
