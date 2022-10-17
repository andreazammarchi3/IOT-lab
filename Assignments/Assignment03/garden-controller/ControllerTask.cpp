#include "ControllerTask.h"
#include <SoftwareSerial.h>

extern bool onOffLights;
extern int fadeLights;
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
  pinMode(Rx, INPUT);
  pinMode(Tx, OUTPUT);
  servo.attach(SERVO_PIN);
  //SoftPWMBegin(SOFTPWM_NORMAL);
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
      } else if (periodCounter % 100 == 0) {  // Ogni mezzo secondo
        //digitalWrite(LED3_PIN, HIGH);
        //digitalWrite(LED4_PIN, HIGH);
        if (mode == 1) {
          state = MANUAL;
          periodCounter = 0;
          break;
        }
        // Update leds
        setOnOffLights(onOffLights);
      } else {
        //digitalWrite(LED3_PIN, LOW);
        //digitalWrite(LED4_PIN, LOW);
      }
      if (periodCounter % ((5 - irrigation) * 200) == 0) {
        if(irrigation != 0) {
          // Update servo
          updateServoPosition();
        }
        periodCounter = 0;
      }
      setFadeLights(fadeLights, periodCounter);

      periodCounter++;
      break;

    // MANUAL state: waiting for user input
    case MANUAL:

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

void ControllerTask::setFadeLights(int value, int periodCounter) {
  //SoftPWMSet(LED3_PIN, value);
  //SoftPWMSet(LED4_PIN, value);
  int dc = periodCounter % (value/2);
  if (dc == 0) {
    digitalWrite(LED3_PIN, HIGH);
    digitalWrite(LED4_PIN, HIGH);
  } else {
    digitalWrite(LED3_PIN, LOW);
    digitalWrite(LED4_PIN, LOW);
  }
}

void ControllerTask::updateServoPosition() {
  servoPosition = servoPosition + stepInc;
  servo.write(servoPosition);
  if (servoPosition == 2250 || servoPosition == 750) {
    stepInc = -1 * stepInc;
  }
}