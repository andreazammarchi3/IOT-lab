#include "MsgTask.h"

void MsgTask::init(int period) {
    Task::init(period);
    MsgService.init();
}

void MsgTask::tick() {
    if (MsgService.isMsgAvailable()){
          Msg* msg = MsgService.receiveMsg();    
          if (msg->getContent() == "modality"){
              MsgService.sendMsg("modality");
           } else if (msg->getContent() == "Coffee"){
              MsgService.sendMsg("6"); 
           } else if (msg->getContent() == "Tea"){
              MsgService.sendMsg("7");   
           } else if (msg->getContent() == "Chocolate"){
              MsgService.sendMsg("8");
           } else if (msg->getContent() == "selfTests"){
              MsgService.sendMsg("9");
           }
          delete msg;
        }
}
