#ifndef __MACHINETASK__
#define __MACHINETASK__

#include <Arduino.h>
#include <LiquidCrystal_I2C.h>
#include <Ultrasonic.h>
#include <avr/sleep.h>
#include <avr/power.h>
//#include <EnableInterrupt.h>
#include "ServoTimer2.h"
#include "Task.h"

class MachineTask: public Task {

    enum {Boot, Ready, Selecting, Making, Assistance, WaitRemove, Idle} state;
    int periodCounter;
    
    public:
        MachineTask();
        void init(int period);
        void tick();
};

#endif
