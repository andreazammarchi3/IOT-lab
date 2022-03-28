/*
 * Assignment #01 - Catch the Bouncing Led Ball
 * 
 * Author: andrea.zammarchi3@studio.unibo.it - 0000914652
 * 
 * Link repo GitHub:
 * https://github.com/andreazammarchi3/IOT-lab/tree/main/Assignments/Assignment%20%2301
 */

/* Include necessary libraries. */
#include <avr/sleep.h>
#include <avr/power.h>
#include "EnableInterrupt.h"
#include "TimerOne.h"

/* Define names for pins. */
#define Pot A0

#define T1 8
#define T2 7
#define T3 4
#define T4 2

#define Ls 10
#define L1 9
#define L2 6
#define L3 5
#define L4 3

/* Define global vars. */
char Leds[] = {L1, L2, L3, L4};
char Btns[] = {T1, T2, T3, T4};

int sleepCounter;
int sleepCountdown;
int fadingSpeed;
int brightness;
int fadeAmount;
int score;
int appStatus;
int S;
int P;

boolean forward;

float t1;
float t1_counter;
float t2;
float F;
float lvlFactor;


/*
 * Setup method, first method to be executed from Arduino.
 */
void setup() {
  Serial.begin(9600);
  for (int i = 0; i < sizeof(Btns); i++) {
    pinMode(Btns[i], INPUT);
  }
  pinMode(Ls, OUTPUT);
  for (int i = 0; i < sizeof(Leds); i++) {
    pinMode(Leds[i], OUTPUT);
  }
  appStatus = 1;
  initialState();
}

/*
 * Super-loop, main app method.
 */
void loop() {
  switch (appStatus) {
    case 1:
      /* 
       * Initial state:
       * Waiting T1 to be clicked. If not, after 10s go to sleep.
       */
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
      break;

    case 2:
      /* 
       * RunBall state:
       * Ball is shifting through the leds, after random time it stops.
       */
      if (forward == true) {
        for (P = 1; P <= 3; P++) {
          if(P != 1) {
            digitalWrite(Leds[P-2], LOW);
          } else {
            digitalWrite(Leds[P], LOW);
          }
          digitalWrite(Leds[P-1], HIGH);
          if (t1  <= S * t1_counter / (1000 * F)) {
            appStatus = 3;
            break;
          }
          delay(S / F);
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
          if (t1  <= S * t1_counter / (1000 * F)) {
            appStatus = 3;
            break;
          }
          delay(S / F);
          t1_counter++;
        }
        forward = true;
      }
      break;

    case 3:
      /* 
       * CatchBall state:
       * User must press the correct button to increase the score.
       */
      enableInterrupt(Btns[P-1], incScore, RISING);
      delay(t2 / F);
      disableInterrupt(Btns[P-1]);
      if (t1_counter != 0) {
        appStatus = 4;
      }
      digitalWrite(Leds[P-1], LOW);
      break;

    case 4:
      /* 
       * GameOver state:
       * Game is over. Go back to initial state.
       */
      String str = "Game Over. Final Score: ";
      str += score;
      Serial.println(str);
      delay(10000);
      appStatus = 1;
      initialState();
      break;

    default:
      /* 
       * Theoretically it shouldn't happen.
       * In case of bugs, go back to the initial state.
       */
      appStatus = 1;
      initialState();
      break;
  }
}

/*
 * Reset some vars to their default values.
 */
void resetVarsToDefaultValue() {
  sleepCounter = 0;
  sleepCountdown = 10000; /* 10000 msec = 10 sec */
  fadingSpeed = 20;
  brightness = 0;
  fadeAmount = 5;
  score = 0;
  forward = true;
  S = 1000;
  F = 1;
  P = 1;
  t1_counter = 0;
  t2 = 10000;
}

/*
 * Generate a random integer in a range.
 */
void genRandom() {
  randomSeed(analogRead(5));
  t1 = random(5, 15);
}

/*
 * Few firts instructions to do when app goes to initial state.
 */
void initialState() {
  resetVarsToDefaultValue();
  Serial.println("Welcome to the Catch the Bouncing Led Ball Game. Press Key T1 to Start");
  digitalWrite(Ls, LOW);
  for (int i = 0; i < sizeof(Leds); i++) {
    digitalWrite(Leds[i], LOW);
  }
  enableInterrupt(T1, startGame, RISING);
}

/*
 * Method called when Arduino has to enter in Deep Sleep Mode.
 */
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

/*
 * Method called when Arduino exits from Deep Sleep Mode.
 */
void wakeUp() {
  for (int i = 0; i < sizeof(Btns); i++) {
    disableInterrupt(Btns[i]);
  }
  appStatus = 1;
  initialState();
}

/*
 * Method called by interrupt on T1. The game starts.
 */
void startGame() {
  disableInterrupt(T1);
  int potVal = analogRead(Pot);
  lvlFactor = map(potVal, 0, 1023, 1, 8) * 0.25;
  Serial.println("Go!");
  digitalWrite(Ls, LOW);
  genRandom();
  appStatus = 2;
}

/*
 * Method called on interrupt on the corresponding btn in game.
 * Increment current score.
 */
void incScore() {
  disableInterrupt(Btns[P-1]);
  score++;
  appStatus = 2;
  forward = true;
  F += lvlFactor;
  P = 1;
  t1_counter = 0;
  genRandom();
  String str = "New point! Score: ";
  str += score;
  Serial.println(str);
}
