#include "ControllerTask.h"

// Importing external vars declared in the .ino file

extern int LED1_PIN;
extern int LED2_PIN;
extern int LED3_PIN;
extern int LED4_PIN;
extern int SERVO_PIN;

extern int MSG_TASK_PERIOD;
extern int CONTROLLER_TASK_PERIOD;

ServoTimer2 servo;

// Constructor
ControllerTask::ControllerTask() {
    lcd.init();
    lcd.backlight();
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
}

// Loop executed every ControllerTask period (100ms)
void ControllerTask::tick() {
    switch (state) {
      
        // AUTO state
        case AUTO:
            if (periodCounter == 0) {
                
            } else if (periodCounter >= 20) {
                
            }
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
