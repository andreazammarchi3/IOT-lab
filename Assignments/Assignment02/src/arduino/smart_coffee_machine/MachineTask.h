#ifndef __MACHINETASK__
#define __MACHINETASK__

#include <Arduino.h>
#include <LiquidCrystal_I2C.h>
#include "Task.h"

class MachineTask: public Task {

    enum {Boot, Ready, Selecting} state;
    int periodCounter;
    
    public:
        MachineTask();
        void init(int period);
        void tick();
};

#endif
