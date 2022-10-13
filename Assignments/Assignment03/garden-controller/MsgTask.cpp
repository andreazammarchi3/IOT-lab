#include "MsgTask.h"

extern int led1;
extern int led2;
extern int led3;
extern int led4;
extern int irrigation;
extern int mode;

void MsgTask::init(int period) {
  Task::init(period);
  MsgService.init();
  bt = new MsgServiceBT();
  bt->init();
}

void MsgTask::tick() {
  if (MsgService.isMsgAvailable()) {
    Msg* msg = MsgService.receiveMsg();    
    if (msg->getContent() == "led1"){
        MsgService.sendMsg(String(led1));
     } else if (msg->getContent() == "led2"){
        MsgService.sendMsg(String(led2)); 
     } else if (msg->getContent() == "led3"){
        MsgService.sendMsg(String(led3));   
     } else if (msg->getContent() == "led4"){
        MsgService.sendMsg(String(led4));
     } else if (msg->getContent() == "irrigation"){
        MsgService.sendMsg(String(irrigation));
     } else if (msg->getContent() == "mode"){
        MsgService.sendMsg(String(mode));
     }
    delete msg;
  }
  if (bt->isMsgAvailable()) {
    Msg1* msg1 = bt->receiveMsg();
    if (msg1->getContent() == "1") {
      led1 = 1;
      led2 = 1;
      led3 = 1;
      led4 = 1;
    } else if (msg1->getContent() == "0") {
      led1 = 0;
      led2 = 0;
      led3 = 0;
      led4 = 0;
    } else if (msg1->getContent() == "M") {
      mode = 1;
    }
    delete msg1;
  }
        
}
