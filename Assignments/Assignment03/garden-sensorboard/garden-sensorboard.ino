#include <WiFiClientSecure.h>
#include <MQTT.h>

//variabili globali
int tempVal = 0;
float temp;
float volts;
float lightVal;

const int tempPin = 35;
const int ledPin = 23;
const int photoPin = 34;

const char ssid[] = "mrbanks";
const char pass[] = "mvepfgzw";

WiFiClientSecure net;
MQTTClient client(1024);

unsigned long lastMillis = 0;

void connect() {
  Serial.print("checking wifi...");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(1000);
  }

  Serial.print("\nconnecting...");
  while (!client.connect("bsidebotham", "try", "try")) {
    Serial.print(".");
    delay(1000);
  }

  Serial.println("\nconnected!");

  client.subscribe("/hello");
  // client.unsubscribe("/hello");
}

void messageReceived(String &topic, String &payload) {
  Serial.println("incoming: " + topic + " - " + payload);

  // Note: Do not use the client in the callback to publish, subscribe or
  // unsubscribe as it may cause deadlocks when other things arrive while
  // sending and receiving acknowledgments. Instead, change a global variable,
  // or push to a queue and handle it in the loop after calling `client.loop()`.
}

void setup()
{
  pinMode(tempPin, INPUT);
  pinMode(ledPin, OUTPUT);
  pinMode(photoPin, INPUT);
  
  digitalWrite(ledPin, HIGH);

  Serial.print("Temperature: ");
  Serial.println(getTemp());
  Serial.print("Light: ");
  Serial.println(getLight());

  Serial.begin(115200);
  WiFi.begin(ssid, pass);

  // Note: Local domain names (e.g. "Computer.local" on OSX) are not supported
  // by Arduino. You need to set the IP address directly.
  //
  // MQTT brokers usually use port 8883 for secure connections.
  client.begin("broker.shiftr.io", 8883, net);
  client.onMessage(messageReceived);

  connect();
}

void loop()
{ 
  client.loop();
  delay(10);  // <- fixes some issues with WiFi stability

  // publish a message roughly every second.
  if (millis() - lastMillis > 1000) {
    if (!client.connected()) {
      Serial.print("lastError: ");
      Serial.println(client.lastError());
      connect();
    }
    lastMillis = millis();
    client.publish("/hello", "world");
  }
}

float getTemp() {
  tempVal = analogRead(tempPin);
  temp = ((tempVal * (3.3 / 4095)) - 0.5) / 0.01;
  return temp;
}

int getLight() {
  lightVal = analogRead(photoPin);
  return lightVal;
}
