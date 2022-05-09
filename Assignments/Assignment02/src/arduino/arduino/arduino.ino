#include <LiquidCrystal.h>

#define bUp 13
#define bDown 12
#define bMake 8
#define trigPin 10
#define echoPin 11

LiquidCrystal lcd(7, 6, 5, 4, 3, 2);

int tempPin = 0;
int potSugar = 1;
char appState = 0;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  lcd.begin(16, 2);
  appState = 0;
}

void loop() {
  switch (appState) {
    case 1:
      lcd.clear();
      getTemperature();
      delay(1000);
      break;
    default:
      lcd.clear();
      lcd.print("Welcome to the:");
      lcd.setCursor(0,1);
      lcd.print("Coffee Machine!");
      delay(10000);
      appState = 1;
      break;
  }
  
}

int getDistance() {
  float durata;
  int cm;
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  durata = pulseIn(echoPin, HIGH);
  cm = durata / 58;
}

int getTemperature() {
  int reading = analogRead(tempPin);  
  float voltage = reading * 5.0;
  voltage /= 1024.0; 
  int temperatureC = (voltage - 0.5) * 100 ; 
  lcd.print(temperatureC);
  lcd.print(" ");
  lcd.print((char)223);
  lcd.print("C");
  return temperatureC;
}
