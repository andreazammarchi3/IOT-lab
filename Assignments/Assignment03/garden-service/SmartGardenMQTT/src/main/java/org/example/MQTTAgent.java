package org.example;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;

/*
 * MQTT Agent
 */
public class MQTTAgent extends AbstractVerticle {

    public MQTTAgent() {
    }

    @Override
    public void start() {
        MqttClient client = MqttClient.create(vertx);

        client.connect(1883, "broker.mqtt-dashboard.com", c -> {

            log("connected");

            log("subscribing...");
            client.publishHandler(s -> {
                        System.out.println(s.topicName() + ": " + s.payload().toString());
                    })
                    .subscribe("SmartGarden/temperature", 2);

            client.publishHandler(s -> {
                        System.out.println(s.topicName() + ": " + s.payload().toString());
                    })
                    .subscribe("SmartGarden/luminosity", 2);

            log("publishing a msg");
            client.publish("SmartGarden/output",
                    Buffer.buffer("hello"),
                    MqttQoS.AT_LEAST_ONCE,
                    false,
                    false);
        });
    }


    private void log(String msg) {
        System.out.println("[DATA SERVICE] "+msg);
    }

}
