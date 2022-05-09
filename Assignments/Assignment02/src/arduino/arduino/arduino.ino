#include "Timer.h"
#include <LiquidCrystal.h>

#define bUp 13
#define bDown 12
#define bMake 8

LiquidCrystal lcd(7, 6, 5, 4, 3, 2);

int tempPin = 0;
int potSugar = 1;
Timer timer;

enum States {Boot, Ready} state;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  lcd.begin(16, 2);
  state = Boot;
  timer.setupPeriod(5000);
}

void step() {
  switch (state) {
    case Boot:
      lcd.clear();
      lcd.print("Welcome to the:");
      lcd.setCursor(0,1);
      lcd.print("Coffee Machine!");
      state = Ready;
      break;
    case Ready:
      lcd.clear();
      getTemperature();
      break;
  }
}

void loop() {
  timer.waitForNextTick();
  step();
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
