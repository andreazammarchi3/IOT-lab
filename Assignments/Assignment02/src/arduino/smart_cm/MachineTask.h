#ifndef __MACHINETASK__
#define __MACHINETASK__

#include <Arduino.h>
#include <LiquidCrystal_I2C.h>
#include <Ultrasonic.h>
#include <avr/sleep.h>
#include "ServoTimer2.h"
#include "Task.h"

class MachineTask: public Task {

    enum {Boot, Ready, Selecting, Making, Assistance, WaitRemove, Idle, Testing} state;
    int periodCounter = 0;
    bool choosing = false;
    int selectingCounter = 0;
    int sugarQuantity = 0;
    int servoPosition;
    int testPeriodCounter = 0;
    
    public:
        MachineTask();
        void init(int period);
        void tick();
        static void wakeUp();
};

#endif
