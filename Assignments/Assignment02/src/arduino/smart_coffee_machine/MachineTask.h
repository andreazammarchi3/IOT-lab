#ifndef __MACHINETASK__
#define __MACHINETASK__

#include <Arduino.h>
#include <LiquidCrystal_I2C.h>
#include "ServoTimer2.h"
#include "Task.h"

class MachineTask: public Task {

    enum {Boot, Ready, Selecting, Making, Assistance} state;
    int periodCounter;
    
    public:
        MachineTask();
        void init(int period);
        void tick();
};

#endif
