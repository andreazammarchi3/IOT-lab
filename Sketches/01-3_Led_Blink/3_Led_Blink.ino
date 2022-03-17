int ledBlue = 2;
int ledGreen = 3;
int ledRed = 4;
int del = 500;


void setup() {
  pinMode(ledBlue, OUTPUT);
  pinMode(ledGreen, OUTPUT);
  pinMode(ledRed, OUTPUT);
}


void loop() {
  digitalWrite(ledRed, HIGH);
  delay(del);
  digitalWrite(ledRed, LOW);
  digitalWrite(ledGreen, HIGH);
  delay(del);
  digitalWrite(ledGreen, LOW);
  digitalWrite(ledBlue, HIGH);
  delay(del);
  digitalWrite(ledBlue, LOW);
}
