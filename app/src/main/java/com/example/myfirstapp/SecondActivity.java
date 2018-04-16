package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        textView = findViewById(R.id.thanksTextView);

        StringBuilder msg = new StringBuilder(Constants.THANKS_SIGN);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        assert bundle != null;
        if (bundle.containsKey(Constants.KEY_NAME)) {
            final String name = bundle.getString(Constants.KEY_NAME);
            msg.append(name);
        }

        textView.setText(msg);

    }

    public void backToMain(View view) {
        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
