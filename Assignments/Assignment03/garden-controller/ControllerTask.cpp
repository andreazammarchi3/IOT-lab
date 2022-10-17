#include "ControllerTask.h"
#include <SoftwareSerial.h>

extern bool onOffLights;
extern int fadeLights;
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
  SoftPWMBegin(SOFTPWM_NORMAL);
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
        servo.write(750);
      } else if (periodCounter % 10 == 0) {
        if (mode == 1) {
          state = MANUAL;
          periodCounter = 0;
          break;
        }
        // Update leds
        setOnOffLights(onOffLights);
        setFadeLights(fadeLights);
      }
      periodCounter++;
      break;

    // MANUAL state: waiting for user input
    case MANUAL:
      // Update leds
      setOnOffLights(onOffLights);
      setFadeLights(fadeLights);

      periodCounter++;
      break;

    // ALARM state
    case ALARM:

      periodCounter++;
      break;
  }
}

void ControllerTask::setOnOffLights(bool value) {
  if (value) {
    digitalWrite(LED1_PIN, HIGH);
    digitalWrite(LED2_PIN, HIGH);
  } else {
    digitalWrite(LED1_PIN, LOW);
    digitalWrite(LED2_PIN, LOW);
  }
}

void ControllerTask::setFadeLights(int value) {
  SoftPWMSet(LED3_PIN, value);
  SoftPWMSet(LED4_PIN, value);
}
