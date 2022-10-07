package com.example.smartgarden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public boolean led1Bool = false;
    public boolean led2Bool = false;
    public int led3Counter = 0;
    public int led4Counter = 0;
    public int mode = 0;
    public boolean irrMode = false;
    public int irrCounter = 0;

    public TextView led3CounterText;

    public TextView led4CounterText;

    public TextView irrCounterText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        led3CounterText = findViewById(R.id.led_3_counter);
        led4CounterText = findViewById(R.id.led_4_counter);
        irrCounterText = findViewById(R.id.irrigation_counter);

        Button buttonRequireManualControl = findViewById(R.id.button_require_manual_control);
        buttonRequireManualControl.setOnClickListener(view ->
                requireManualControl()
        );

        Button buttonLed1 = findViewById(R.id.button_led_1);
        buttonLed1.setOnClickListener(view ->
                toggleLed1()
        );

        Button buttonLed2 = findViewById(R.id.button_led_2);
        buttonLed2.setOnClickListener(view ->
                toggleLed2()
        );

        Button buttonLed3Plus = findViewById(R.id.button_led_3_plus);
        buttonLed3Plus.setOnClickListener(view ->
                incLed3()
        );

        Button buttonLed3Minus = findViewById(R.id.button_led_3_minus);
        buttonLed3Minus.setOnClickListener(view ->
                decLed3()
        );

        Button buttonLed4Plus = findViewById(R.id.button_led_4_plus);
        buttonLed4Plus.setOnClickListener(view ->
                incLed4()
        );

        Button buttonLed4Minus = findViewById(R.id.button_led_4_minus);
        buttonLed4Minus.setOnClickListener(view ->
                decLed4()
        );

        Button buttonIrrigationMinus = findViewById(R.id.button_irrigation_minus);
        buttonIrrigationMinus.setOnClickListener(view ->
                decIrr()
        );

        Button buttonIrrigationPlus = findViewById(R.id.button_irrigation_plus);
        buttonIrrigationPlus.setOnClickListener(view ->
                incIrr()
        );

        Button buttonIrrigation = findViewById(R.id.button_irrigation);
        buttonIrrigation.setOnClickListener(view ->
                toggleIrr()
        );

        buttonLed1.setEnabled(false);
        buttonLed2.setEnabled(false);
        buttonLed3Minus.setEnabled(false);
        buttonLed3Plus.setEnabled(false);
        buttonLed4Minus.setEnabled(false);
        buttonLed4Plus.setEnabled(false);
        buttonIrrigation.setEnabled(false);
        buttonIrrigationMinus.setEnabled(false);
        buttonIrrigationPlus.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_alarm) {
            System.out.println("Alarm btn clicked");
        }
        return true;
    }

    private void incLed3() {
        if (led3Counter < 4) {
            led3Counter++;
        }
        led3CounterText.setText(String.valueOf(led3Counter));
    }

    private void incLed4() {
        if (led4Counter < 4) {
            led4Counter++;
        }
        led4CounterText.setText(String.valueOf(led4Counter));
    }

    private void incIrr() {
        if (irrCounter < 4) {
            irrCounter++;
        }
        irrCounterText.setText(String.valueOf(irrCounter));
    }

    private void decLed3() {
        if (led3Counter > 0) {
            led3Counter--;
        }
        led3CounterText.setText(String.valueOf(led3Counter));
    }

    private void decLed4() {
        if (led4Counter > 0) {
            led4Counter--;
        }
        led4CounterText.setText(String.valueOf(led4Counter));
    }

    private void decIrr() {
        if (irrCounter > 0) {
            irrCounter--;
        }
        irrCounterText.setText(String.valueOf(irrCounter));
    }

    private void toggleLed1() {
        led1Bool = !led1Bool;
    }

    private void toggleLed2() {
        led2Bool = !led2Bool;
    }

    private void toggleIrr() {
        irrMode = !irrMode;
    }

    private void requireManualControl() {
        System.out.println("Required MANUAL control");
        
    }
}