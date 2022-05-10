#include "MachineTask.h"

LiquidCrystal_I2C lcd(0x27,20,4);
ServoTimer2 servo;

extern int TEMP_PIN;
extern int POT_SUGAR;
extern int BTN_UP;
extern int BTN_DOWN;
extern int BTN_MAKE;
extern int SERVO_PIN;

extern int N_MAX_COFFEE;
extern int N_MAX_TEA;
extern int N_MAX_CHOCOLATE;

extern int MSG_TASK_PERIOD;
extern int MACHINE_TASK_PERIOD;

extern int T_MAKING;

extern String modality;
extern int nCoffee;
extern int nTea;
extern int nChocolate;
extern int selfTests;

bool choosing = false;
int selectingCounter = 0;
int sugarQuantity = 0;
int servoPosition;

MachineTask::MachineTask() {
    lcd.init();
    lcd.backlight();
    pinMode(BTN_UP, INPUT_PULLUP);
    pinMode(BTN_DOWN, INPUT_PULLUP);
    pinMode(BTN_MAKE, INPUT_PULLUP);
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
        } else if (periodCounter >= 20) {
            state = Ready;
            periodCounter = 0;
            break;
        }
        periodCounter++;
        break;
    
    case Ready:
        if (periodCounter == 0) {
            lcd.clear();
            lcd.print("Ready");
        } else {
            if (digitalRead(BTN_UP) || digitalRead(BTN_DOWN)) {
                state = Selecting;
                periodCounter = 0;
                break;
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
                sugarQuantity = map(analogRead(POT_SUGAR), 0, 1023, 0, 4);
                state = Making;
                periodCounter = 0;
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
            if (selectingCounter == 0) {
                lcd.print("Making a Coffee");
            } else if (selectingCounter == 1){
                lcd.print("Making a Tea");
            } else if (selectingCounter == 2){
                lcd.print("Making a Chocolate");
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
            state = Ready;
            periodCounter = 0;
            break;
        }
        periodCounter++;
        break;
    }
}
