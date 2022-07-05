package com.example.gardenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mButton = findViewById(R.id.button_require_manual_control);
        mButton.setOnClickListener(view -> System.out.println("clicked"));
    }
}