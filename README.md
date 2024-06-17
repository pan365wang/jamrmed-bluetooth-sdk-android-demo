# Smart Blood Pressure Monitor with Bluetooth SDK-Android-20240615

# **One、Scope of Use**

android Development Engineer

# **two、Method**

**2.0 sdk Steps for Use：**

```
 1 Applicaton Invocation MonitercenterManager._getInstance_().init(this);  Initialize SDK
```

### **2.1、Global Callback Interface**

All asynchronous callback methods are implemented in this interface.

Through

MonitercenterManager._getInstance_().registerBluetoothSettingChangeListener(this);
MonitercenterManager._getInstance_().registerDataChangeListener(this); Register Callback

**2.1.1、Name of the implemented interface: IBluetoothStateChangeListener****  and IDataChangeListener**

**2.1.2、Example**

```json
public class **BluetoothStateChangeListener implements IBluetoothStateChangeListener** {
   
    @Override
    public void onBluetoothAdapterStateChange(boolean currentState) {
        if (!currentState) {
          //Bluetooth turned off
    
        } else {
          //Bluetooth turned on
        }
    }
    @Override
    public void onAdapterDiscoveryStarted() {
       //Scanning started
    }
    
    @Override
    public void onDeviceFound(BleDevice bleDevice) {
        Log._i_(_TAG_, "onDeviceFound name:" + bleDevice.getName() + " address:" + bleDevice.getAddress());
        //Bluetooth devices scanned
    }
    
    @Override
    public void onAdapterDiscoveryFinished() {
         //Bluetooth scan finished
    }
    
    @Override
    public void onBleConnectedStateChanged(BleDevice bleDevice, int connectState, int reasonState) {
        Log._i_(_TAG_, "onBleConnectedStateChanged bleDevice:" + bleDevice.getName()
                + " address:" + bleDevice.getAddress() + "  connectState:" + connectState + "  reasonState:" + reasonState);
        //Bluetooth connection status
        switch (connectState) {
            case BleDef._PROFILE_STATE_CONNECTED_: {
               
            }
            break;
            case BleDef._PROFILE_STATE_DISCONNECTED_: {
               
            }
            break;
            case BleDef._PROFILE_STATE_CONNECTING_: {
    
            }
            break;
            case BleDef._PROFILE_STATE_DISCONNECTING_: {
    
            }
            break;
            case BleDef._PROFILE_STATE_CONNECTED_FAILED_: {
               
            }
            break;
            case BleDef._PROFILE_STATE_DISCONNECT_FAILED_: {
    
            }
            break;
        }

    }
}

public class **DataChangeListener implements IDataChangeListener**{
    @Override
    public void onStartMeasurStateChanged(int status) {
        Log._i_(_TAG_, "onStartMeasurStateChanged status:" + status);
       //Start measurement: Sending 1 for success, 0 for failure
    }
    
    @Override
    public void onStopMeasurStateChanged(int status) {
        Log._i_(_TAG_, "onStopMeasurStateChanged status:" + status);
       //Stop measurement: Sending 1 for success, 0 for failure
    }
    
    @Override
    public void onSetVolumeStateChanged(int status) {
        Log._i_(_TAG_, "onSetVolumeStateChanged status:" + status);
        //Set volume: Sending 1 for success, 0 for failure
    }
    
    @Override
    public void onQueryVolumeStateChanged(int status) {
        Log._i_(_TAG_, "onQuueryVolumeStateChanged status:" + status);
       //Query volume: Send 1 for success, 0 for failure
    }
    
    @Override
    public void onSendDateStateChanged(int status) {
        Log._i_(_TAG_, "onSendDateStateChanged status:" + status);
        //Set time: Send 1 for success, 0 for failure
    }
    
    @Override
    public void onQueryBatteryStateChanged(int status) {
        Log._i_(_TAG_, "onQueryBatteryStateChanged status:" + status);
        //Query battery level: Send 1 for success, 0 for failure
    }
    
    @Override
    public void onQueryHistoryDataStateChanged(int status) {
        Log._i_(_TAG_, "onQueryHistoryDataStateChanged status:" + status);
       //Query history records: Send 1 for success, 0 for failure
    }
    
    @Override
    public void onClearHistoryDataStateChanged(int status) {
        Log._i_(_TAG_, "onClearHistoryDataStateChanged status:" + status);
        //Clear history records: Send 1 for success, 0 for failure
    }
    
    @Override
    public void onSendAckChanged(int status) {
        Log._i_(_TAG_, "onClearHistoryDataStateChanged status:" + status);
        //Send ACK (Acknowledgment): Send 1 for success, 0 for failure
    }
    
    @Override
    public void onUpdateRemindTaskStateChanged(int status) {
        Log._i_(_TAG_, "onUpdateRemindTaskStateChanged status:" + status);
               //Set task reminder: Send 1 for success, 0 for failure
    }
    
    @Override
    public void onCancelRemindTaskStateChanged(int status) {
        Log._i_(_TAG_, "onCancelRemindTaskStateChanged status:" + status);
        //Cancel task reminder: Send 1 for success, 0 for failure
    }
    
    @Override
    public void onHistoryData(MeasurBean measurBean) {
        Log._i_(_TAG_, "onHistoryData ");
        //Historical measurement data
    }
    
    @Override
    public void onMeasurData(MeasurBean measurBean) {
        Log._i_(_TAG_, "onMeasurData ");
        //Current measurement data
    }
    
    @Override
    public void onMeasurPressureValue(double value) {
        Log._i_(_TAG_, "onMeasurPressureValue value：" + value);
         //Current measured pressure value
    }
    
    @Override
    public void onBattery(int battery) {
        Log._i_(_TAG_, "onBattery battery：" + battery);
       //Current volume
    }
    
    @Override
    public void onVolume(int volume) {
        Log._i_(_TAG_, "onVolume volume：" + volume);
        //Current volume
    }
    
    @Override
    public void onError(int error) {
        Log._i_(_TAG_, "onError error：" + error);
        //Measurement error code
    }

}


public class MeasurBean implements Serializable {

    public int sys1 ;//SYS Systolic blood pressure
    public int sys2;//SYS Systolic pressure
   
    public  int dia;//Diastolic pressure
    public  int bpm; //Heart rate
    public  int afib; //Atrial fibrillation status (0: none, 1: present)
    public  int ihb ; //Irregular heart rate status (0: none, 1: present)
    public  int as ;//Arterial stiffness (0: none, 1: present)
    public  int userId;//User ID (0: User 1, 1: User 2)
    public  int year; //APP year-2000+(corresponding value)
    public  int month;//Month
    public  int day;//Days
    public  int hour;//Hours
    public  int min;//Minutes
    public  int sec;//Seconds
    public  int state ;//Measurement status (0: increasing pressure, 1: decreasing pressure, 2: dual mode)
    public  int battery ;//Battery level（XX %）If shown as "05", it represents 5% battery level. The app displays the percentage. "0xFF" indicates no battery display when powered by USB.
    public  int pMode ;//Power supply mode (00: battery powered, 01: USB powered)
    public  int rssi ;//Bluetooth signal strength (-dBm to 0dBm). Display in the app requires a negative sign. "0xFF" indicates historical data upload.
    
}


_/**_
_ * Bluetooth device_
_ */_
public class BleDevice implements Serializable {
    _/**_
_     * Bluetooth device_
_     */_
_    _private String name ;
    _/**_
_     * Bluetooth MAC address_
_     */_
_    _private String address ;
 
}
```

