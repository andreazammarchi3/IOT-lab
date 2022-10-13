package com.example.smartgarden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.bluetooth.BluetoothAdapter;

import com.example.smartgarden.utils.Bluetooth;
import com.example.smartgarden.utils.BluetoothChannel;
import com.example.smartgarden.utils.BluetoothUtils;
import com.example.smartgarden.utils.ConnectToBluetoothServerTask;
import com.example.smartgarden.utils.ConnectionTask;
import com.example.smartgarden.utils.RealBluetoothChannel;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private boolean led1Bool = false;
    private boolean led2Bool = false;
    private int led3Counter = 0;
    private int led4Counter = 0;
    private int mode = 0;
    private boolean irrMode = false;
    private int irrCounter = 0;
    private boolean btActive = false;

    // GUI
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
    private Button buttonRequireManualControl;

    // Bluetooth
    private BluetoothChannel btChannel;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        led3CounterText = findViewById(R.id.led_3_counter);
        led4CounterText = findViewById(R.id.led_4_counter);
        irrCounterText = findViewById(R.id.irrigation_counter);

        buttonRequireManualControl = (Button)findViewById(R.id.button_require_manual_control);
        buttonRequireManualControl.setOnClickListener(view ->
                {
                    try {
                        toggleBT();
                    } catch (Exception e) {
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

        // Bluetooth init
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(btAdapter != null && !btAdapter.isEnabled()){
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), Bluetooth.bluetooth.ENABLE_BT_REQUEST);
        }
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
                // System.out.println("Alarm btn clicked");
                btChannel.sendMessage("AUTO");
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
            btChannel.sendMessage("Led3_" + led3Counter);
        }
        led3CounterText.setText(String.valueOf(led3Counter));
    }

    private void incLed4() {
        if (led4Counter < 4) {
            led4Counter++;
            btChannel.sendMessage("Led4_" + led4Counter);
        }
        led4CounterText.setText(String.valueOf(led4Counter));
    }

    private void incIrr() {
        if (irrCounter < 4) {
            irrCounter++;
            btChannel.sendMessage("irr_" + irrCounter);
        }
        irrCounterText.setText(String.valueOf(irrCounter));
    }

    private void decLed3() {
        if (led3Counter > 0) {
            led3Counter--;
            btChannel.sendMessage("Led3_" + led3Counter);
        }
        led3CounterText.setText(String.valueOf(led3Counter));
    }

    private void decLed4() {
        if (led4Counter > 0) {
            led4Counter--;
            btChannel.sendMessage("Led4_" + led3Counter);
        }
        led4CounterText.setText(String.valueOf(led4Counter));
    }

    private void decIrr() {
        if (irrCounter > 0) {
            irrCounter--;
            btChannel.sendMessage("irr_" + irrCounter);
        }
        irrCounterText.setText(String.valueOf(irrCounter));
    }

    private void toggleLed1() {
        if (led1Bool) {
            led1Bool = false;
            btChannel.sendMessage("1");
        } else {
            led1Bool = true;
            btChannel.sendMessage("0");
        }
    }

    private void toggleLed2() {
        if (led2Bool) {
            led2Bool = false;
            btChannel.sendMessage("1");
        } else {
            led2Bool = true;
            btChannel.sendMessage("0");
        }
    }

    private void toggleIrr() {
        if (irrMode) {
            irrMode = false;
            btChannel.sendMessage("irr_CLOSE");
        } else {
            irrMode = true;
            btChannel.sendMessage("irr_OPEN");
        }
    }

    private void requireManualControl() throws Exception {
        // System.out.println("Required MANUAL control");
        connectToBTServer();
    }

    private void connectToBTServer() throws Exception  {
        final BluetoothDevice serverDevice = BluetoothUtils.getPairedDeviceByAddress("98:D3:31:20:44:12");
        // final BluetoothDevice serverDevice = BluetoothUtils.getPairedDeviceByName(Bluetooth.bluetooth.BT_DEVICE_ACTING_AS_SERVER_NAME);
        final UUID uuid = BluetoothUtils.generateUuidFromString("00001101-0000-1000-8000-00805F9B34FB");

        AsyncTask<Void, Void, Integer> execute = new ConnectToBluetoothServerTask(serverDevice, uuid, new ConnectionTask.EventListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onConnectionActive(final BluetoothChannel channel) {
                System.out.println("Status : connected to server on device " + serverDevice.getName());
                btChannel = channel;
                btChannel.registerListener(new RealBluetoothChannel.Listener() {
                    @Override
                    public void onMessageReceived(String receivedMessage) {
                        System.out.println("> [RECEIVED from " +
                                btChannel.getRemoteDeviceName() +
                                "] " +
                                receivedMessage);
                    }

                    @Override
                    public void onMessageSent(String sentMessage) {
                        System.out.println("> [SENT to " +
                                btChannel.getRemoteDeviceName() +
                                "] " +
                                sentMessage);
                    }
                });
            }

            @Override
            public void onConnectionCanceled() {
                System.out.println("Status : unable to connect, device " +
                        Bluetooth.bluetooth.BT_DEVICE_ACTING_AS_SERVER_NAME +
                        " not found!");
            }
        });
        execute.execute();
    }

    @SuppressLint("SetTextI18n")
    private void toggleBT() throws Exception {
        if (!btActive) {
            requireManualControl();
            btActive = true;
            // buttonRequireManualControl.setText("Close BT connection");
            setEnabledAllBtns(true);
            mode = 1;
            btChannel.sendMessage("M");
        } else {
            /*
            btActive = false;

            buttonRequireManualControl.setText("Require manual control");
            setEnabledAllBtns(false);
            mode = 0;

            */
        }
    }


}