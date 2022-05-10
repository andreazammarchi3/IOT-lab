#include "MsgTask.h"

extern int tempPin;
extern int potSugar;
extern int bUp;
extern int bDown;
extern int bMake;

extern int N_MAX_COFFEE;
extern int N_MAX_TEA;
extern int N_MAX_CHOCOLATE;

extern String modality;
extern int nCoffee;
extern int nTea;
extern int nChocolate;
extern int selfTests;



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
           }
          delete msg;
        }
}
