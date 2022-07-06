#include <WiFi.h>
#include <HTTPClient.h>

//variabili globali
int tempVal = 0;
float temp;
float volts;
float lightVal;

const int tempPin = 35;
const int ledPin = 23;
const int photoPin = 34;

const char* ssid = "iPhone di Andrea";
const char* password =  "infondoalmar";

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
  delay(4000);
  WiFi.begin(ssid, password);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting to WiFi..");
  }
  
  Serial.println("Connected to the WiFi network");
}

void loop()
{ 
  if ((WiFi.status() == WL_CONNECTED)) { //Check the current connection status
  
    HTTPClient http;
  
    http.begin("http://127.0.0.1:8080"); //Specify the URL
    int httpCode = http.GET();                                        //Make the request
  
    if (httpCode > 0) { //Check for the returning code
  
        String payload = http.getString();
        Serial.println(httpCode);
        Serial.println(payload);
      }
  
    else {
      Serial.println("Error on HTTP request");
    }
  
    http.end(); //Free the resources
  }

  delay(10000);
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
