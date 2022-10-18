#ifndef __CONTROLLERTASK__
#define __CONTROLLERTASK__

#include <Arduino.h>
#include <avr/sleep.h>
#include <avr/wdt.h>
#include <math.h>
#include "ServoTimer2.h"
#include "Task.h"
#include "define.h"

class ControllerTask: public Task {

    enum {AUTO, MANUAL, ALARM} state;
    int periodCounter = 0;

  public:
    ControllerTask();
    void init(int period);
    void tick();

  private:
    void setOnOffLights(bool value);
    void setFadeLight(int value, int periodCounter, int led);
    void updateServoPosition();
};

#endif
