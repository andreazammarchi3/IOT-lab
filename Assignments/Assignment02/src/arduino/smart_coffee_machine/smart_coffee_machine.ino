#include <String.h>
#include "Timer.h"
#include "MsgService.h"
#include "Scheduler.h"
#include "MsgTask.h"
#include "MachineTask.h"

Timer timer;
Scheduler sched;

enum States {Boot, Ready, Selecting} state;

int tempPin = 0;
int potSugar = 1;
int bUp = 13;
int bDown = 12;
int bMake = 8;

static int N_MAX_COFFEE = 3;
static int N_MAX_TEA = 4;
static int N_MAX_CHOCOLATE = 5;

String modality = "Idle"; 
int nCoffee = N_MAX_COFFEE;
int nTea = N_MAX_TEA;
int nChocolate = N_MAX_CHOCOLATE;
int selfTests = 0;

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
