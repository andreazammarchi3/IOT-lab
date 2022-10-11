#include <SoftwareSerial.h>
#include <String.h>
#include "Scheduler.h"
#include "MsgTask.h"
#include "ControllerTask.h"

#define SCHED_PERIOD 100

const int RxD = 2;
const int TxD = 3;

SoftwareSerial channel(RxD, TxD);

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

char command;
 
void setup() 
{ 
  pinMode(LED1_PIN, OUTPUT);
  pinMode(LED2_PIN, OUTPUT);
  pinMode(LED3_PIN, OUTPUT);
  pinMode(LED4_PIN, OUTPUT);
  pinMode(RxD, INPUT);
  pinMode(TxD, OUTPUT);
  pinMode(SERVO_PIN, OUTPUT);

  Serial.begin(19200);
  Serial.println("Type AT commands!");
  channel.begin(38400);
  //while (!channel){}

  /*
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
  */
} 
 
void loop() 
{ 
  /*
  digitalWrite(LED2_PIN, HIGH);
  // sched.schedule();
  if (channel.available()) 
  { 
    digitalWrite(LED1_PIN, HIGH);
    while(channel.available()){
      //leggo i valori ricevuti dal bluetooth
      command += (char)channel.read();
    }
    if (Serial.available()){
      delay(10); // The DELAY!
      channel.write(Serial.read());
    }
  }
  */
  if (channel.available()){
    Serial.print(char(channel.read()));
  }
}
