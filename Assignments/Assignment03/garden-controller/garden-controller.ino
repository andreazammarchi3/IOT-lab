#include <SoftwareSerial.h>
#include <String.h>
#include "Scheduler.h"
#include "MsgTask.h"
#include "ControllerTask.h"
#include "MsgServiceBT.h"

#define SCHED_PERIOD 100

const int RxD = 2;
const int TxD = 3;

MsgServiceBT msgServiceBT(2,3);

Scheduler sched;

int LED1_PIN = 13;
int LED2_PIN = 12;
int LED3_PIN = 8;
int LED4_PIN = 7;

int SERVO_PIN = 5;

int led1 = 1;
int led2 = 1;
int led3 = 1;
int led4 = 1;
int irrigation = 1;

int mode = 0;

// Tasks period
int MSG_TASK_PERIOD = 100;
int CONTROLLER_TASK_PERIOD = 100;
 
void setup() 
{ 
  pinMode(RxD, INPUT);
  pinMode(TxD, OUTPUT);

  // Scheduler initialization, period = 100ms
  sched.init(SCHED_PERIOD);

  // MsgTask initialization, period = 100ms
  Task* t0 = new MsgTask();
  t0->init(MSG_TASK_PERIOD);
  sched.addTask(t0);

  // ControllerTask initialization, period = 100ms
  Task* t1 = new ControllerTask();
  t1->init(CONTROLLER_TASK_PERIOD);
  sched.addTask(t1);
} 
 
void loop() 
{ 
  sched.schedule();
}
