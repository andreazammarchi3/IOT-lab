#include "ControllerTask.h"
#include <SoftwareSerial.h>

extern int led1;
extern int led2;
extern int led3;
extern int led4;
extern int irrigation;
extern int mode;

ServoTimer2 servo;

// Constructor
ControllerTask::ControllerTask() {
  pinMode(LED1_PIN, OUTPUT);
  pinMode(LED2_PIN, OUTPUT);
  pinMode(LED3_PIN, OUTPUT);
  pinMode(LED4_PIN, OUTPUT);
  pinMode(Rx, INPUT);
  pinMode(Tx, OUTPUT);
  servo.attach(SERVO_PIN);
}

// Initializer
void ControllerTask::init(int period) {
  Task::init(period);
  state = AUTO;
}

// Loop executed every ControllerTask period (100ms)
void ControllerTask::tick() {
  switch (state) {

    // AUTO state
    case AUTO:
      if (periodCounter == 0) {
        digitalWrite(LED1_PIN, 0);
        digitalWrite(LED2_PIN, 0);
        digitalWrite(LED3_PIN, 0);
        digitalWrite(LED4_PIN, 0);
        servo.write(750);
      } else {
        if (mode == 1) {
          state = MANUAL;
          periodCounter = 0;
          break;
        }
        // Update leds
        digitalWrite(LED1_PIN, led1);
        digitalWrite(LED2_PIN, led2);
        digitalWrite(LED3_PIN, 64 * led3);
        digitalWrite(LED4_PIN, 64 * led4);
      }
      periodCounter++;
      break;

    // MANUAL state: waiting for user input
    case MANUAL:
      // Update leds
      digitalWrite(LED1_PIN, led1);
      digitalWrite(LED2_PIN, led2);
      digitalWrite(LED3_PIN, 64 * led3);
      digitalWrite(LED4_PIN, 64 * led4);

      periodCounter++;
      break;

    // ALARM state
    case ALARM:

      periodCounter++;
      break;
  }
}
