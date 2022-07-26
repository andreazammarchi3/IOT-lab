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

const char *serverPath = "https://3419-37-160-47-90.eu.ngrok.io";

void connectToWifi(const char* ssid, const char* password){
  WiFi.begin(ssid, password);
  Serial.println("Connecting");
  while(WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected to WiFi network with IP Address: ");
  Serial.println(WiFi.localIP());
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
  connectToWifi(ssid, password);
}

void loop()
{ 
  if(WiFi.status()== WL_CONNECTED){      
    HTTPClient http;
  
    // Your Domain name with URL path or IP address with path
    http.begin(serverPath);
      
    // Send HTTP GET request
    int httpResponseCode = http.GET();
      
    if (httpResponseCode>0) {
      Serial.print("HTTP Response code: ");
      Serial.println(httpResponseCode);
      String payload = http.getString();
      Serial.println(payload);
    } else {
      Serial.print("Error code: ");
      Serial.println(httpResponseCode);
    }
    
    // Free resources
    http.end();
    }
   delay(2000);
}
