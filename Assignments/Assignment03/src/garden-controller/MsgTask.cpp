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
    if (MsgServiceBT.isMsgAvailable()) {
      Msg* msg = MsgServiceBT.receiveMsg();
      String str = msg->getContent();
      if (str == "MANUAL") {
        mode = 1;
        MsgServiceBT.sendMsg(str);
      }
      delete msg;
    }
  } else {
    if (MsgService.isMsgAvailable()) {
      Msg* msg = MsgService.receiveMsg();
      String str = msg->getContent();
      if (str == "ALARM") {
        mode = 2;
        MsgService.sendMsg("ALARM");
      } else if (str == "mode") {
        MsgService.sendMsg(String(mode));
      } else if (str == "led1") {
        MsgService.sendMsg(String(led1));
      } else if (str == "led2") {
        MsgService.sendMsg(String(led2));
      } else if (str == "led3") {
        MsgService.sendMsg(String(led3));
      } else if (str == "led4") {
        MsgService.sendMsg(String(led4));
      } else if (str == "irri") {
        MsgService.sendMsg(String(irrigation));
      } else {
        cutValueFromStr(str);
        MsgService.sendMsg(str);
      }
      delete msg;
    }

    if (MsgServiceBT.isMsgAvailable()) {
      Msg* msg = MsgServiceBT.receiveMsg();
      String str = msg->getContent();
      if (str == "MANUAL") {
        mode = 1;
        MsgServiceBT.sendMsg(str);
      } else {
        cutValueFromStr(str);
        MsgServiceBT.sendMsg(str);
      }
      delete msg;
    }
  }
}

void MsgTask::cutValueFromStr(String str) {
  int value = str.toInt();
  int ones = value%10;
  int tens = (value/10)%10;
  switch (tens) {
    case 1:
      led1 = ones;
      break;
    case 2:
      led2 = ones;
      break;
    case 3:
      led3 = ones;
      break;
    case 4:
      led4 = ones;
      break;
    case 5:
      irrigation = ones;
      break;
  }
}


