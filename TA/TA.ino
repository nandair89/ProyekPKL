#include <Timer.h>
#include <Wire.h>
#include <OneWire.h>
#include <DallasTemperature.h>
#include <esp_wifi.h>
#include <Firebase_ESP_Client.h>
#include <ArduinoJson.h>
#include "addons/TokenHelper.h"
#include "addons/RTDBHelper.h"

//init wifi
char ssid[] = "OPPOA9";
char pass[] = "123467890";

//init firebase
#define DATABASE_URL "loginregisters-8d656-default-rtdb.firebaseio.com/";
#define API_KEY "AIzaSyCK5r-K7GbPMbL29WuP2Ad2ze5W1WLz7cA";

FirebaseData fbdo;

FirebaseAuth auth;
FirebaseConfig config;

Timer t;
String datakirim;
String dataKirim;

//init pin
#define smokeSensor 27
#define ledPin1 5
#define LOCK_PIN 2
const int oneWireBus = 4;

//init batasan dan sensor
int batas = 100;
OneWire oneWire(oneWireBus);
DallasTemperature sensors(&oneWire);
bool signupOK = false;
volatile bool isDoorLocked = true;

void setup() {
  Serial.begin(115200);
  sensors.begin();
  pinMode(smokeSensor, INPUT);
  pinMode(ledPin1, OUTPUT);
  digitalWrite(LOCK_PIN, HIGH);
  isDoorLocked = true;

  WiFi.begin(ssid, pass);
  while (WiFi.status()!= WL_CONNECTED) {
    Serial.println("Not-Konek");
    delay(500);
  }
  Serial.println("Tekoneksi ke WiFi");
  config.api_key = API_KEY;
  config.database_url = DATABASE_URL;
  if (Firebase.signUp(&config, &auth, "", "")) {
    Serial.println("ok");
    signupOK = true;
  } else {
    Serial.printf("%s\n", config.signer.signupError.message.c_str());
  }
  config.token_status_callback = tokenStatusCallback;
  Firebase.begin(&config, &auth);
  t.every(2000, kirimdata);
  Firebase.reconnectWiFi(true);
}

void loop() {
  t.update();

  //sensor asap
  int analogSensor = analogRead(smokeSensor);

  int mappedppmValue = (analogSensor/4.095);
  Serial.print("PPM: ");
  Serial.println(mappedppmValue);

  Serial.print("Nilai Analog = ");
  Serial.println(analogSensor);
  Serial.print("Smoke = ");
  Serial.println(analogSensor-50);
  
  if(analogSensor-50 >= batas){
    dataKirim = String(analogSensor-50);
  } else {
    dataKirim = String(analogSensor-50);
  }

  //sensor Suhu
  sensors.requestTemperatures(); 
  float temperatureC = sensors.getTempCByIndex(0);
  float temperatureF = sensors.getTempFByIndex(0);
  Serial.print(temperatureC);
  Serial.println("ºC");
  datakirim = String(temperatureC) + "ºC";
  //Serial.print(temperatureF);
  //Serial.println("ºF");
  delay(500);

  //cek pin
  if (Firebase.RTDB.getString(&fbdo, "doorStatus")) {
    if (fbdo.dataType() == "string" && fbdo.stringData().length() > 0) {
      String doorStatus = fbdo.stringData();
      if (doorStatus == "open") {
        unlockDoor();
        isDoorLocked = false;
      } else if (doorStatus == "close") {
        lockDoor();
        isDoorLocked = true;
      }
    }
  }
  Serial.println();
}

//fungsi untuk kirim data
void kirimdata(){
  Firebase.RTDB.setString(&fbdo,"Sensor/Sensor1", dataKirim);
  Firebase.RTDB.setString(&fbdo,"Sensor/Sensor2", datakirim);
}

//fungsi tutup
void lockDoor() {
  digitalWrite(LOCK_PIN, HIGH); // Mengunci pintu
  digitalWrite(ledPin1, HIGH);
  Serial.println("Door locked");
}

void unlockDoor() {
  digitalWrite(LOCK_PIN, LOW); // Membuka pintu
  digitalWrite(ledPin1, LOW);
  Serial.println("Door unlocked");
}