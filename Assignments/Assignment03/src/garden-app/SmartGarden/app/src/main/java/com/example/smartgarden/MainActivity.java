package com.example.smartgarden;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartgarden.btutils.BluetoothChannel;
import com.example.smartgarden.btutils.ConnectToEmulatedBluetoothServerTask;
import com.example.smartgarden.btutils.ConnectionTask;
import com.example.smartgarden.btutils.EmulatedBluetoothChannel;

public class MainActivity extends AppCompatActivity {

    private boolean led1Bool = false;
    private boolean led2Bool = false;
    private int led3Counter = 0;
    private int led4Counter = 0;
    private int mode = 0;
    private boolean irrMode = false;
    private int irrCounter = 0;

    // Bluetooth
    private BluetoothChannel btChannel;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_alarm) {
            if (mode == 2) {
                sendMessage("MANUAL");
                mode = 1;
            }
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        btChannel.close();
    }

    private void setEnabledAllBtns(Boolean state) {
        findViewById(R.id.button_led_1).setEnabled(state);
        findViewById(R.id.button_led_2).setEnabled(state);
        findViewById(R.id.button_led_3_minus).setEnabled(state);
        findViewById(R.id.button_led_3_plus).setEnabled(state);
        findViewById(R.id.button_led_4_minus).setEnabled(state);
        findViewById(R.id.button_led_4_plus).setEnabled(state);
        findViewById(R.id.button_irrigation).setEnabled(state);
        findViewById(R.id.button_irrigation_minus).setEnabled(state);
        findViewById(R.id.button_irrigation_plus).setEnabled(state);

    }

    private void incLed3() {
        if (led3Counter < 4) {
            led3Counter++;
            sendMessage("3" + led3Counter);
        }
        ((TextView)findViewById(R.id.led_3_counter)).setText(String.valueOf(led3Counter));
    }

    private void incLed4() {
        if (led4Counter < 4) {
            led4Counter++;
            sendMessage("4" + led4Counter);
        }
        ((TextView)findViewById(R.id.led_4_counter)).setText(String.valueOf(led4Counter));
    }

    private void incIrr() {
        if (irrCounter < 4) {
            irrCounter++;
            sendMessage("5" + irrCounter);
            irrMode = true;
        }
        ((TextView)findViewById(R.id.irrigation_counter)).setText(String.valueOf(irrCounter));
    }

    private void decLed3() {
        if (led3Counter > 0) {
            led3Counter--;
            sendMessage("3" + led3Counter);
        }
        ((TextView)findViewById(R.id.led_3_counter)).setText(String.valueOf(led3Counter));
    }

    private void decLed4() {
        if (led4Counter > 0) {
            led4Counter--;
            sendMessage("4" + led4Counter);
        }
        ((TextView)findViewById(R.id.led_4_counter)).setText(String.valueOf(led4Counter));
    }

    private void decIrr() {
        if (irrCounter > 1) {
            irrCounter--;
            sendMessage("5" + irrCounter);
            irrMode = true;
        }
        ((TextView)findViewById(R.id.irrigation_counter)).setText(String.valueOf(irrCounter));
    }

    private void toggleLed1() {
        if (led1Bool) {
            led1Bool = false;
            sendMessage("10");
        } else {
            led1Bool = true;
            sendMessage("11");
        }
    }

    private void toggleLed2() {
        if (led2Bool) {
            led2Bool = false;
            sendMessage("20");
        } else {
            led2Bool = true;
            sendMessage("21");
        }
    }

    private void toggleIrr() {
        if (irrMode) {
            irrMode = false;
            sendMessage("50");
            irrCounter = 0;
            ((TextView)findViewById(R.id.irrigation_counter)).setText(String.valueOf(irrCounter));
        } else {
            irrMode = true;
            sendMessage("51");
            irrCounter = 1;
            ((TextView)findViewById(R.id.irrigation_counter)).setText(String.valueOf(irrCounter));
        }
    }

    private void connectToBTServer() {
        new ConnectToEmulatedBluetoothServerTask(new ConnectionTask.EventListener() {
            @Override
            public void onConnectionActive(final BluetoothChannel channel) {
                findViewById(R.id.button_require_manual_control).setEnabled(false);
                setEnabledAllBtns(true);
                btChannel = channel;
                btChannel.registerListener(new EmulatedBluetoothChannel.Listener() {
                    @Override
                    public void onMessageReceived(String receivedMessage) {
                        ((TextView) findViewById(R.id.outText)).append(String.format(
                                "> [RECEIVED from %s] %s\n",
                                btChannel.getRemoteDeviceName(),
                                receivedMessage
                        ));
                    }

                    @Override
                    public void onMessageSent(String sentMessage) {
                        ((TextView) findViewById(R.id.outText)).append(String.format(
                                "> [SENT to %s] %s\n",
                                btChannel.getRemoteDeviceName(),
                                sentMessage
                        ));
                    }
                });
            }

            @Override
            public void onConnectionCanceled() {
                ((TextView) findViewById(R.id.outText)).setText(String.format(
                        "Status : unable to connect, device %s not found!",
                        "host machine"
                ));
            }
        }).execute();
    }

    private void sendMessage(final String message) {
        new Thread(() -> btChannel.sendMessage(message)).start();
    }

    private void initUI() {
        findViewById(R.id.button_require_manual_control).setOnClickListener(l -> {
            connectToBTServer();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 3s
                    sendMessage("MANUAL");
                    findViewById(R.id.button_require_manual_control).setEnabled(false);
                }
            }, 3000);
        });
        findViewById(R.id.button_led_1).setOnClickListener(view -> toggleLed1());
        findViewById(R.id.button_led_2).setOnClickListener(view -> toggleLed2());
        findViewById(R.id.button_led_3_plus).setOnClickListener(view -> incLed3());
        findViewById(R.id.button_led_3_minus).setOnClickListener(view -> decLed3());
        findViewById(R.id.button_led_4_plus).setOnClickListener(view -> incLed4());
        findViewById(R.id.button_led_4_minus).setOnClickListener(view -> decLed4());
        findViewById(R.id.button_irrigation_minus).setOnClickListener(view -> decIrr());
        findViewById(R.id.button_irrigation_plus).setOnClickListener(view -> incIrr());
        findViewById(R.id.button_irrigation).setOnClickListener(view -> toggleIrr());
        ((TextView) findViewById(R.id.outText)).setMovementMethod(new ScrollingMovementMethod());

        setEnabledAllBtns(false);
    }

}