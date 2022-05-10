#include <String.h>
#include "MsgService.h"
#include "Scheduler.h"
#include "MsgTask.h"
#include "MachineTask.h"

Scheduler sched;

enum States {Boot, Ready, Selecting} state;

// Analog pins
int TEMP_PIN = 0;
int POT_SUGAR = 1;

// Digital pins
int PR_PIN = 7;
int BTN_MAKE = 8;
int SERVO_PIN = 9;
int ECHO_PIN = 10;
int TRIG_PIN = 11;
int BTN_DOWN = 12;
int BTN_UP = 13;

int N_MAX_COFFEE = 1;
int N_MAX_TEA = 4;
int N_MAX_CHOCOLATE = 5;

int MSG_TASK_PERIOD = 200;
int MACHINE_TASK_PERIOD = 100;

int T_MAKING = 10000;
int T_TIMEOUT = 5000;
int T_IDLE = 30000;

String modality = "working"; 
int nCoffee = N_MAX_COFFEE;
int nTea = N_MAX_TEA;
int nChocolate = N_MAX_CHOCOLATE;
int selfTests = 0;

bool refill = false;

void setup() {
  // put your setup code here, to run once:
  sched.init(MACHINE_TASK_PERIOD);

  Task* t0 = new MsgTask();
  t0->init(MSG_TASK_PERIOD);
  sched.addTask(t0);

  Task* t1 = new MachineTask();
  t1->init(MACHINE_TASK_PERIOD);
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
