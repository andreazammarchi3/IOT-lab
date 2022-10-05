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
    public String mode = "AUTO";
    public boolean irrMode = false;
    public int irrCounter = 0;

    private TextView led3CounterText = findViewById(R.id.led_3_counter);

    private TextView led4CounterText = findViewById(R.id.led_4_counter);

    private TextView irrCounterText = findViewById(R.id.irrigation_counter);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonRequireManualControl = findViewById(R.id.button_require_manual_control);
        buttonRequireManualControl.setOnClickListener(view ->
                System.out.println("Required MANUAL control")
        );

        Button buttonLed1 = findViewById(R.id.button_led_1);
        buttonLed1.setOnClickListener(view ->
                boolBtnClick(led1Bool)
        );

        Button buttonLed2 = findViewById(R.id.button_led_2);
        buttonLed2.setOnClickListener(view ->
                boolBtnClick(led2Bool)
        );

        Button buttonLed3Plus = findViewById(R.id.button_led_3_plus);
        buttonLed3Plus.setOnClickListener(view ->
                counterBtnClick(led3Counter, led3CounterText, true)
        );

        Button buttonLed3Minus = findViewById(R.id.button_led_3_minus);
        buttonLed3Minus.setOnClickListener(view ->
                counterBtnClick(led3Counter, led3CounterText, false)
        );

        Button buttonLed4Plus = findViewById(R.id.button_led_4_plus);
        buttonLed4Plus.setOnClickListener(view ->
                counterBtnClick(led4Counter, led4CounterText, true)
        );

        Button buttonLed4Minus = findViewById(R.id.button_led_4_minus);
        buttonLed4Minus.setOnClickListener(view ->
                counterBtnClick(led4Counter, led4CounterText, false)
        );

        Button buttonIrrigationMinus = findViewById(R.id.button_irrigation_minus);
        buttonIrrigationMinus.setOnClickListener(view ->
                counterBtnClick(irrCounter, irrCounterText, false)
        );

        Button buttonIrrigationPlus = findViewById(R.id.button_irrigation_plus);
        buttonIrrigationPlus.setOnClickListener(view ->
                counterBtnClick(irrCounter, irrCounterText, true)
        );

        Button buttonIrrigation = findViewById(R.id.button_irrigation);
        buttonIrrigation.setOnClickListener(view ->
                boolBtnClick(irrMode)
        );
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

    private void boolBtnClick(boolean bool) {
        bool = !bool;
    }

    private void counterBtnClick(int counter, TextView view, boolean sum) {
        if(counter<4 && sum) {
            counter++;
        } else if(counter>0 && !sum) {
            counter--;
        }
        view.setText(counter);
    }

    private void led4PlusClick() {
        if(led4Counter<4) {
            led4Counter++;
        }
        led4CounterText.setText(led4Counter);
    }
}