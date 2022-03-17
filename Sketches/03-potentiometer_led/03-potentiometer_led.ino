#define POT_PIN A0
#define led 3
int current;

void setup()
{
  Serial.begin(115000);
  pinMode(led, OUTPUT);
}

void loop()
{
  int newValue = analogRead(POT_PIN);
  if(newValue != current){
    current = newValue;
    Serial.println(current);
    analogWrite(led, current/4);
  }
}
