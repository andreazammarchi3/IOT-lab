#include <SoftwareSerial.h>
#include <String.h>
#include "Scheduler.h"
#include "MsgTask.h"
#include "MachineTask.h"

#define SCHED_PERIOD 100

const int RxD = 2;
const int TxD = 3;

SoftwareSerial HC05(RxD,TxD); // RX, TX

Scheduler sched;

// Tasks period
int MSG_TASK_PERIOD = 100;
 
void setup() 
{ 
  pinMode(RxD, INPUT);
  pinMode(TxD, OUTPUT);

  // HC05.begin(9600);

  // Scheduler initialization, period = 100ms
  sched.init(SCHED_PERIOD);

  // MsgTask initialization, period = 100ms
  Task* t0 = new MsgTask();
  t0->init(MSG_TASK_PERIOD);
  sched.addTask(t0);
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
