#include <avr/sleep.h>
#include <avr/power.h>
#include "EnableInterrupt.h"
#include "TimerOne.h"

#define T1 8
#define T2 7
#define T3 4
#define T4 2

#define Ls 10
#define L1 9
#define L2 6
#define L3 5
#define L4 3

char Leds[] = {L1, L2, L3, L4};
char Btns[] = {T1, T2, T3, T4};

int sleepCounter;
int sleepCountdown;
int fadingSpeed;
int brightness;
int fadeAmount;
int score;
boolean waiting;
boolean forward;

int S;
int P;
int t1;
int t1_counter;
int t2;

void resetVarsToDefaultValue() {
  sleepCounter = 0;
  sleepCountdown = 10000; /* 10000 msec = 10 sec */
  fadingSpeed = 20;
  brightness = 0;
  fadeAmount = 5;
  score = 0;
  waiting = true;
  forward = true;
  S = 500;
  P = 1;
  t1_counter = 0;
  t2 = 5000;
}

void setup() {
  Serial.begin(9600);
  for (int i = 0; i < sizeof(Btns); i++) {
    pinMode(Btns[i], INPUT_PULLUP);
  }
  pinMode(Ls, OUTPUT);
  for (int i = 0; i < sizeof(Leds); i++) {
    pinMode(Leds[i], OUTPUT);
  }
  initialState();
}

void loop() {
  if (waiting == true) {
    /* Waiting for T1 pressed */
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
    /* Playing */
    if (forward == true) {
      for (P = 1; P <= 3; P++) {
        if(P != 1) {
          digitalWrite(Leds[P-2], LOW);
        } else {
          digitalWrite(Leds[P], LOW);
        }
        digitalWrite(Leds[P-1], HIGH);
        if (t1  == S * t1_counter / 1000) {
          checkBtnClick();
          break;
        }
        delay(S);
        t1_counter++;
      }
      forward = false;
    } else {
      for (P = 4; P >= 2; P--) {
        if(P != 4) {
          digitalWrite(Leds[P], LOW);
        } else {
          digitalWrite(Leds[P-2], LOW);
        }
        digitalWrite(Leds[P-1], HIGH);
        if (t1  == S * t1_counter / 1000) {
          checkBtnClick();
          break;
        }
        delay(S);
        t1_counter++;
      }
      forward = true;
    }
  }
}

void genRandom() {
  randomSeed(analogRead(5));
  t1 = random(5, 15);
}

void initialState() {
  resetVarsToDefaultValue();
  Serial.println("Welcome to the Catch the Bouncing Led Ball Game. Press Key T1 to Start");
  digitalWrite(Ls, LOW);
  for (int i = 0; i < sizeof(Leds); i++) {
    digitalWrite(Leds[i], LOW);
  }
  enableInterrupt(T1, startGame, RISING);
}

void startSleep() {
  disableInterrupt(T1);
  for (int i = 0; i < sizeof(Btns); i++) {
    enableInterrupt(Btns[i], wakeUp, RISING);
  }
  delay(100);
  digitalWrite(Ls, LOW);
  set_sleep_mode(SLEEP_MODE_PWR_DOWN);
  sleep_enable();
  sleep_mode();
  
  sleep_disable();
}

void wakeUp() {
  for (int i = 0; i < sizeof(Btns); i++) {
    disableInterrupt(Btns[i]);
  }
  initialState();
}

void startGame() {
  disableInterrupt(T1);
  Serial.println("Go!");
  digitalWrite(Ls, LOW);
  genRandom();
  waiting = false;
}

void incScore() {
  score++;
}

void checkBtnClick() {
  int tempScore = score;
  enableInterrupt(Btns[P-1], incScore, RISING);
  delay(t2);
  if(tempScore == score) {
    gameOver();
  } else {
    disableInterrupt(Btns[P-1]);
    waiting = false;
    forward = true;
    P = 1;
    t1_counter = 0;
    genRandom();
    String str = "New point! Score: ";
    str += score;
    Serial.println(str);
  }
}

void gameOver() {
  disableInterrupt(Btns[P-1]);
  String str = "Game Over. Final Score: ";
  str += score;
  Serial.println(str);
  delay(10000);
  initialState();
}
