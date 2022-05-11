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
extern int T_CHECK;

extern int TEMP_MIN;
extern int TEMP_MAX;

extern String modality;
extern int nCoffee;
extern int nTea;
extern int nChocolate;
extern int selfTests;

extern bool refill;
extern bool recover;

LiquidCrystal_I2C lcd(0x27,20,4);
ServoTimer2 servo;
Ultrasonic ultrasonic(TRIG_PIN, ECHO_PIN);

MachineTask::MachineTask() {
    lcd.init();
    lcd.backlight();
    pinMode(BTN_UP, INPUT_PULLUP);
    pinMode(BTN_DOWN, INPUT_PULLUP);
    pinMode(BTN_MAKE, INPUT_PULLUP);
    pinMode(PR_PIN, INPUT_PULLUP);
    servo.attach(SERVO_PIN);
}

void MachineTask::init(int period) {
    Task::init(period);
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
        if (testPeriodCounter >= T_CHECK / MACHINE_TASK_PERIOD) {
            selfTests++;
            state = Testing;
            lcd.clear();
            lcd.print("testing");
            testPeriodCounter = 0;
            periodCounter = 0;
            break;
        }
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
        testPeriodCounter++;
        periodCounter++;
        break;

    case Selecting:
        if (testPeriodCounter >= T_CHECK / MACHINE_TASK_PERIOD) {
            selfTests++;
            state = Testing;
            lcd.clear();
            lcd.print("testing");
            testPeriodCounter = 0;
            periodCounter = 0;
            break;
        }
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
        testPeriodCounter++;
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
            lcd.setCursor(0,0);
            if (selectingCounter == 0) {
                lcd.print("The Coffee");
            } else if (selectingCounter == 1){
                lcd.print("The Tea");
            } else if (selectingCounter == 2){
                lcd.print("The Chocolate");
            }
            lcd.setCursor(0,1);
            lcd.print("is ready");
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
        } else if (recover == true) {
            servo.write(750);
            modality = "working";
            recover = false;
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
        if (periodCounter <= 10) {
            modality = "idle";
            lcd.clear();
            lcd.print("idle");
        } else if (periodCounter >= 10){
          lcd.clear();
          lcd.noBacklight();
          attachInterrupt(0, wakeUp, RISING);
          set_sleep_mode(SLEEP_MODE_PWR_DOWN);
          sleep_enable();
          sleep_mode();
          
          // Executed after the interrupt
          sleep_disable();
          detachInterrupt(0);
          lcd.backlight();
          state = Ready;
          modality = "working";
          periodCounter = 0;
          break;
        }
        periodCounter++;
        break;

    case Testing:
        int reading = analogRead(TEMP_PIN);  
        float voltage = reading * 5.0;
        voltage /= 1024.0; 
        int t = (voltage - 0.5) * 100;
        lcd.clear();
        lcd.print("testing");
        if(t < TEMP_MIN || t > TEMP_MAX) {
            state = Assistance;
            periodCounter = 0;
            break;
        }
        
        if (periodCounter <= 20) {
            servo.write(map(periodCounter, 0, 20, 750, 2250));
        } else if (periodCounter >= 21 && periodCounter <= 40) {
            servo.write(map(periodCounter, 21, 40, 2250, 750));
        } else if (periodCounter >= 41) {
            state = Ready;
            periodCounter = 0;
            break;
        }
          
        periodCounter++;
        break;
    }
}

static void MachineTask::wakeUp() {
    sleep_disable();
    detachInterrupt(0);
}
