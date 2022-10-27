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
        led1 = value.toInt();
        MsgService.sendMsg("led1_" + value);
      } else if (strBT.indexOf("led2_") >= 0) {
        String value = cutValueFromStr(strBT, "led2_");
        led2 = value.toInt();
        MsgService.sendMsg("led2_" + value);
      } else if (strBT.indexOf("led3_") >= 0) {
        String value = cutValueFromStr(strBT, "led3_");
        led3 = value.toInt();
        if (led3 != 0) {
          led3 = led3 * 5;
        }
        MsgService.sendMsg("led3_" + value);
      } else if (strBT.indexOf("led4_") >= 0) {
        String value = cutValueFromStr(strBT, "led4_");
        led4 = value.toInt();
        if (led4 != 0) {
          led4 = led4 * 5;
        }
        MsgService.sendMsg("led4_" + value);
      } else if (strBT.indexOf("irri_") >= 0) {
        String value = cutValueFromStr(strBT, "irri_");
        irrigation = value.toInt();
        MsgService.sendMsg("irri_" + value);
      } else if (strBT == "MANUAL") {
        mode = 1;
        MsgService.sendMsg("MANUAL");
      }
      delete msg;
    }
    if (MsgService.isMsgAvailable()) {
      Msg* msg = MsgService.receiveMsg();
      String str = msg->getContent();
      if (str.indexOf("led1_") >= 0) {
        String value = cutValueFromStr(str, "led1_");
        led1 = value.toInt();
        MsgService.sendMsg("led1_" + value);
      } else if (str.indexOf("led2_") >= 0) {
        String value = cutValueFromStr(str, "led2_");
        led2 = value.toInt();
        MsgService.sendMsg("led2_" + value);
      } else if (str.indexOf("led3_") >= 0) {
        String value = cutValueFromStr(str, "led3_");
        led3 = value.toInt();
        MsgService.sendMsg("led3_" + value);
      } else if (str.indexOf("led4_") >= 0) {
        String value = cutValueFromStr(str, "led4_");
        led4 = value.toInt();
        MsgService.sendMsg("led4_" + value);
      } else if (str.indexOf("irri_") >= 0) {
        String value = cutValueFromStr(str, "irri_");
        irrigation = value.toInt();
        MsgService.sendMsg("irri_" + value);
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