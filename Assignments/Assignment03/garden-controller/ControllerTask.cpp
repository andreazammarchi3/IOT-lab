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
                digitalWrite(LED1_PIN, led1);
                digitalWrite(LED2_PIN, led2);
                digitalWrite(LED3_PIN, led3);
                digitalWrite(LED4_PIN, led4);
                servo.write(750);
            } else {
                if (mode == 1) {
                  state = MANUAL;
                  periodCounter = 0;
                  break;
                }
            }
            periodCounter++;
            break;

        // MANUAL state: waiting for user input
        case MANUAL:
            if (led1 == 1) {
              digitalWrite(LED1_PIN, HIGH);
            } else if (led1 == 0) {
              digitalWrite(LED1_PIN, LOW);
            }
            periodCounter++;
            break;

        // ALARM state
        case ALARM:
            
            periodCounter++;
            break;
    }
}
