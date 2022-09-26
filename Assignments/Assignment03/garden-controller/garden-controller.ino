#include <SoftwareSerial.h>
#include <String.h>
#include "Scheduler.h"
#include "MsgTask.h"
#include "ControllerTask.h"

#define SCHED_PERIOD 1000

const int RxD = 2;
const int TxD = 3;

SoftwareSerial HC05(RxD,TxD); // RX, TX

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

// Tasks period
int MSG_TASK_PERIOD = 1000;
int CONTROLLER_TASK_PERIOD = 1000;
 
void setup() 
{ 
  pinMode(RxD, INPUT);
  pinMode(TxD, OUTPUT);

  // HC05.begin(9600);

  // Scheduler initialization, period = 1000ms
  sched.init(SCHED_PERIOD);

  // MsgTask initialization, period = 1000ms
  Task* t0 = new MsgTask();
  t0->init(MSG_TASK_PERIOD);
  sched.addTask(t0);

  // ControllerTask initialization, period = 1000ms
  Task* t1 = new ControllerTask();
  t1->init(CONTROLLER_TASK_PERIOD);
  sched.addTask(t1);
} 
 
void loop() 
{ 
  /*
  HC05.print("bluetooth connected!\n");
  delay(2000);
  HC05.flush();
  */
  sched.schedule();
}
