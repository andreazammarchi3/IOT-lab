package com.example.smartgarden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

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

    // bluetooth
    public UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    BluetoothAdapter mBluetoothAdapter = null;
    BluetoothSocket mmSocket = null;
    BluetoothDevice mmDevice = null;
    OutputStream outStream;
    InputStream inStream;

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
                    } catch (InterruptedException | IOException e) {
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
                sendMsg("AUTO");
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
            sendMsg("Led3_" + led3Counter);
        }
        led3CounterText.setText(String.valueOf(led3Counter));
    }

    private void incLed4() {
        if (led4Counter < 4) {
            led4Counter++;
            sendMsg("Led4_" + led4Counter);
        }
        led4CounterText.setText(String.valueOf(led4Counter));
    }

    private void incIrr() {
        if (irrCounter < 4) {
            irrCounter++;
            sendMsg("irr_" + irrCounter);
        }
        irrCounterText.setText(String.valueOf(irrCounter));
    }

    private void decLed3() {
        if (led3Counter > 0) {
            led3Counter--;
            sendMsg("Led3_" + led3Counter);
        }
        led3CounterText.setText(String.valueOf(led3Counter));
    }

    private void decLed4() {
        if (led4Counter > 0) {
            led4Counter--;
            sendMsg("Led4_" + led3Counter);
        }
        led4CounterText.setText(String.valueOf(led4Counter));
    }

    private void decIrr() {
        if (irrCounter > 0) {
            irrCounter--;
            sendMsg("irr_" + irrCounter);
        }
        irrCounterText.setText(String.valueOf(irrCounter));
    }

    private void toggleLed1() {
        if (led1Bool) {
            led1Bool = false;
            sendMsg("Led1_OFF");
        } else {
            led1Bool = true;
            sendMsg("Led1_ON");
        }
    }

    private void toggleLed2() {
        if (led2Bool) {
            led2Bool = false;
            sendMsg("Led2_OFF");
        } else {
            led2Bool = true;
            sendMsg("Led2_ON");
        }
    }

    private void toggleIrr() {
        if (irrMode) {
            irrMode = false;
            sendMsg("irr_CLOSE");
        } else {
            irrMode = true;
            sendMsg("irr_OPEN");
        }
    }

    private void requireManualControl() throws InterruptedException, IOException {
        System.out.println("Required MANUAL control");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // IL BLUETOOTH NON E' SUPPORTATO
            Toast.makeText(MainActivity.this, "BlueTooth non supportato", Toast.LENGTH_LONG).show();
            // tgb.setChecked(false);
        } else {
            if (!mBluetoothAdapter.isEnabled()) { //controlla che sia abilitato il devices
                //  NON E' ABILITATO IL BLUETOOTH
                Toast.makeText(MainActivity.this, "BlueTooth non abilitato", Toast.LENGTH_LONG).show();
                // tgb.setChecked(false);
            } else {
                //  IL BLUETOOTH E' ABILITATO
                mmDevice = mBluetoothAdapter.getRemoteDevice("98:D3:31:20:44:12"); //MAC address del bluetooth di arduino
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        System.out.println("Permission");
                    }
                    mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                }
                catch (IOException e){
                    // tgb.setChecked(false);
                }
                try{
                    // CONNETTE IL DISPOSITIVO TRAMITE IL SOCKET mmSocket
                    mmSocket.connect();
                    if (mmSocket.isConnected()) {
                        outStream = mmSocket.getOutputStream();
                        inStream = mmSocket.getInputStream();
                        Toast.makeText(MainActivity.this, "ON",  Toast.LENGTH_SHORT).show(); //bluetooth è connesso
                        mode = 1;
                        setEnabledAllBtns(true);
                    } else {
                        System.out.println("NOT CONNECTED");
                    }
                }
                catch (IOException closeException){
                    //tgb.setChecked(false);
                    try{
                        //TENTA DI CHIUDERE IL SOCKET
                        mmSocket.close();
                    }
                    catch (IOException ignored){
                    }
                    Toast.makeText(MainActivity.this, "connessione non riuscita",  Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void sendMsg(String message) {
        if (outStream == null)
        {
            return;
        }
        byte[] msgBuffer = message.getBytes();
        try
        {
            outStream.write(msgBuffer);
        }
        catch (IOException e)
        {
            Toast.makeText(MainActivity.this, "Messaggio non Inviato", Toast.LENGTH_SHORT).show();
        }
    }

    private int receiveMsg() {
        if (inStream == null)
        {
            return -1;
        }
        try
        {
            return inStream.read();
        }
        catch (IOException e)
        {
            Toast.makeText(MainActivity.this, "Messaggio non Inviato", Toast.LENGTH_SHORT).show();
            return -1;
        }
    }


}