**2.1.3、Bluetooth status: on: onBluetoothAdapterStateChange**

**2.1.3.1、Return parameters**

**2.1.3.2、Example**

```json
@Override
public void onBluetoothAdapterStateChange(boolean currentState) {
    if (!currentState) {
      //Bluetooth disabled

    } else {
      //Bluetooth enabled
    }
}
```

**2.1.4、Bluetooth scanning started：onAdapterDiscoveryStarted**

**2.1.4.1、Return parameters: None**

**2.1.4.2、Example**

```json
@Override
public void onAdapterDiscoveryStarted() {
    //Start search
}
```

**2.1.5、Bluetooth devices found during Bluetooth scan：onDeviceFound（BleDevice bleDevice）**

**2.1.5.1、Return parameters：BleDevice**

**2.1.5.2、Example**

```java
@Override
public void onDeviceFound(BleDevice bleDevice) {
  //Detected devices
}
```

**2.1.6、The Bluetooth scan search is complete.：onAdapterDiscoveryFinished()**

**2.1.6.1、return parameter：**

**2.1.6.2、Example**

```java
@Override
public void onAdapterDiscoveryFinished() {
   //Search ended.
}
```

**2.1.7、Bluetooth connection status notification：onBleConnectedStateChanged(BleDevice bleDevice, int connectState, int reasonState)**

