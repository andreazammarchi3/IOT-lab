#include "MsgTask.h"

extern String modality;
extern int nCoffee;
extern int nTea;
extern int nChocolate;
extern int selfTests;

extern bool refill;

void MsgTask::init(int period) {
    Task::init(period);
    MsgService.init();
}

void MsgTask::tick() {
    if (MsgService.isMsgAvailable()){
          Msg* msg = MsgService.receiveMsg();    
          if (msg->getContent() == "modality"){
              MsgService.sendMsg(modality);
           } else if (msg->getContent() == "Coffee"){
              MsgService.sendMsg(String(nCoffee)); 
           } else if (msg->getContent() == "Tea"){
              MsgService.sendMsg(String(nTea));   
           } else if (msg->getContent() == "Chocolate"){
              MsgService.sendMsg(String(nChocolate));
           } else if (msg->getContent() == "selfTests"){
              MsgService.sendMsg(String(selfTests));
           } else if (msg->getContent() == "refill"){
              refill = true;
           }
          delete msg;
        }
}
