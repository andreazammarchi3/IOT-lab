#include "Arduino.h"
#include "MsgServiceBT.h"


MsgServiceBT::MsgServiceBT(){
  channel = new SoftwareSerial(Rx, Tx);
}

void MsgServiceBT::init(){
  content.reserve(256);
  channel->begin(9600);
  channel->print("AT");
  channel->print("AT+BAUD4");
  channel->print("AT+NAME");
  channel->print("AT+NAMEisi03");
}

bool MsgServiceBT::sendMsg(Msg1 msg){
  channel->println(msg.getContent());  
}

bool MsgServiceBT::isMsgAvailable(){
  return channel->available();
}

Msg1* MsgServiceBT::receiveMsg(){
  while (channel->available()) {
    char ch = (char) channel->read();
    if (ch == '\n'){
      Msg1* msg = new Msg1(content);  
      content = "";
      return msg;    
    } else {
      content += ch;      
    }
  }
  return NULL;  
}
