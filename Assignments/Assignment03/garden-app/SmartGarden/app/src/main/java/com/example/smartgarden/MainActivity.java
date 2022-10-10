package com.example.smartgarden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.smartgarden.utils.CommChannel;
import com.example.smartgarden.utils.ExtendedSerialCommChannel;
import com.example.smartgarden.utils.SerialCommChannel;

public class MainActivity extends AppCompatActivity {

    private boolean led1Bool = false;
    private boolean led2Bool = false;
    private int led3Counter = 0;
    private int led4Counter = 0;
    private int mode = 0;
    private boolean irrMode = false;
    private int irrCounter = 0;

    private TextView led3CounterText;
    private TextView led4CounterText;
    private TextView irrCounterText;

    private Button buttonLed1;
    private Button buttonLed2;
    private Button buttonLed3Plus;
    private Button buttonLed3Minus;
    private Button buttonLed4Plus;
    private Button buttonLed4Minus;
    private Button buttonIrrigationMinus;
    private Button buttonIrrigationPlus;
    private Button buttonIrrigation;

    CommChannel channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        led3CounterText = findViewById(R.id.led_3_counter);
        led4CounterText = findViewById(R.id.led_4_counter);
        irrCounterText = findViewById(R.id.irrigation_counter);

        Button buttonRequireManualControl = findViewById(R.id.button_require_manual_control);
        buttonRequireManualControl.setOnClickListener(view ->
                {
                    try {
                        requireManualControl();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );

        buttonLed1 = findViewById(R.id.button_led_1);
        buttonLed1.setOnClickListener(view ->
                toggleLed1()
        );

        buttonLed2 = findViewById(R.id.button_led_2);
        buttonLed2.setOnClickListener(view ->
                toggleLed2()
        );

        buttonLed3Plus = findViewById(R.id.button_led_3_plus);
        buttonLed3Plus.setOnClickListener(view ->
                incLed3()
        );

        buttonLed3Minus = findViewById(R.id.button_led_3_minus);
        buttonLed3Minus.setOnClickListener(view ->
                decLed3()
        );

        buttonLed4Plus = findViewById(R.id.button_led_4_plus);
        buttonLed4Plus.setOnClickListener(view ->
                incLed4()
        );

        buttonLed4Minus = findViewById(R.id.button_led_4_minus);
        buttonLed4Minus.setOnClickListener(view ->
                decLed4()
        );

        buttonIrrigationMinus = findViewById(R.id.button_irrigation_minus);
        buttonIrrigationMinus.setOnClickListener(view ->
                decIrr()
        );

        buttonIrrigationPlus = findViewById(R.id.button_irrigation_plus);
        buttonIrrigationPlus.setOnClickListener(view ->
                incIrr()
        );

        buttonIrrigation = findViewById(R.id.button_irrigation);
        buttonIrrigation.setOnClickListener(view ->
                toggleIrr()
        );

        setEnabledAllBtns(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_alarm) {
            if (mode != 0) {
                System.out.println("Alarm btn clicked");
                channel.sendMsg("AUTO");
                mode = 0;
            }
        }
        return true;
    }

    private void setEnabledAllBtns(Boolean state) {
        buttonLed1.setEnabled(state);
        buttonLed2.setEnabled(state);
        buttonLed3Minus.setEnabled(state);
        buttonLed3Plus.setEnabled(state);
        buttonLed4Minus.setEnabled(state);
        buttonLed4Plus.setEnabled(state);
        buttonIrrigation.setEnabled(state);
        buttonIrrigationMinus.setEnabled(state);
        buttonIrrigationPlus.setEnabled(state);

    }

    private void incLed3() {
        if (led3Counter < 4) {
            led3Counter++;
            channel.sendMsg("Led3_" + led3Counter);
        }
        led3CounterText.setText(String.valueOf(led3Counter));
    }

    private void incLed4() {
        if (led4Counter < 4) {
            led4Counter++;
            channel.sendMsg("Led4_" + led4Counter);
        }
        led4CounterText.setText(String.valueOf(led4Counter));
    }

    private void incIrr() {
        if (irrCounter < 4) {
            irrCounter++;
            channel.sendMsg("irr_" + irrCounter);
        }
        irrCounterText.setText(String.valueOf(irrCounter));
    }

    private void decLed3() {
        if (led3Counter > 0) {
            led3Counter--;
            channel.sendMsg("Led3_" + led3Counter);
        }
        led3CounterText.setText(String.valueOf(led3Counter));
    }

    private void decLed4() {
        if (led4Counter > 0) {
            led4Counter--;
            channel.sendMsg("Led4_" + led3Counter);
        }
        led4CounterText.setText(String.valueOf(led4Counter));
    }

    private void decIrr() {
        if (irrCounter > 0) {
            irrCounter--;
            channel.sendMsg("irr_" + irrCounter);
        }
        irrCounterText.setText(String.valueOf(irrCounter));
    }

    private void toggleLed1() {
        if (led1Bool) {
            led1Bool = false;
            channel.sendMsg("Led1_OFF");
        } else {
            led1Bool = true;
            channel.sendMsg("Led1_ON");
        }
    }

    private void toggleLed2() {
        if (led2Bool) {
            led2Bool = false;
            channel.sendMsg("Led2_OFF");
        } else {
            led2Bool = true;
            channel.sendMsg("Led2_ON");
        }
    }

    private void toggleIrr() {
        if (irrMode) {
            irrMode = false;
            channel.sendMsg("irr_CLOSE");
        } else {
            irrMode = true;
            channel.sendMsg("irr_OPEN");
        }
    }

    private void requireManualControl() throws InterruptedException {
        System.out.println("Required MANUAL control");
        try {
            channel = new ExtendedSerialCommChannel("/dev/cu.DSDTECHHC-05",9600);
            channel.sendMsg("MANUAL");
            String msg = channel.receiveMsg();
            System.out.println(msg);
            mode = 1;
            setEnabledAllBtns(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}