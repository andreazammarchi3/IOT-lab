#include <avr/sleep.h>
#include <avr/power.h>
#include "EnableInterrupt.h"

#define T1 8
#define T2 7
#define T3 4
#define T4 2

#define Ls 10
#define L1 9
#define L2 6
#define L3 5
#define L4 3

int s = 0;

void setup() {
  Serial.begin(9600);
  pinMode(T4, INPUT_PULLUP);
  pinMode(Ls, OUTPUT);
  Serial.println("Pronti!");

  digitalWrite(Ls, HIGH);
}

void loop() {
  delay(1000);
  s++;

  if(s == 3) {
    Serial.println("Sto dormendo...");
    delay(200);
    s = 0;
    startSleep();
  }
}

void startSleep() {
  enableInterrupt(T1, wakeUp, RISING);
  enableInterrupt(T2, wakeUp, RISING);
  enableInterrupt(T3, wakeUp, RISING);
  enableInterrupt(T4, wakeUp, RISING);
  delay(100);
  digitalWrite(Ls, LOW);

  set_sleep_mode(SLEEP_MODE_PWR_DOWN);
  sleep_enable();
  sleep_mode();

  sleep_disable();
}

void wakeUp() {
  disableInterrupt(T1);
  disableInterrupt(T2);
  disableInterrupt(T3);
  disableInterrupt(T4);
  digitalWrite(Ls, HIGH);
  Serial.println("Sono sveglio!");
}
