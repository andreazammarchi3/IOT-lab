#include "Timer.h"
#include "MsgService.h"
#include "Scheduler.h"
#include "MsgTask.h"
#include "MachineTask.h"

#define bUp 13
#define bDown 12
#define bMake 8

#define N_MAX_COFFEE 3
#define N_MAX_TEA 4
#define N_MAX_CHOCOLATE 5

Timer timer;
Scheduler sched;

enum States {Boot, Ready, Selecting} state;

int tempPin = 0;
int potSugar = 1;

char modality[] = "Idle"; 
int nCoffee = 0;
int nTea = 0;
int nChocolate = 0;
int selfTests = 0;

int timerPeriod = 50;
int periodCounter = 0;


void setup() {
  // put your setup code here, to run once:
  sched.init(50);

  Task* t0 = new MsgTask();
  t0->init(500);
  sched.addTask(t0);

  Task* t1 = new MachineTask();
  t1->init(50);
  sched.addTask(t1);
}

void loop() {
  sched.schedule();
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

/*
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
*/
