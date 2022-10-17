#include <WiFi.h>
#include <PubSubClient.h>
#include <String.h>
#define MSG_BUFFER_SIZE  50

//variabili globali
int tempVal = 0;
float temp;
float volts;
float lightVal;

const int tempPin = 35;
const int ledPin = 23;
const int photoPin = 34;

/* wifi network info */

const char* ssid = "AIR2";
const char* password = "giovanniboss";

/* MQTT server address */
const char* mqtt_server = "broker.mqtt-dashboard.com";

/* MQTT client management */

WiFiClient espClient;
PubSubClient client(espClient);

unsigned long lastMsgTime = 0;
char msg[MSG_BUFFER_SIZE];
int value = 0;

void setup_wifi() {

  delay(10);

  Serial.println(String("Connecting to ") + ssid);

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

/* MQTT subscribing callback */

void callback(char* topic, byte* message, unsigned int length) {
  Serial.print("Message arrived on topic: ");
  Serial.print(topic);
  Serial.print(". Message: ");
  String messageTemp;
  
  for (int i = 0; i < length; i++) {
    Serial.print((char)message[i]);
    messageTemp += (char)message[i];
  }
  Serial.println();
  // If a message is received on the topic, you check if the message is either "on" or "off". 
  // Changes the output state according to the message
  if (String(topic) == "SmartGarden/data") {
    if(messageTemp == "led_ON"){
      Serial.println("led_ON");
      digitalWrite(ledPin, HIGH);
    }
    else if(messageTemp == "led_OFF"){
      Serial.println("led_OFF");
      digitalWrite(ledPin, LOW);
    }
  }
}

void reconnect() {
  
  // Loop until we're reconnected
  
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    
    // Create a random client ID
    String clientId = String("SmartGarden-client-")+String(random(0xffff), HEX);

    // Attempt to connect
    if (client.connect(clientId.c_str())) {
      Serial.println("connected");
      // Once connected, publish an announcement...
      // client.publish("outTopic", "hello world");
      // ... and resubscribe
      client.subscribe("SmartGarden/data");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}

int getTemp() {
  tempVal = analogRead(tempPin);
  Serial.println(tempVal);
  tempVal = map(tempVal, 0, 4096, 1, 5);
  tempVal = int(tempVal);
  return tempVal;
}

int getLight() {
  lightVal = analogRead(photoPin);
  lightVal = map(lightVal, 0, 4096, 1, 8);
  lightVal = int(lightVal);
  return lightVal;
}

void setup()
{
  pinMode(tempPin, INPUT);
  pinMode(ledPin, OUTPUT);
  pinMode(photoPin, INPUT);
  
  digitalWrite(ledPin, HIGH);

  Serial.begin(115200);
  delay(2000);
  setup_wifi();
  randomSeed(micros());
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);
}

void loop()
{ 
  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  unsigned long now = millis();
  if (now - lastMsgTime > 10000) {
    lastMsgTime = now;
    value++;

    /* Publishing the temperature */
    char tempString[2];
    dtostrf(getTemp(), 1, 0, tempString);
    char lightString[2];
    dtostrf(getLight(), 1, 0, lightString);
    strcpy(msg, tempString);
    strcat(msg, ", ");
    strcat(msg, lightString);

    client.publish("SmartGarden/data", msg);
  }
}
