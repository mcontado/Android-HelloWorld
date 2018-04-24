package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    TextView thanksTextView;
    TextView nameTextView;
    TextView ageTextView;
    TextView emailTextView;
    TextView userNameTextView;
    TextView birthDateTextView;
    TextView descriptionTextView;
    TextView occupationTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        thanksTextView = findViewById(R.id.thanksTextView);
        nameTextView = findViewById(R.id.nameTextViewSecondActivity);
        ageTextView = findViewById(R.id.ageTextViewSecondActivity);
        emailTextView = findViewById(R.id.emailTextViewSecondActivity);
        userNameTextView = findViewById(R.id.userNameTextViewSecondActivity);
        birthDateTextView = findViewById(R.id.birthDateTextViewSecondActivity);
        descriptionTextView = findViewById(R.id.descriptionTextViewSecondActivity);
        occupationTextView = findViewById(R.id.occupationTextViewSecondActivity);

        StringBuilder msg = new StringBuilder(Constants.THANKS_SIGN);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        assert bundle != null;
        if (bundle.containsKey(Constants.KEY_NAME)) {
            final String name = bundle.getString(Constants.KEY_NAME);
            nameTextView.setText(name);
            msg.append(name);
        }
        final String age = bundle.getString(Constants.KEY_AGE);
        ageTextView.setText(age);

        final String email = bundle.getString(Constants.KEY_EMAIL);
        emailTextView.setText(email);

        final String userName = bundle.getString(Constants.KEY_USERNAME);
        userNameTextView.setText(userName);

        final String birthDate = bundle.getString(Constants.KEY_BDATE);
        birthDateTextView.setText(birthDate);

        final String description = bundle.getString(Constants.KEY_DESCRIPTION);
        descriptionTextView.setText(description);

        final String occupation = bundle.getString(Constants.KEY_OCCUPATION);
        occupationTextView.setText(occupation);

        thanksTextView.setText(msg);

    }

    public void backToMain(View view) {
        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
