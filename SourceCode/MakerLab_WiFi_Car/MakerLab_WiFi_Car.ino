/*
 *  Copyright by Makerlab.vn 
 *  Site : makerlab.vn
 * 
 */

#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include <Servo.h>

Servo car_servo;

#define SERVO D6        
#define PWMA  D1       
#define DIRA  D3             
#define PWMB  D2                 
#define DIRB  D4         
String command = "", lastcommand = "makerlab";
const char* ssid = "Maker WiFi Car";
String b_speed, b_controller,b_servo;
int Speed;

ESP8266WebServer server(80);

void setup() {
  car_servo.attach(D6);
  pinMode(PWMA, OUTPUT);
  pinMode(DIRA, OUTPUT);
  pinMode(PWMB, OUTPUT);
  pinMode(DIRB, OUTPUT);
  analogWrite(PWMA, 0);
  analogWrite(PWMB, 0);
  Serial.begin(115200);
  WiFi.mode(WIFI_AP);
  WiFi.softAP(ssid);
  IPAddress myIP = WiFi.softAPIP();
  Serial.println("Start");
  Serial.print("AP IP address: ");
  Serial.println(myIP);
  server.on ( "/", HTTP_handleRoot );
  server.onNotFound ( HTTP_handleRoot );
  server.begin();
  servo_atack();
}

void loop() {
  server.handleClient();
  command = server.arg("Makerlab");
  //  Serial.println(command);
  if (command != "" && command != lastcommand)
  {
    //    Serial.println(command);
    lastcommand = command;
    handlingdata(command);
    checkController(b_controller, b_speed);
    checkServo(b_servo);
  }
}

void HTTP_handleRoot(void) {

  if ( server.hasArg("Makerlab") ) {
    Serial.println(server.arg("Makerlab"));
  }
  server.send ( 200, "text/html", "" );
  delay(1);
}

void handlingdata(String data) {
  b_speed = splitString(data, "Speed", "=", ",", 0);
  b_controller = splitString(data, "Controller", "=", ",", 0);
  b_servo = splitString(data, "Servo", "=", ",", 0);
}

String splitString(String v_G_motherString, String v_G_Command, String v_G_Start_symbol, String v_G_Stop_symbol, unsigned char v_G_Offset) {
  unsigned char lenOfCommand = v_G_Command.length();
  unsigned char posOfCommand = v_G_motherString.indexOf(v_G_Command);
  int PosOfStartSymbol = v_G_motherString.indexOf(v_G_Start_symbol, posOfCommand + lenOfCommand);

  while (v_G_Offset > 0){
    v_G_Offset--;
    PosOfStartSymbol = v_G_motherString.indexOf(v_G_Start_symbol, PosOfStartSymbol + 1);
  }

  int PosOfStopSymbol = v_G_motherString.indexOf(v_G_Stop_symbol, PosOfStartSymbol + 1);

  return v_G_motherString.substring(PosOfStartSymbol + 1, PosOfStopSymbol);
}

void goT() {
  Serial.println("Tiến");
  analogWrite(PWMA, Speed);
  analogWrite(PWMB, Speed);
  digitalWrite(DIRA, HIGH);
  digitalWrite(DIRB, HIGH);
}
void goTP() {
  Serial.println("Tiến phải");
  analogWrite(PWMA, Speed);
  analogWrite(PWMB, Speed / 2);
  digitalWrite(DIRA, HIGH);
  digitalWrite(DIRB, HIGH);
}
void goTT() {
  Serial.println("Tiến trái");
  analogWrite(PWMA, Speed / 2);
  analogWrite(PWMB, Speed);
  digitalWrite(DIRA, HIGH);
  digitalWrite(DIRB, HIGH);
}
void goL() {
  Serial.println("Lùi");
  analogWrite(PWMA, Speed);
  analogWrite(PWMB, Speed);
  digitalWrite(DIRA, LOW);
  digitalWrite(DIRB, LOW);
}
void goLP() {
  Serial.println("Lùi phải");
  analogWrite(PWMA, Speed / 2);
  analogWrite(PWMB, Speed);
  digitalWrite(DIRA, LOW);
  digitalWrite(DIRB, LOW);
}
void goLT() {
  Serial.println("Lùi trái");
  analogWrite(PWMA, Speed);
  analogWrite(PWMB, Speed / 2);
  digitalWrite(DIRA, LOW);
  digitalWrite(DIRB, LOW);
}
void goTr() {
  Serial.println("Trái");
  analogWrite(PWMA, 0);
  analogWrite(PWMB, Speed);
  digitalWrite(DIRA, HIGH);
  digitalWrite(DIRB, HIGH);
}
void goP() {
  Serial.println("Phải");
  analogWrite(PWMA, Speed);
  analogWrite(PWMB, 0);
  digitalWrite(DIRA, HIGH);
  digitalWrite(DIRB, HIGH);
}
void Stop() {
  Serial.println("Stop");
  analogWrite(PWMA, 0);
  analogWrite(PWMB, 0);
}

void checkServo(String b_pos){
  int val_servo = b_pos.toInt();
  car_servo.write(val_servo); 
}

void checkController(String _controller, String _speed) {
  /*
     Tiến 1
     Lùi 2
     Phải 3
     Trái 4
     Tiến Trái 5
     Tiến Phải 6
     Lùi Trái 7
     Lùi Phải 8
     Dừng 9
  */
  Speed = _speed.toInt() * 10.23;
  int controllers = _controller.toInt();
  switch (controllers)
  {
    case 1:
      goT();
      break;
    case 2:
      goL();
      break;
    case 3:
      goP();
      break;
    case 4:
      goTr();
      break;
    case 5:
      goTT();
      break;
    case 6:
      goTP();
      break;
    case 7:
      goLT();
      break;
    case 8:
      goLP();
      break;
    case 9:
      Stop();
      break;
  }
}

void servo_atack() {
  int pos;
  for (pos = 0; pos <= 180; pos += 1) {
    car_servo.write(pos);
    delay(15);
  }

  for (pos = 180; pos >= 0; pos -= 1) {
    car_servo.write(pos);
    delay(15);
  }

    for (pos = 0; pos <= 90; pos += 1) {
    car_servo.write(pos);
    delay(15);
  }

  
}
