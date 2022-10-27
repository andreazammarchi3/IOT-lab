#include <SoftwareSerial.h>
#include <String.h>
#include "Scheduler.h"
#include "MsgTask.h"
#include "ControllerTask.h"
#include "define.h"

Scheduler sched;

int irrigation = 0;
int mode = 0;
int led1 = 0;
int led2 = 0;
int led3 = 0;
int led4 = 0;

void setup()
{
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
