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
    String str = msg->getContent();  
    if (str == "led1"){
        MsgService.sendMsg(String(led1));
     } else if (str == "led2"){
        MsgService.sendMsg(String(led2)); 
     } else if (str == "led3"){
        MsgService.sendMsg(String(led3));   
     } else if (str == "led4"){
        MsgService.sendMsg(String(led4));
     } else if (str == "lrrigation"){
        MsgService.sendMsg(String(irrigation));
     } else if (str == "mode"){
        MsgService.sendMsg(String(mode));
     } else if (str.indexOf("led1_") > 0) {
        led1 = cutValueFromStr(str, "led1_");
     } else if (str.indexOf("led2_") > 0) {
        led2 = cutValueFromStr(str, "led2_");
     } else if (str.indexOf("led3_") > 0) {
        led3 = cutValueFromStr(str, "led3_");
     } else if (str.indexOf("led4_") > 0) {
        led4 = cutValueFromStr(str, "led4_");
     } else if (str.indexOf("irr_") > 0) {
        irrigation = cutValueFromStr(str, "irri_");
     }
    delete msg;
  }
  /*
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
  */   
}

int MsgTask::cutValueFromStr(String str, String sub) {
  str.remove(4);
  return str.toInt();
}