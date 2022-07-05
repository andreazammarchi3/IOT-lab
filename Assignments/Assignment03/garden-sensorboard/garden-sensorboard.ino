//variabili globali
int tempVal = 0;
float temp;
float volts;
float lightVal;

const int tempPin = 35;
const int ledPin = 23;
const int photoPin = 34;

void setup()
{
  //init seriale
  Serial.begin(9600);
  pinMode(tempPin, INPUT);
  pinMode(ledPin, OUTPUT);
  pinMode(photoPin, INPUT);
  
  digitalWrite(ledPin, HIGH);
}

void loop()
{
  Serial.print("Temperature: ");
  Serial.println(getTemp());
  Serial.print("Light: ");
  Serial.println(getLight());

  delay(500);
}

float getTemp() {
  tempVal = analogRead(tempPin);
  volts = tempVal/4095.0;
  temp = (volts - 0.5) * 100 ;
  return temp;
}

int getLight() {
  lightVal = analogRead(photoPin);
  return lightVal;
}
