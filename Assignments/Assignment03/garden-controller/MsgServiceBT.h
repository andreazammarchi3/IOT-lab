#ifndef __MSGSERVICEBT__
#define __MSGSERVICEBT__

#include "Arduino.h"
#include <SoftwareSerial.h>
#include "define.h"

class Msg1 {
  String content;

public:
  Msg1(const String& content){
    this->content = content;
  }
  
  String getContent(){
    return content;
  }
};

class MsgServiceBT {
    
public: 
  MsgServiceBT();  
  void init();  
  bool isMsgAvailable();
  Msg1* receiveMsg();
  bool sendMsg(Msg1 msg);

private:
  String content;
  SoftwareSerial* channel;
  
};

#endif