#include <Wire.h>
#include <MPU6050.h>
#include <ESP8266WiFi.h>

MPU6050 mpu;

const char* ssid = "ssid wifi";
const char* password = "password wifi";
WiFiServer server(80);

const int buttonPin = D3; // Pin al quale è collegato il pulsante
bool drift = false;

int16_t ax_offset = 0;
int16_t ay_offset = 0;
int16_t az_offset = 0;
const int threshold = 1000; // Valore di soglia per ignorare il rumore

void calibrateMPU() {
  int32_t ax_sum = 0;
  int32_t ay_sum = 0;
  int32_t az_sum = 0;
  const int num_samples = 100;

  for (int i = 0; i < num_samples; ++i) {
    int16_t ax, ay, az, gx, gy, gz;
    mpu.getMotion6(&ax, &ay, &az, &gx, &gy, &gz);
    ax_sum += ax;
    ay_sum += ay;
    az_sum += az;
    delay(10);
  }

  ax_offset = ax_sum / num_samples;
  ay_offset = ay_sum / num_samples;
  az_offset = az_sum / num_samples;
}

void setup() {
  Serial.begin(115200);
  Wire.begin();
  mpu.initialize();
  pinMode(buttonPin, INPUT_PULLUP); // Imposta il pin del pulsante come input con pull-up
  if (!mpu.testConnection()) {
    Serial.println("MPU6050 connection failed");
    while (1);
  }

  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("WiFi connected");

  // Stampa l'indirizzo IP
  Serial.print("ESP8266 IP Address: ");
  Serial.println(WiFi.localIP());

  // Calibrazione
  calibrateMPU();

  server.begin();
}

void loop() {
  WiFiClient client = server.available();
  if (client) {
    while (client.connected()) {
      int16_t ax, ay, az, gx, gy, gz;
      mpu.getMotion6(&ax, &ay, &az, &gx, &gy, &gz);

      // Applica gli offset
      ax -= ax_offset;
      ay -= ay_offset;
      az -= az_offset;

      // Filtra il rumore
      if (abs(ax) < threshold) ax = 0;
      if (abs(ay) < threshold) ay = 0;
      if (abs(az) < threshold) az = 0;

      // Leggi lo stato del pulsante
      drift = (digitalRead(buttonPin) == LOW); // Pulsante premuto

      // Debug dei valori letti
      Serial.print("ax: ");
      Serial.print(ax);
      Serial.print(", ay: ");
      Serial.print(ay);
      Serial.print(", az: ");
      Serial.print(az);
      Serial.print(", drift: ");
      Serial.println(drift);

      String driftStr = drift ? "1" : "0";
      String data = String(ax) + "," + String(ay) + "," + String(az) + "," + String(gx) + "," + String(gy) + "," + String(gz) + "," + driftStr;
      client.println(data);
      delay(100);
    }
    client.stop();
  }
}
