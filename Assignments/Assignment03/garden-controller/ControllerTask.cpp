#include "Arduino.h"
#include "ControllerTask.h"

extern int led1;
extern int led2;
extern int led3;
extern int led4;
extern int irrigation;
extern int mode;

ServoTimer2 servo;

int servoPosition = 750;
int stepInc;

// Constructor
ControllerTask::ControllerTask() {
  pinMode(LED1_PIN, OUTPUT);
  pinMode(LED2_PIN, OUTPUT);
  pinMode(LED3_PIN, OUTPUT);
  pinMode(LED4_PIN, OUTPUT);
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
        digitalWrite(LED1_PIN, led1);
        digitalWrite(LED2_PIN, led2);
      }
      if (periodCounter % ((5 - irrigation) * 200) == 0) {
        if(irrigation != 0) {
          // Update servo
          updateServoPosition();
        }
        periodCounter = 0;
      }
      setFadeLight(led3, periodCounter, LED3_PIN);
      setFadeLight(led4, periodCounter, LED4_PIN);

      periodCounter++;
      break;

    // MANUAL state: waiting for user input
    case MANUAL:
      if (periodCounter == 0) {
        digitalWrite(LED1_PIN, 0);
        digitalWrite(LED2_PIN, 0);
        digitalWrite(LED3_PIN, 0);
        digitalWrite(LED4_PIN, 0);
        servoPosition = 750;
        servo.write(servoPosition);
      }
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

        digitalWrite(LED1_PIN, led1);
        digitalWrite(LED2_PIN, led2);
      }

      if (periodCounter % ((5 - irrigation) * 200) == 0) {
        if(irrigation != 0) {
          // Update servo
          updateServoPosition();
        }
        periodCounter = 0;
      }

      setFadeLight(led3, periodCounter, LED3_PIN);
      setFadeLight(led4, periodCounter, LED4_PIN);

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
        digitalWrite(LED1_PIN, LOW);
        digitalWrite(LED2_PIN, LOW);
        digitalWrite(LED3_PIN, LOW);
        digitalWrite(LED4_PIN, LOW);
        servo.write(750);
        servoPosition = 750;
      }

      periodCounter++;
      break;
  }
}

void ControllerTask::setFadeLight(int value, int periodCounter, int led) {
  if (value != 0) {
    int dc = periodCounter % ((5-value) * 5);
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