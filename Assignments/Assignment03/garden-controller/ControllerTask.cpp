#include "Arduino.h"
#include "ControllerTask.h"
#include <SoftwareSerial.h>

extern bool onOffLights;
extern int fadeLights;
extern int irrigation;
extern int mode;

extern int led1BT;
extern int led2BT;
extern int led3BT;
extern int led4BT;
extern int irrigationBT;

ServoTimer2 servo;

int servoPosition = 750;
int stepInc;

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
  stepInc = (2250 - 750) / CONTROLLER_TASK_PERIOD;
}

// Loop executed every ControllerTask period (50ms)
void ControllerTask::tick() {
  switch (state) {

    // AUTO state
    case AUTO:
      if (periodCounter == 0) {
        servo.write(servoPosition);
      } else if (periodCounter % 100 == 0) {
        if (mode == 1) {
          state = MANUAL;
          periodCounter = 0;
          break;
        } else if (mode == 2) {
          state = ALARM;
          periodCounter = 0;
          break;
        }
        // Update leds
        setOnOffLights(onOffLights);
      }
      if (periodCounter % ((5 - irrigation) * 200) == 0) {
        if(irrigation != 0) {
          // Update servo
          updateServoPosition();
        }
        periodCounter = 0;
      }
      setFadeLight(fadeLights, periodCounter, LED3_PIN);
      setFadeLight(fadeLights, periodCounter, LED4_PIN);

      periodCounter++;
      break;

    // MANUAL state: waiting for user input
    case MANUAL:
      if (periodCounter % 100 == 0) {
        if (mode == 0) {
          state = AUTO;
          periodCounter = 0;
          break;
        } else if (mode == 2) {
          state = ALARM;
          periodCounter = 0;
          break;
        }

        digitalWrite(LED1_PIN, led1BT);
        digitalWrite(LED2_PIN, led2BT);
      }

      if (periodCounter % ((5 - irrigationBT) * 200) == 0) {
        if(irrigationBT != 0) {
          // Update servo
          updateServoPosition();
        }
        periodCounter = 0;
      }

      setFadeLight(led3BT, periodCounter, LED3_PIN);
      setFadeLight(led4BT, periodCounter, LED4_PIN);

      periodCounter++;
      break;

    // ALARM state
    case ALARM:
      if (periodCounter % 100 == 0) {
        if (mode == 0) {
          state = AUTO;
          periodCounter = 0;
          break;
        } else if (mode == 1) {
          state = MANUAL;
          periodCounter = 0;
          break;
        }
      } else {
        
      }

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

void ControllerTask::setFadeLight(int value, int periodCounter, int led) {
  if (fadeLights != 0) {
    int dc = periodCounter % (value);
    if (dc == 0) {
      digitalWrite(led, HIGH);
    } else {
      digitalWrite(led, LOW);
    }
  }
}

void ControllerTask::updateServoPosition() {
  servoPosition = servoPosition + stepInc;
  servo.write(servoPosition);
  if (servoPosition == 2250 || servoPosition == 750) {
    stepInc = -1 * stepInc;
  }
}