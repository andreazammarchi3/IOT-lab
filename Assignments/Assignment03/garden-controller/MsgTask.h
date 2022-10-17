#ifndef __MSGTASK__
#define __MSGTASK__

#include <String.h>
#include "Task.h"
#include "MsgService.h"
#include "MsgServiceBT.h"
#include "define.h"

class MsgTask: public Task {
    MsgServiceBT* bt;

  public:
    void init(int period);
    void tick();

  private:
    String cutValueFromStr(String str, String sub);
};

#endif
