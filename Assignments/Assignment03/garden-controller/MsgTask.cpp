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
    if (msg1->getContent() == "Led1_ON") {
      led1 = 1;
      digitalWrite(LED1_PIN, HIGH);
    } else if (msg1->getContent() == "Led1_OFF") {
      led1 = 0;
      digitalWrite(LED1_PIN, LOW);
    } else if (msg1->getContent() == "MANUAL") {
      mode = 1;
    }
  }
        
}
