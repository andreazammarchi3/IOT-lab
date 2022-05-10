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
            lcd.setCursor(0,0);
            lcd.print("Welcome to the:");
            lcd.setCursor(0,1);
            lcd.print("Coffee Machine!");
        } else if (periodCounter >= 40) {
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
          
        }
        periodCounter++;
        break;
    }
}
