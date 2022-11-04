#ifndef __MSGTASK__
#define __MSGTASK__

#include <String.h>
#include "Task.h"
#include "MsgService.h"
#include "define.h"

class MsgTask: public Task {
    int periodCounter;

  public:
    void init(int period);
    void tick();

  private:
    void cutValueFromStr(String str);
};

#endif
