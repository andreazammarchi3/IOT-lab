#ifndef __MSGTASK__
#define __MSGTASK__

#include <String.h>
#include "Task.h"
#include "MsgService.h"

class MsgTask: public Task {
    public:
        void init(int period);
        void tick();
};

#endif
