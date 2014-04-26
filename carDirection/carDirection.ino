const int backwardPin = 2;     // the number of the pushbutton pin
const int forwardPin = 4;
const int rightPin = 8;
const int leftPin = 7;

void setup() { 
  //Initializes serial connection
  Serial.begin(9600);

  // initialize the pins for car direction
  pinMode(backwardPin, OUTPUT);
  pinMode(forwardPin, OUTPUT);  
  pinMode(rightPin, OUTPUT);
  pinMode(leftPin, OUTPUT);
  stopMoving();
}

void loop(){
  while(Serial.available()) {
    int dir = Serial.parseInt();
    if(dir == 1) {
      goForward();
    } else if(dir == 2){
      goRight();
    } else if(dir == 3){
      goLeft();
    } else if(dir == 4){
      goBackward();
    }
    delay(1000);
    Serial.println(dir);
  }
}

void stopMoving() {
     digitalWrite(backwardPin, HIGH);
     digitalWrite(forwardPin, HIGH);
     digitalWrite(rightPin, HIGH);
     digitalWrite(leftPin, HIGH);
     delay(3000); 
}
void goRight() {
  for(int i = 0; i < 5; i++){
     digitalWrite(leftPin, LOW);
     digitalWrite(backwardPin, LOW);
     delay(250);
     digitalWrite(backwardPin, HIGH);
     digitalWrite(leftPin, HIGH);
     digitalWrite(forwardPin, LOW);
     digitalWrite(rightPin, LOW);
     delay(265);
     digitalWrite(forwardPin, HIGH);
     digitalWrite(rightPin, HIGH);
  }
  goForward();
}
void goLeft() {
  for(int i = 0; i < 5; i++){
     digitalWrite(rightPin, LOW);
     digitalWrite(backwardPin, LOW);
     delay(250);
     digitalWrite(backwardPin, HIGH);
     digitalWrite(rightPin, HIGH);
     digitalWrite(forwardPin, LOW);
     digitalWrite(leftPin, LOW);
     delay(265);
     digitalWrite(forwardPin, HIGH);
     digitalWrite(leftPin, HIGH);
  }
  goForward();
}
void goForward() {
     digitalWrite(forwardPin, LOW);
     delay(400);
     digitalWrite(forwardPin, HIGH);
     digitalWrite(backwardPin, LOW);
     delay(150);
     digitalWrite(backwardPin, HIGH); 
}
void goBackward() {
    digitalWrite(backwardPin, LOW);
    delay(1000);
    digitalWrite(backwardPin, HIGH);
    digitalWrite(forwardPin, LOW);
    delay(150);
    digitalWrite(forwardPin, HIGH);
}
