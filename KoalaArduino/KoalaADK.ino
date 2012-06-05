#include <Max3421e.h>
#include <Usb.h>
#include <AndroidAccessory.h>

#define uint8 unsigned char 
#define uint16 unsigned int
#define uint32 unsigned long int

AndroidAccessory acc("Manufacturer","Model","Description","1.0", "http://rosproxy.appspot.com/", "0000000012345678");

void setup()  {
  Serial.begin(9600); // for debug
  Serial1.begin(9600);
  acc.powerOn();
} 

void readMessage() {
  byte message[255];
  if (acc.isConnected()) {
    int length = acc.read(message, sizeof(message), 1); // read data into msg variable
    if (length > 0) {
      // message[0] is type, message[1] is length and the rest is value
      writeMessageToSerial(&message[2], message[1]);
    }
  }  
}

void writeMessageToSerial(byte message[], byte length) {
  Serial1.write(message, length);
  Serial.write(message, length); // for debug  
}

boolean requestSent = false;
long previousMillis = 0;
long interval = 500; //update sensors every 1s

void loop()  { 
  
  readMessage();
  
  // check sensors
  if (acc.isConnected()) {
    unsigned long currentMillis = millis();
    if(currentMillis - previousMillis > interval) {
      previousMillis = currentMillis;
      if(!requestSent) {
        Serial1.print("N\n");
        requestSent = true;
      } else {
        requestSent = false;
        
        byte result[1024];
        int bytesReaded = -1;
    
        byte incomingByte = -1;
        while (Serial1.available() > 0) {
          // read the incoming byte:
          incomingByte = Serial1.read();
          bytesReaded++;
          result[bytesReaded] = incomingByte;
        }
        if (result[0] == 'n') {
          acc.write(result, bytesReaded);
          Serial.write(result, bytesReaded);
          Serial.print("\n");
        }
      }
    }
  }
  
}
