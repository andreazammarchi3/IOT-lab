#include <LiquidCrystal.h>
#include "Timer.h"
#include "MsgService.h"

#define bUp 13
#define bDown 12
#define bMake 8

#define N_MAX_COFFEE 3
#define N_MAX_TEA 4
#define N_MAX_CHOCOLATE 5

LiquidCrystal lcd(7, 6, 5, 4, 3, 2);
Timer timer;

enum States {Boot, Ready, Selecting} state;

int tempPin = 0;
int potSugar = 1;
int nCoffee = 0;
int nTea = 0;
int nChocolate = 0;


void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  MsgService.init();
  lcd.begin(16, 2);
  state = Boot;
  timer.setupPeriod(10000);
}

void step() {
  switch (state) {
    case Boot:
      lcd.clear();
      lcd.print("Welcome to the:");
      lcd.setCursor(0,1);
      lcd.print("Coffee Machine!");
      nCoffee = N_MAX_COFFEE;
      nTea = N_MAX_TEA;
      nChocolate = N_MAX_CHOCOLATE;
      state = Ready;
      lcd.clear();
      lcd.print("");
      timer.setupPeriod(50);
      break;
      
    case Ready:
      if (MsgService.isMsgAvailable()){
        Msg* msg = MsgService.receiveMsg();    
        if (msg->getContent() == "modality"){
            MsgService.sendMsg("Idle");
         } else if (msg->getContent() == "Coffee"){
            MsgService.sendMsg("5"); 
         } else if (msg->getContent() == "Tea"){
            MsgService.sendMsg("6");   
         } else if (msg->getContent() == "Chocolate"){
            MsgService.sendMsg("7");  
         } 
        /* NOT TO FORGET: msg deallocation */
        delete msg;
      }
      break;
      
    case Selecting:
      
      break;
  }
}

void loop() {
  timer.waitForNextTick();
  step();
}

/*
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
*/

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