**2.1.7.1、return parameter：**

**2.1.7.2、Example**

```java
@Override
public void onBleConnectedStateChanged(BleDevice bleDevice, int connectState, int reasonState) {
    //Connection status
｝
```

**2.1.8、Start measurement result notification：onStartMeasurStateChanged(int status)**

**2.1.8.1、return parameter：**

**2.1.8.2、Example**

```java
@Override
public void onStartMeasurStateChanged(int status) {
    //Start measurement
}
```

**2.1.9、Stop measurement result notification：onStopMeasurStateChanged(int status)**

**2.1.9.1、return parameter：**

**2.1.9.2、Example**

```java
@Override
public void onStopMeasurStateChanged(int status) {
    Log._i_(_TAG_, "onStopMeasurStateChanged status:" + status);
    //Stop measurement
}
```

**2.1.10、Set volume result notification：onSetVolumeStateChanged(int status)**

**2.1.10.1、return parameter：**

**2.1.10.2、Example**

```java
@Override
public void onSetVolumeStateChanged(int status) {
    Log._i_(_TAG_, "onSetVolumeStateChanged status:" + status);
    //Volume setting notification
}
```

**2.1.11、Query volume result notification：onQueryVolumeStateChanged(int status)**

**2.1.11.1、return parameter：**

**2.1.11.2、Example**

```java
@Override
public void onQueryVolumeStateChanged(int status) {
    //**Query**Volume notification
   
}
```

**2.1.12、Set time result notification：onSendDateStateChanged(int status)**

**2.1.12.1、return parameter：**

**2.1.12.2、Example**

```java
@Override
public void onSendDateStateChanged(int status) {
   //**Set time result notification**
}
```

**2.1.13、Query battery level notification：onQueryBatteryStateChanged(int status)**

**2.1.13.1、return parameter：**

**2.1.13.2、Example**

```java
@Override
public void onQueryBatteryStateChanged(int status) {
    //**Query battery level result notification**
}
```

**2.1.14、Query history record result notification：onQueryHistoryDataStateChanged(int status)**

**2.1.14.1、return parameter：**

**2.1.14.2、Example**

```java
@Override
public void onQueryHistoryDataStateChanged(int status) {
   //**Query history record result notification**
}
```

**2.1.15、Clear history record result notification：onClearHistoryDataStateChanged(int status)**

**2.1.15.1、return parameter：**

**2.1.15.2、Example**

```java
@Override
public void onClearHistoryDataStateChanged(int status) {
   //**Clear history record result notification**
}
```

**2.1.16、Send ACK result notification：onSendAckChanged(int status)**

**2.1.16.1、return parameter：**

**2.1.16.2、Example**

```java
@Override
public void onSendAckChanged(int status) {
  //**Send ACK result notification**
}
```

**2.1.17 Set task reminder result notification：onUpdateRemindTaskStateChanged(int status)**

**2.1.17.1、return parameter：**

**2.1.17.2、Example**

```java
@Override
public void onUpdateRemindTaskStateChanged(int status) {
   //**Set task reminder result notification**
}
```

**2.1.18 Cancel task reminder result notification：onCancelRemindTaskStateChanged(int status)**

**2.1.18.1、return parameter：**

**2.1.18.2、Example**

```java
@Override
public void onCancelRemindTaskStateChanged(int status) {
   //Cancel task reminder
}
```

**2.1.19 Request for history record result notification：onHistoryData(MeasurBean measurBean)**

**2.1.19.1、return parameter：**

**2.1.19.2、Example**

```java
@Override
public void onHistoryData(MeasurBean measurBean) {
    //** Request history record result notification**
}
```

**2.1.20 Current measurement data record result notification：onMeasurData(MeasurBean measurBean)**

**2.1.0.1、return parameter：**

**2.1.20.2、Example**

```java
@Override
public void onMeasurData(MeasurBean measurBean) {
//**Current measurement data record result notification**
｝
```

**2.1.21 Current measurement pressure value result notification：onMeasurPressureValue(double value)**

**2.1.21.1、return parameter：**

**2.1.21.2、Example**

```java
@Override
public void onMeasurPressureValue(double value) {
    //**Current measurement pressure value result notification**
}
```

**2.1.2 Current measurement pressure value result notification：onBattery(int battery)**

**2.1.22.1、return parameter：**

**2.1.22.2、Example**

```java
@Override
public void onBattery(int battery) {
    //Battery notification
}
```

