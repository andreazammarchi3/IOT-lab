#include "MachineTask.h"

extern int PR_PIN;
extern int TEMP_PIN;
extern int POT_SUGAR;
extern int BTN_UP;
extern int BTN_DOWN;
extern int BTN_MAKE;
extern int SERVO_PIN;
extern int ECHO_PIN;
extern int TRIG_PIN;

extern int N_MAX_COFFEE;
extern int N_MAX_TEA;
extern int N_MAX_CHOCOLATE;

extern int MSG_TASK_PERIOD;
extern int MACHINE_TASK_PERIOD;

extern int T_MAKING;
extern int T_TIMEOUT;
extern int T_IDLE;

extern String modality;
extern int nCoffee;
extern int nTea;
extern int nChocolate;
extern int selfTests;

extern bool refill;

bool choosing = false;
int selectingCounter = 0;
int sugarQuantity = 0;
int servoPosition;

LiquidCrystal_I2C lcd(0x27,20,4);
ServoTimer2 servo;
Ultrasonic ultrasonic(TRIG_PIN, ECHO_PIN);

MachineTask::MachineTask() {
    lcd.init();
    lcd.backlight();
    pinMode(BTN_UP, INPUT_PULLUP);
    pinMode(BTN_DOWN, INPUT_PULLUP);
    pinMode(BTN_MAKE, INPUT_PULLUP);
    pinMode(PR_PIN, INPUT);
    servo.attach(SERVO_PIN);
}

void MachineTask::init(int period) {
    Task::init(period);
    this->periodCounter = 0;
    state = Boot;
}

void MachineTask::tick() {
    switch (state) {
    case Boot:
        if (periodCounter == 0) {
            lcd.clear();
            lcd.setCursor(0,0);
            lcd.print("Welcome to the:");
            lcd.setCursor(0,1);
            lcd.print("Coffee Machine!");
            servo.write(750);
        } else if (periodCounter >= 20) {
            state = Ready;
            periodCounter = 0;
            break;
        }
        periodCounter++;
        break;
    
    case Ready:
        if (periodCounter == 0) {
            modality = "working";
            lcd.clear();
            lcd.print("Ready");
        } else if (periodCounter >= T_IDLE / MACHINE_TASK_PERIOD) {
            modality = "idle";
            state = Idle;
            periodCounter = 0;
            break;
        } else {
            if (digitalRead(BTN_UP) || digitalRead(BTN_DOWN)) {
                state = Selecting;
                periodCounter = 1;
                break;
            } else {
                if(digitalRead(PR_PIN) == HIGH) {
                    periodCounter = 0;
                }
            }
        }
        periodCounter++;
        break;

    case Selecting:
        if (periodCounter != 50) {
            if (choosing == false) {
                if (digitalRead(BTN_UP)) {
                    choosing = true;
                    periodCounter = 0;
                    selectingCounter++;
                    if (selectingCounter == 3) {
                        selectingCounter = 0;
                    }
                } else if (digitalRead(BTN_DOWN)) {
                    choosing = true;
                    periodCounter = 0;
                    if (selectingCounter == 0) {
                        selectingCounter = 3;
                    }
                    selectingCounter--;
                }
            } else if (!digitalRead(BTN_UP) && !digitalRead(BTN_DOWN)){
                choosing = false;
            }
            
            if (selectingCounter == 0) {
                lcd.clear();
                lcd.print("Coffee");
            } else if (selectingCounter == 1){
                lcd.clear();
                lcd.print("Tea");
            } else if (selectingCounter == 2){
                lcd.clear();
                lcd.print("Chocolate");
            }
            
            if (digitalRead(BTN_MAKE)) {
                if (selectingCounter == 0) {
                    if (nCoffee != 0) {
                        nCoffee--;
                    } else {
                        state = Ready;
                        periodCounter = 0;
                        lcd.clear();
                        lcd.setCursor(0,0);
                        lcd.print("Product not");
                        lcd.setCursor(0,1);
                        lcd.print("available.");
                        break;
                    }
                } else if (selectingCounter == 1){
                    if (nTea != 0) {
                        nTea--;
                    } else {
                        state = Ready;
                        periodCounter = 0;
                        lcd.clear();
                        lcd.setCursor(0,0);
                        lcd.print("Product not");
                        lcd.setCursor(0,1);
                        lcd.print("available.");
                        break;
                    }
                } else if (selectingCounter == 2){
                    if (nChocolate != 0) {
                        nChocolate--;
                    } else {
                        state = Ready;
                        periodCounter = 0;
                        lcd.clear();
                        lcd.setCursor(0,0);
                        lcd.print("Product not");
                        lcd.setCursor(0,1);
                        lcd.print("available.");
                        break;
                    }
                }
                sugarQuantity = map(analogRead(POT_SUGAR), 0, 1023, 0, 4);
                state = Making;
                periodCounter = 0;
                break;
            }
        } else if (periodCounter == 50){
            state = Ready;
            periodCounter = 0;
            break;
        }
        periodCounter++;
        break;

    case Making:
        if (periodCounter != T_MAKING / MACHINE_TASK_PERIOD) {
            lcd.clear();
            lcd.setCursor(0,0);
            lcd.print("Making a");
            lcd.setCursor(0,1);
            if (selectingCounter == 0) {
                lcd.print("Coffee.");
            } else if (selectingCounter == 1){
                lcd.print("Tea.");
            } else if (selectingCounter == 2){
                lcd.print("Chocolate.");
            }
            servo.write(map(periodCounter, 0, T_MAKING / MACHINE_TASK_PERIOD, 750, 2250));
        } else {
            lcd.clear();
            if (selectingCounter == 0) {
                lcd.print("The Coffee is ready");
            } else if (selectingCounter == 1){
                lcd.print("The Tea is ready");
            } else if (selectingCounter == 2){
                lcd.print("The Chocolate is ready");
            }
            selectingCounter = 0;
            state = WaitRemove;
            periodCounter = 0;
            break;
        }
        periodCounter++;
        break;

    case Assistance:
        if (periodCounter == 0) {
            lcd.clear();
            lcd.print("Assistance");
            lcd.setCursor(0,1);
            lcd.print("required");
            lcd.setCursor(0,0);
            modality = "assistance";
        } else if (refill == true) {
            modality = "working";
            nCoffee = N_MAX_COFFEE;
            nTea = N_MAX_TEA;
            nChocolate = N_MAX_CHOCOLATE;
            refill = false;
            state = Ready;
            periodCounter = 0;
            break;
        }
        periodCounter++;
        break;

    case WaitRemove:
        if (periodCounter == 0) {
            if (nCoffee == 0 && nTea == 0 && nChocolate == 0) {
                state = Assistance;
                periodCounter = 0;
                break;
            }
        } else if (periodCounter >= T_TIMEOUT / MACHINE_TASK_PERIOD || ultrasonic.distanceRead() >= 40) {
            state = Ready;
            periodCounter = 0;
            servo.write(750);
            break;
        }
        periodCounter++;
        break;

    case Idle:
        if (periodCounter == 0) {
            lcd.clear();
            lcd.print("idle");
        }
        periodCounter++;
        break;
    }
}
