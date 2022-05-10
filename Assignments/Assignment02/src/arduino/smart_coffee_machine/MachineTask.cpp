#include "MachineTask.h"

LiquidCrystal_I2C lcd(0x27,20,4);

MachineTask::MachineTask() {
    lcd.init();
    lcd.backlight();
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
            lcd.print("Welcome!");
            /*
            this->lcd.clear();
            this->lcd.setCursor(1,0);
            this->lcd.print("Welcome to the:");
            this->lcd.setCursor(1,1);
            this->lcd.print("Coffee Machine!");
            */
        } else if (periodCounter >= 100) {
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
        }
        periodCounter++;
        break;
    }
}