**2.1.23 Current measurement pressure value result notification：onVolume(int volume)**

**2.1.23.1、return parameter：**

**2.1.23.2、Example**

```java
@Override
public void onVolume(int volume) {
    //Volume level
}
```

**2.1.24 Current pressure measurement result notification：onError(int error)**

**2.1.24.1、return parameter：**

**2.1.24.2、Example**

```java
@Override
public void onError(int error) {
    //Error code
}
```

### **2.2、Enable Bluetooth module**

Enable Bluetooth module

**2.2.1、Method: openBluetoothAdapter**

**2.2.2、Parameter:****None**

**2.2.3、return parameter:**

Through IBluetoothStateChangeListener Interface，Listen onBluetoothAdapterStateChange Method

### **2.3、Disable Bluetooth module**

To turn off the Bluetooth module. Calling this method will disconnect all established connections and release system resources. It's recommended to use this after finishing the Bluetooth process `openBluetoothAdapter` Call in pairs。

**2.3.1、Method: ****closeBluetoothAdapter**

**2.3.2、Parameter:****None**

**2.3.3、return parameter:**

Through IBluetoothStateChangeListener Interface Listening status onBluetoothAdapterStateChange Method

### **2.4、Scan Bluetooth devices**

Start scanning for nearby Bluetooth peripheral devices. This operation consumes system resources, so please call it after searching for and connecting to devices. `stopBluetoothDevicesDiscovery` Method to stop searching.。

**2.4.1、Method: startBluetoothDevicesDiscovery**

**2.4.2、Parameter:**

**2.3.3、return parameter:****None，Through IBluetoothStateChangeListener Interface listening status**

**2.4.5 Example：**

```json
@Override
public void onAdapterDiscoveryStarted() {
  
    //Scan started
}
```

### **2.5、Scan for new devices****Listen****Event**

Listen for events when new devices are found.

**2.5.1、****IBluetoothStateChangeListener Interface****Method: ****onDeviceFound**

**2.5.2、Parameter:**

**2.5.3、return parameter:**

**2.5.5 Example：**

```json
@Override
public void onDeviceFound(BleDevice bleDevice) {
    Log._i_(_TAG_, "onDeviceFound name:" + bleDevice.getName() + " address:" + bleDevice.getAddress());
   
    //Scan for device listener method
}
```

### **2.6、Stop scanning Bluetooth devices**

Stop scanning nearby Bluetooth peripheral devices. It is recommended to call this interface to stop Bluetooth scanning when the desired Bluetooth device has been found and further scanning is not needed.。

**2.6.1、Method: stopBluetoothDevicesDiscovery**

**2.6.2、Parameter:****None**

**2.6.3、return parameter:****None，Through IBluetoothStateChangeListener Interface listens to status**

**2.6.5 Example：**

```json
@Override
public void onAdapterDiscoveryFinished() {
    //Scanning finished
}
```

### **2.7、Listen for Bluetooth status**

Listen for Bluetooth adapter state change events

**2.7.1、Method: ****void onBluetoothAdapterStateChange(boolean currentState);**

**2.7.2、Parameter:**

**2.7.5 Example：**

```json
@Override
public void onBluetoothAdapterStateChange(boolean currentState) {
    if (!currentState) {
      
        //Bluetooth turned off
    } else {
      
        //Bluetooth turned on
    }
}
```

### **2.8、Get Bluetooth status**

Get local Bluetooth adapter status。

**2.8.1、Method: getBluetoothAdapterState**

**2.8.2、parameter:**

**2.8.3、return parameter:**

**2.8.4 Example：**

```json
MonitercenterManager._getInstance_().getBluetoothAdapterState() ；//To get the Bluetooth status
```

### **2.9、Device connected**

Connect to a Bluetooth Low Energy (BLE) device。

**2.9.1、Method:****createBLEConnect**

**2.9.2、parameter:**

**2.9.3、return parameter:****None，Through IBluetoothStateChangeListener Interface listens for connection status**

**2.9.4 Example：**

```json
@Override
public void onBleConnectedStateChanged(BleDevice bleDevice, int connectState, int reasonState) {
    Log._i_(_TAG_, "onBleConnectedStateChanged bleDevice:" + bleDevice.getName()
            + " address:" + bleDevice.getAddress() + "  connectState:" + connectState + "  reasonState:" + reasonState);
            //Connection status
    switch (connectState) {
        case BleDef._PROFILE_STATE_CONNECTED_: {
           
        }
        break;
        ｝
```

