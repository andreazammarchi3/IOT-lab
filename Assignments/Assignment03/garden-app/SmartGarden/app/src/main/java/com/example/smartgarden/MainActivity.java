package com.example.smartgarden;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonRequireManualControl = findViewById(R.id.button_require_manual_control);
        buttonRequireManualControl.setOnClickListener(view ->
                System.out.println("clicked")
        );

        Button buttonLed1 = findViewById(R.id.button_led_1);
        buttonLed1.setOnClickListener(view ->
                System.out.println("clicked")
        );

        Button buttonLed2 = findViewById(R.id.button_led_2);
        buttonLed2.setOnClickListener(view ->
                System.out.println("clicked")
        );

        Button buttonLed3Plus = findViewById(R.id.button_led_3_plus);
        buttonLed3Plus.setOnClickListener(view ->
                System.out.println("clicked")
        );

        Button buttonLed3Minus = findViewById(R.id.button_led_3_minus);
        buttonLed3Minus.setOnClickListener(view ->
                System.out.println("clicked")
        );

        Button buttonLed4Plus = findViewById(R.id.button_led_4_plus);
        buttonLed4Plus.setOnClickListener(view ->
                System.out.println("clicked")
        );

        Button buttonLed4Minus = findViewById(R.id.button_led_4_minus);
        buttonLed4Minus.setOnClickListener(view ->
                System.out.println("clicked")
        );

        Button buttonIrrigationMinus = findViewById(R.id.button_irrigation_minus);
        buttonIrrigationMinus.setOnClickListener(view ->
                System.out.println("clicked")
        );

        Button buttonIrrigationPlus = findViewById(R.id.button_irrigation_plus);
        buttonIrrigationPlus.setOnClickListener(view ->
                System.out.println("clicked")
        );

        Button buttonIrrigation = findViewById(R.id.button_irrigation);
        buttonIrrigation.setOnClickListener(view ->
                System.out.println("clicked")
        );

        TextView led3Counter = findViewById(R.id.led_3_counter);

        TextView led4Counter = findViewById(R.id.led_4_counter);

        TextView irrigationCounter = findViewById(R.id.irrigation_counter);
    }
}