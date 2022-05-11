/*
 *  Assignment #02 - Smart Coffee Machine
 * 
 *  Author: andrea.zammarchi3@studio.unibo.it - 0000914652
 * 
 *  Link repo GitHub:
 *  https://github.com/andreazammarchi3/IOT-lab/tree/main/Assignments/Assignment02
 */

#include <String.h>
#include "Scheduler.h"
#include "MsgTask.h"
#include "MachineTask.h"

#define SCHE_PERIOD 100

/*  For a better user experience, all of the vars
 *  are delcared here...
 */

Scheduler sched;

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

// Products max availability
int N_MAX_COFFEE = 1;
int N_MAX_TEA = 4;
int N_MAX_CHOCOLATE = 5;

// Tasks period
int MSG_TASK_PERIOD = 100;
int MACHINE_TASK_PERIOD = 100;

// Some times deadline
int T_MAKING = 10000;
int T_TIMEOUT = 5000;
int T_IDLE = 5000;
int T_CHECK = 20000;

// Temperature range
int TEMP_MIN = 17;
int TEMP_MAX = 28;

// Current machine modality
String modality = "working";

// Products availability
int nCoffee = N_MAX_COFFEE;
int nTea = N_MAX_TEA;
int nChocolate = N_MAX_CHOCOLATE;

// Number of self tests made
int selfTests = 0;

// Bools for pc serial communication
bool refill = false;
bool recover = false;

// SETUP
void setup() {
  // Scheduler initialization, period = 100ms
  sched.init(SCHED_PERIOD);

  // MsgTask initialization, period = 100ms
  Task* t0 = new MsgTask();
  t0->init(MSG_TASK_PERIOD);
  sched.addTask(t0);

  // MachineTask initialization, period = 100ms
  Task* t1 = new MachineTask();
  t1->init(MACHINE_TASK_PERIOD);
  sched.addTask(t1);
}

// INFINITE LOOP
void loop() {
  sched.schedule();
}
