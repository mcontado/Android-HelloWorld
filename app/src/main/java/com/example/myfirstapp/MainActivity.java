package com.example.myfirstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.pressMeButtonId);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                TextView textViewHelloWorld = findViewById(R.id.textViewHelloWorld);
                TextView textViewNameDateId = findViewById(R.id.textViewNameDateId);

                textViewHelloWorld.setText(R.string.hello_world);
                textViewNameDateId.setText(R.string.lastname_firstname_date);
            }
        });
    }
}
