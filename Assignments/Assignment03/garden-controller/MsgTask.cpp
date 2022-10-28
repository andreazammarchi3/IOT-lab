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
  if (mode == 2) {
    checkManual();
  } else if (mode == 0) {
    checkManual();
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
      } else if (str == "ALARM") {
        mode = 2;
        MsgService.sendMsg("ALARM");
      } else if (str == "mode") {
        MsgService.sendMsg(String(mode));
      }
    }
  } else if(mode == 1) {
    if (MsgService.isMsgAvailable()) {
      Msg* msg = MsgService.receiveMsg();
      String str = msg->getContent();
      if (str == "led1") {
        MsgService.sendMsg(String(led1));
      } else if (str == "led2") {
        MsgService.sendMsg(String(led2));
      } else if (str == "led3") {
        MsgService.sendMsg(String(led3));
      } else if (str == "led4") {
        MsgService.sendMsg(String(led4));
      } else if (str == "irri") {
        MsgService.sendMsg(String(irrigation));
      } else if (str == "mode") {
        MsgService.sendMsg(String(mode));
      }
      delete msg;
    }

    if (MsgServiceBT.isMsgAvailable()) {
      Msg* msg = MsgServiceBT.receiveMsg();
      String str = msg->getContent();
      if (str.indexOf("led1_") >= 0) {
        String value = cutValueFromStr(str, "led1_");
        led1 = value.toInt();
        MsgServiceBT.sendMsg("led1_" + value);
      } else if (str.indexOf("led2_") >= 0) {
        String value = cutValueFromStr(str, "led2_");
        led2 = value.toInt();
        MsgServiceBT.sendMsg("led2_" + value);
      } else if (str.indexOf("led3_") >= 0) {
        String value = cutValueFromStr(str, "led3_");
        led3 = value.toInt();
        MsgServiceBT.sendMsg("led3_" + value);
      } else if (str.indexOf("led4_") >= 0) {
        String value = cutValueFromStr(str, "led4_");
        led4 = value.toInt();
        MsgServiceBT.sendMsg("led4_" + value);
      } else if (str.indexOf("irri_") >= 0) {
        String value = cutValueFromStr(str, "irri_");
        irrigation = value.toInt();
        MsgServiceBT.sendMsg("irri_" + value);
      } else if (str == "MANUAL") {
        mode = 1;
        MsgServiceBT.sendMsg(str);
      }
      delete msg;
    }
  }
}

String MsgTask::cutValueFromStr(String str, String sub) {
  str.remove(0, 5);
  return str;
}

void MsgTask::checkManual() {
  if (MsgServiceBT.isMsgAvailable()) {
    Msg* msg = MsgServiceBT.receiveMsg();
    String str = msg->getContent();
    if (str == "MANUAL") {
      mode = 1;
      MsgServiceBT.sendMsg(str);
    }
    delete msg;
  }
}


