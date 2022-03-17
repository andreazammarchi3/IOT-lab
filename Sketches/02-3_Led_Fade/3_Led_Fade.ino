int ledRed = 6;
int ledGreen = 5;
int ledBlue = 3;
int brightness = 0;
int fadeAmount = 5;


void setup() {
  pinMode(ledRed, OUTPUT);
  pinMode(ledGreen, OUTPUT);
  pinMode(ledBlue, OUTPUT);
}

void loop() {
  analogWrite(ledRed, brightness);
  analogWrite(ledGreen, brightness);
  analogWrite(ledBlue, brightness);
  brightness = brightness + fadeAmount;
  if (brightness <= 0 || brightness >= 255) {
    fadeAmount = -fadeAmount;
  }
  delay(30);
}
