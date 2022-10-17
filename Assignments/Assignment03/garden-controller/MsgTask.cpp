#include "MsgTask.h"

extern bool onOffLights;
extern int fadeLights;
extern int irrigation;

void MsgTask::init(int period) {
  Task::init(period);
  MsgService.init();
  /*
  bt = new MsgServiceBT();
  bt->init();
  */
}

void MsgTask::tick() {
  if (MsgService.isMsgAvailable()) {
    Msg* msg = MsgService.receiveMsg();  
    String str = msg->getContent();  
    if (str == "onOffLights"){
        if (onOffLights) {
          MsgService.sendMsg(String(1));
        } else {
          MsgService.sendMsg(String(0));
        }
     } else if (str == "fadeLights"){
        MsgService.sendMsg(String(fadeLights)); 
     } else if (str == "irrigation"){
        MsgService.sendMsg(String(irrigation));
     }  else if (str.indexOf("leds_") >= 0) {
        String value = cutValueFromStr(str, "leds_");
        if (value == "0") {
          onOffLights = false;
        } else {
          onOffLights = true;
        }
        MsgService.sendMsg(value);
     } else if (str.indexOf("fade_") >= 0) {
        String value = cutValueFromStr(str, "fade_");
        fadeLights = value.toInt();
        if (fadeLights != 0) {
          fadeLights = (6 - fadeLights) * 10;
        }
        MsgService.sendMsg(String(fadeLights));
     } else if (str.indexOf("irri_") >= 0) {
        String value = cutValueFromStr(str, "irri_");
        irrigation = value.toInt();
        MsgService.sendMsg(value);
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

String MsgTask::cutValueFromStr(String str, String sub) {
  str.remove(0,5);
  return str;
}
