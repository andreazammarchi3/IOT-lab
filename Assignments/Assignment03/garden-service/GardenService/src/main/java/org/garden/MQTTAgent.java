package org.garden;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;

/*
 * MQTT Agent
 */
public class MQTTAgent extends AbstractVerticle {
    private MqttClient client;

    private int temperature = -1;
    private int luminosity = -1;

    public MQTTAgent() {
    }

    @Override
    public void start() {
        client = MqttClient.create(vertx);

        client.connect(1883, "broker.mqtt-dashboard.com", c -> {

            log("connected");

            log("subscribing...");
            client.publishHandler(s -> {
                        String data = s.payload().toString();
                        System.out.println(s.topicName() + ": " + data);
                        temperature = Integer.parseInt(data.split(", ")[0]);
                        luminosity = Integer.parseInt(data.split(", ")[1]);
                    })
                    .subscribe("SmartGarden/data", 2);


            /*
            log("publishing a msg");
            client.publish("SmartGarden/data",
                    Buffer.buffer("hello"),
                    MqttQoS.AT_LEAST_ONCE,
                    false,
                    false);
             */
        });
    }


    private void log(String msg) {
        System.out.println("[DATA SERVICE] " + msg);
    }

    public int getTemperature() {
        return temperature;
    }

    public int getLuminosity() {
        return luminosity;
    }

    public void setMode(int mode) {
        if (mode == 2) {
            client.publish("SmartGarden/data",
                    Buffer.buffer("led_off"),
                    MqttQoS.AT_LEAST_ONCE,
                    false,
                    false);
        } else {
            client.publish("SmartGarden/data",
                    Buffer.buffer("led_on"),
                    MqttQoS.AT_LEAST_ONCE,
                    false,
                    false);
        }
    }
}
