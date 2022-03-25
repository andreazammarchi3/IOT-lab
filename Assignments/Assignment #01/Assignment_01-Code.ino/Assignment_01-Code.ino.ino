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

int current;

void setup()
{
  pinMode(Ls, OUTPUT);
  pinMode(L1, OUTPUT);
  pinMode(L2, OUTPUT);
  pinMode(L3, OUTPUT);
  pinMode(L4, OUTPUT);
  pinMode(T1, INPUT);
  pinMode(T2, INPUT);
  pinMode(T3, INPUT);
  pinMode(T4, INPUT);

  Serial.begin(9600);
}

void loop()
{
  int T1_state = digitalRead(T1);
  int T2_state = digitalRead(T2);
  int T3_state = digitalRead(T3);
  int T4_state = digitalRead(T4);
  
  if (T1_state == HIGH) {
    digitalWrite(L1, HIGH);
  } else {
    digitalWrite(L1, LOW);
  }

  if (T2_state == HIGH) {
    digitalWrite(L2, HIGH);
  } else {
    digitalWrite(L2, LOW);
  }

  if (T3_state == HIGH) {
    digitalWrite(L3, HIGH);
  } else {
    digitalWrite(L3, LOW);
  }

  if (T4_state == HIGH) {
    digitalWrite(L4, HIGH);
  } else {
    digitalWrite(L4, LOW);
  }
  
  digitalWrite(Ls, HIGH);

  int newValue = analogRead(Pot);
  if (newValue != current) {
    current = newValue;
    Serial.println(current);
  }
}