### **2.10、Disconnect device connection**

**2.10.1、Method:****closeBLEConnect**

**2.10.2、parameter:**

**2.10.3、return parameter:****None，Through IBluetoothStateChangeListener Interface listens for connection status**

**2.10.4 Example：**

```json
@Override
public void onBleConnectedStateChanged(BleDevice bleDevice, int connectState, int reasonState) {
    Log._i_(_TAG_, "onBleConnectedStateChanged bleDevice:" + bleDevice.getName()
            + " address:" + bleDevice.getAddress() + "  connectState:" + connectState + "  reasonState:" + reasonState);
        //Connection status           
    ｝
```

### **2.11、Send time**

app After successful connection, actively send the current time to the device.

**2.11.1、Method:sendDate**

**2.11.2、parameter:**

**2.11.3、return parameter:****None，Through IDataChangeListener Interface，Callback void onSendDateStateChanged(int status); Method notification of successful setting，status1 Success，0 Failure**

**2.11.5 Example：**

```json
@Override
public void onSendDateStateChanged(int status) {
    Log._i_(_TAG_, "onSendDateStateChanged status:" + status);
    
    // status == 1 ? "Set time successfully" : "Failed to set time";
   
}
```

### **2.12、Start measurement**

Send start measurement command

**2.12.1、Method:startMeasuring**

**2.12.2、parameter:**

**2.12.3、return parameter:****None，Through IDataChangeListener Interface，Callback void onStartMeasurStateChanged(int status); Method notification of successful startup，status1 Success，0 Failure**

**2.12.5 Example：**

```json
@Override
public void onStartMeasurStateChanged(int status) {
    Log._i_(_TAG_, "onStartMeasurStateChanged status:" + status);
  
   // status == 1 ? "Device measurement started successfully" : "Failed to start device measurement";
  
}
```

### **2.13、End measurement**

Send terminate measurement command during measurement

**2.13.1、Method:stopMeasuring**

**2.13.2、parameter:**

**2.13.3、return parameter:****None，Through IDataChangeListener Interface，Callback void onStopMeasurStateChanged(int status); Method notification of successful stop，status1 Success，0 Failure**

**2.13.5 Example：**

```json
@Override
public void onStopMeasurStateChanged(int status) {
    Log._i_(_TAG_, "onStopMeasurStateChanged status:" + status);
   
   // status == 1 ? "Device measurement stopped successfully" : "Failed to stop device measurement";
 
}
```

### **2.14、Set volume**

