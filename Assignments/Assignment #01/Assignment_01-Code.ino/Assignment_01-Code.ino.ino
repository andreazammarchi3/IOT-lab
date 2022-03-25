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

int Leds[] = {L1, L2, L3, L4};

int sleepCounter = 0;
int sleepCountdown = 10000; /* 10000 msec = 10 sec */
int fadingSpeed = 20;
int brightness = 0;
int fadeAmount = 5;
int score = 0;
boolean waiting = true;

void setup() {
  Serial.begin(9600);
  pinMode(T1, INPUT_PULLUP);
  pinMode(T2, INPUT_PULLUP);
  pinMode(T3, INPUT_PULLUP);
  pinMode(T4, INPUT_PULLUP);
  pinMode(Ls, OUTPUT);
  for (int i=0; i<sizeof(Leds); i++) {
    pinMode(Leds[i], OUTPUT);
  }
  initialState();
}

void loop() {
  if(waiting == true) {
    analogWrite(Ls, brightness);
    brightness = brightness + fadeAmount;
    if (brightness <= 0 || brightness >= 255) {
      fadeAmount = -fadeAmount;
    }
  
    if (sleepCounter == sleepCountdown / fadingSpeed) {
      Serial.println("I'm sleeping...");
      sleepCounter = 0;
      startSleep();
    }
  
    delay(fadingSpeed);
    sleepCounter++;
  } else {
    delay(5000);
    initialState();
  }
  
}

void initialState() {
  waiting = true;
  sleepCounter = 0;
  Serial.println("Welcome to the Catch the Bouncing Led Ball Game. Press Key T1 to Start");

  digitalWrite(Ls, LOW);
  for (int i=0; i<sizeof(Leds); i++) {
    digitalWrite(Leds[i], LOW);
  }

  enableInterrupt(T1, startGame, RISING);
}

void startSleep() {
  disableInterrupt(T1);
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
  initialState();
}

void startGame() {
  disableInterrupt(T1);
  Serial.println("Go!");
  digitalWrite(Ls, LOW);
  for (int i=0; i<sizeof(Leds); i++) {
    digitalWrite(Leds[i], HIGH);
  }
  waiting = false;
  score = 0;
}
