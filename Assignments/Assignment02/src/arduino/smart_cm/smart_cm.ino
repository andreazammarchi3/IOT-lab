#include <String.h>
#include "Scheduler.h"
#include "MsgTask.h"
#include "MachineTask.h"

Scheduler sched;

enum States {Boot, Ready, Selecting} state;

// Analog pins
int TEMP_PIN = 0;
int POT_SUGAR = 1;

// Digital pins
int PR_PIN = 2;
int BTN_MAKE = 8;
int SERVO_PIN = 9;
int ECHO_PIN = 10;
int TRIG_PIN = 11;
int BTN_DOWN = 12;
int BTN_UP = 13;

int N_MAX_COFFEE = 1;
int N_MAX_TEA = 4;
int N_MAX_CHOCOLATE = 5;

int MSG_TASK_PERIOD = 100;
int MACHINE_TASK_PERIOD = 100;

int T_MAKING = 10000;
int T_TIMEOUT = 5000;
int T_IDLE = 5000;
int T_CHECK = 20000;

int TEMP_MIN = 17;
int TEMP_MAX = 28;

String modality = "working"; 
int nCoffee = N_MAX_COFFEE;
int nTea = N_MAX_TEA;
int nChocolate = N_MAX_CHOCOLATE;
int selfTests = 0;

bool refill = false;
bool recover = false;

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
