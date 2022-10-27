#include "MsgTask.h"

extern bool onOffLights;
extern int fadeLights;
extern int irrigation;
extern int mode;

extern int led1BT;
extern int led2BT;
extern int led3BT;
extern int led4BT;
extern int irrigationBT;

void MsgTask::init(int period) {
  Task::init(period);
  MsgService.init();
  MsgServiceBT.init();
  periodCounter = 0;
}

void MsgTask::tick() {
  if (mode != 2) {
   if (MsgServiceBT.isMsgAvailable()) {
      Msg* msg = MsgServiceBT.receiveMsg();
      String strBT = msg->getContent();
      if (strBT.indexOf("led1_") >= 0) {
        String value = cutValueFromStr(strBT, "led1_");
        if (value == "0") {
          led1BT = 1;
        } else {
          led1BT = 0;
        }
        MsgServiceBT.sendMsg(value);
      } else if (strBT.indexOf("led2_") >= 0) {
        String value = cutValueFromStr(strBT, "led2_");
        if (value == "0") {
          led2BT = 1;
        } else {
          led2BT = 0;
        }
        MsgServiceBT.sendMsg(value);
      } else if (strBT.indexOf("led3_") >= 0) {
        String value = cutValueFromStr(strBT, "led3_");
        led3BT = value.toInt();
        if (led3BT != 0) {
          led3BT = led3BT * 5;
        }
        MsgServiceBT.sendMsg(value);
      } else if (strBT.indexOf("led4_") >= 0) {
        String value = cutValueFromStr(strBT, "led4_");
        led4BT = value.toInt();
        if (led4BT != 0) {
          led4BT = led4BT * 5;
        }
        MsgServiceBT.sendMsg(value);
      } else if (strBT.indexOf("irri_") >= 0) {
        String value = cutValueFromStr(strBT, "irri_");
        irrigationBT = value.toInt();
        MsgServiceBT.sendMsg(value);
      } else if (strBT == "AUTO") {
        mode = 0;
      } else if (strBT == "MANUAL") {
        mode = 1;
      } else if (strBT == "ALARM") {
        mode = 2;
      }
      delete msg;
    }
    if (MsgService.isMsgAvailable()) {
      Msg* msg = MsgService.receiveMsg();
      String str = msg->getContent();
      if (str == "onOffLights") {
        if (onOffLights) {
          MsgService.sendMsg(String(1));
        } else {
          MsgService.sendMsg(String(0));
        }
      } else if (str == "fadeLights") {
        MsgService.sendMsg(String(fadeLights));
      } else if (str == "irrigation") {
        MsgService.sendMsg(String(irrigation));
      } else if (str.indexOf("leds_") >= 0) {
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
          fadeLights = fadeLights * 5;
        }
        MsgService.sendMsg(String(fadeLights));
      } else if (str.indexOf("irri_") >= 0) {
        String value = cutValueFromStr(str, "irri_");
        irrigation = value.toInt();
        MsgService.sendMsg(value);
      } else if (str == "AUTO") {
        mode = 0;
      } else if (str == "MANUAL") {
        mode = 1;
      } else if (str == "ALARM") {
        mode = 2;
      }
      delete msg;
    }
    periodCounter = 0;
  }

  periodCounter++;
  if (periodCounter == 10) {
    mode = 2;
    periodCounter = 0;
    if (MsgServiceBT.isMsgAvailable()) {
      Msg* msg = MsgServiceBT.receiveMsg();
      if (msg->getContent() == "MANUAL") {
        mode=1;
        MsgServiceBT.sendMsg("MANUAL");
      }
      delete msg;
    }
  }
}

String MsgTask::cutValueFromStr(String str, String sub) {
  str.remove(0, 5);
  return str;
}