(0/1/2/3 Level）

Mute：NUM=0  Default：NUM=2

**2.14.1、Method:****setVolume**

**2.14.2、parameter:**

**2.14.3、Returnparameter:****None，Through IDataChangeListener Interface，Callback void onSetVolumeStateChanged(int status); Method notification of successful setting，status1 Success，0 Failure**

**2.14.5 Example：**

```json
@Override
public void onSetVolumeStateChanged(int status) {
    Log._i_(_TAG_, "onSetVolumeStateChanged status:" + status);

   // = status == 1 ? "Volume set successfully" : "Failed to set volume";
 
}
```

### **2.15、Get volume**

(0/1/2/3 Level）

Mute：NUM=0  Default：NUM=2

**2.15.1、Method:getVol****ume**

**2.15.2、parameter:**

**2.15.3、Returnparameter:****None，Through IDataChangeListener Interface，Callback void onQueryVolumeStateChanged(int status); Method notification of successful retrieval，status1 Success，0 Failure，Volume data Through void onVolume(int volume)  Interface notifies volume level**

**2.15.5 Example：**

```json
@Override
public void onQueryVolumeStateChanged(int status) {
    Log._i_(_TAG_, "onQuueryVolumeStateChanged status:" + status);
    
    //message.obj = status == 1 ? "Volume retrieved successfully" : "Failed to retrieve volume";
   
}

@Override
public void onVolume(int volume) {
    Log._i_(_TAG_, "onVolume volume：" + volume);
  
    //message.obj = "Receive volume:" + volume;
   
}
```

### **2.16、Get battery level**

Get the current battery level of the device，unit：%

**2.16.1、Method:getBattery**

**2.16.2、parameter:**

**2.16.3、return parameter:****None，Through IDataChangeListener Interface，Callback void onSendDateStateChanged(int status); Method notification of successful retrieval，status1 Success，0 Failure，Battery data passed void onBattery(int battery) Interface notifies volume level**

**2.16.5 Example：**

```json
@Override
public void onQueryBatteryStateChanged(int status) {
    Log._i_(_TAG_, "onQueryBatteryStateChanged status:" + status);
    // status == 1 ? "Query battery level successful" : "Query battery level failed";
}

@Override
public void onBattery(int battery) {
    Log._i_(_TAG_, "onBattery battery：" + battery);
   
   // "Receive battery:" + battery;
}
```

### **2.17、Fetch historical data**

Initiate method to actively fetch historical records

**2.17.1、Method:getHistoryData**

**2.17.2、parameter:**

**2.17.3、return parameter:****None，Through IDataChangeListener Interface，Callback void onQueryHistoryDataStateChanged(int status); Method notification of successful retrieval，status1 Success，0 Failure，Battery data received void onHistoryData(MeasurBean measurBean) Interface notifies historical data**

**2.17.5 Example：**

```json
@Override
public void onQueryHistoryDataStateChanged(int status) {
    Log._i_(_TAG_, "onQueryHistoryDataStateChanged status:" + status);
   
   // status == 1 ? "Query history records successful" : "Query history records failed";
   
}

@Override
public void onHistoryData(MeasurBean measurBean) {
    Log._i_(_TAG_, "onHistoryData ");
    //Historical measurement data, available through the global interface. MeasureBean Definition
｝
```

### **2.18、Delete historical data**

Initiate deletion of historical data

**2.18.1、Method:clearHistoryData**

**2.18.2、parameter:**** **

**2.18.3、return parameter:****None，Through IDataChangeListener Interface，Callback void onClearHistoryDataStateChanged(int status);  Method notification of successful retrieval**

**2.18.5 Example：**

```json
@Override
public void onClearHistoryDataStateChanged(int status) {
    Log._i_(_TAG_, "onClearHistoryDataStateChanged status:" + status);
    
    // status == 1 ? "Clear history records successful" : "Clear history records failed";

}
```

### **2.19、Set reminder task**

Initiate deletion of historical data

**2.19.1、Method:setRemindTask**

**2.19.2、parameter:**

**2.17.3、return parameter:****None，Through IDataChangeListener Interface，Callback void onUpdateRemindTaskStateChanged(int status);  Method notification of successful setting**

**2.19.4 Example：**

```json
@Override
public void onUpdateRemindTaskStateChanged(int status) {
    Log._i_(_TAG_, "onUpdateRemindTaskStateChanged status:" + status);
    
    // status == 1 ? "Set reminder task successful" : "Set reminder task failed";
}
```

### **2.20、Cancel reminder task**

Initiate deletion of historical data

**2.20.1、Method:cancelRemindTask**

**2.20.2、parameter:**

**2.20.3、return parameter:****None，Through IDataChangeListener Interface，Callback void onCancelRemindTaskStateChanged(int status); Method notification of successful setting**

**2.20.5 Example：**

```json
@Override
public void onCancelRemindTaskStateChanged(int status) {
    Log._i_(_TAG_, "onCancelRemindTaskStateChanged status:" + status);
   
    //status == 1 ? "Cancel reminder task successful" : "Cancel reminder task failed";
   
}
```

### **2.21、Monitor measurement data**

Monitor measurement data, including measurement data, pressure data, and error measurement data.

**2.21.1、****IDataChangeListener ****Method:****void onMeasurData(MeasurBean measurBean) Measurement data**

```
 void onMeasurPressureValue(double value)  Pressure value
```

**2.21.2、Parameters: none**

**2.21.3、return parameter:****MeasurBean **

**2.21.5 Example：**

```json
@Override
public void onMeasurData(MeasurBean measurBean) {
｝

@Override
public void onMeasurPressureValue(double value) {
    Log._i_(_TAG_, "onMeasurPressureValue value：" + value);
  
   // "Receive current pressure value:" + value;
}
```

### **2.22、Send ACK for data reception confirmation (needed for fetching historical or real-time data).**

**2.22.1、Method:sendACK**

**2.22.2、parameter:**

**2.22.3、return parameter:****None，Through IDataChangeListener Interface，Callback void void onSendAckChanged(int status); Method notification of successful setting**

**2.22.5 Example：**

```json
@Override
public void onSendAckChanged(int status) {
  Log._i_(_TAG_, "onClearHistoryDataStateChanged status:" + status);

  // status == 1 ? "ACK sent successfully" : "Failed to send ACK";

}
```
