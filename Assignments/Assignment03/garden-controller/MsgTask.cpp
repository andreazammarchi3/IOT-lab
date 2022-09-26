#include "MsgTask.h"

extern int led1;
extern int led2;
extern int led3;
extern int led4;
extern int irrigation;

void MsgTask::init(int period) {
    Task::init(period);
    MsgService.init();
}

void MsgTask::tick() {
    if (MsgService.isMsgAvailable()){
          Msg* msg = MsgService.receiveMsg();    
          if (msg->getContent() == "led1"){
              MsgService.sendMsg(led1);
           } else if (msg->getContent() == "led2"){
              MsgService.sendMsg(String(led2)); 
           } else if (msg->getContent() == "led3"){
              MsgService.sendMsg(String(led3));   
           } else if (msg->getContent() == "led4"){
              MsgService.sendMsg(String(led4));
           } else if (msg->getContent() == "irrigation"){
              MsgService.sendMsg(String(irrigation));
           }
          delete msg;
        }
}
