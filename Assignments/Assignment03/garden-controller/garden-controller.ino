#include <SoftwareSerial.h> 

const int RxD = 2;
const int TxD = 3;

SoftwareSerial HC05(RxD,TxD); // RX, TX
 
void setup() 
{ 
  pinMode(RxD, INPUT);
  pinMode(TxD, OUTPUT);

  HC05.begin(9600);
} 
 
void loop() 
{ 
  while(1) {
    HC05.print("bluetooth connected!\n");
    delay(2000);
    HC05.flush();
  }
}
