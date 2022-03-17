#include <LiquidCrystal.h>

LiquidCrystal lcd(12, 11, 5, 4, 3, 2);

int sensorPin = 0; 
 
void setup()
{
  lcd.begin(16, 2);                         
}
 
void loop()                     
{
  lcd.clear();
  int reading = analogRead(sensorPin);  
  float voltage = reading * 5.0;
  voltage /= 1024.0; 
  float temperatureC = (voltage - 0.5) * 100 ; 
  float temperatureF = (temperatureC * 9.0 / 5.0) + 32.0;
  lcd.print(temperatureF); 
  lcd.println(" degrees F ");
  lcd.setCursor(0,1);
  lcd.print(temperatureC);
  lcd.println(" degrees C ");
  delay(1000);
}
 
