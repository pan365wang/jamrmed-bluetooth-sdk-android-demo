# 智能血压计蓝牙 SDK-Android-20240615

# **一、使用范围**

android 开发工程师

# **二、方法**

**2.0 sdk 使用步骤：**

```
 1 Applicaton里调用MonitercenterManager._getInstance_().init(this); 初始化sdk
```

### **2.1、全局回调接口**

所有异步回调方法均在此接口实现。

通过

MonitercenterManager._getInstance_().registerBluetoothSettingChangeListener(this);
MonitercenterManager._getInstance_().registerDataChangeListener(this); 注册回调

**2.1.1、实现的接口名: IBluetoothStateChangeListener****  和 IDataChangeListener**

**2.1.2、示例**

```json
public class **BluetoothStateChangeListener implements IBluetoothStateChangeListener** {
   
    @Override
    public void onBluetoothAdapterStateChange(boolean currentState) {
        if (!currentState) {
          //蓝牙关闭
    
        } else {
          //蓝牙打开
        }
    }
    @Override
    public void onAdapterDiscoveryStarted() {
       //扫描开始
    }
    
    @Override
    public void onDeviceFound(BleDevice bleDevice) {
        Log._i_(_TAG_, "onDeviceFound name:" + bleDevice.getName() + " address:" + bleDevice.getAddress());
        //扫描到的蓝牙设备
    }
    
    @Override
    public void onAdapterDiscoveryFinished() {
         //蓝牙搜索结束
    }
    
    @Override
    public void onBleConnectedStateChanged(BleDevice bleDevice, int connectState, int reasonState) {
        Log._i_(_TAG_, "onBleConnectedStateChanged bleDevice:" + bleDevice.getName()
                + " address:" + bleDevice.getAddress() + "  connectState:" + connectState + "  reasonState:" + reasonState);
        //蓝牙连接状态        
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
       //启动测量 发送1 成功 0失败
    }
    
    @Override
    public void onStopMeasurStateChanged(int status) {
        Log._i_(_TAG_, "onStopMeasurStateChanged status:" + status);
       //停止测量 发送1 成功 0失败
    }
    
    @Override
    public void onSetVolumeStateChanged(int status) {
        Log._i_(_TAG_, "onSetVolumeStateChanged status:" + status);
        //设置音量 发送1 成功 0失败
    }
    
    @Override
    public void onQueryVolumeStateChanged(int status) {
        Log._i_(_TAG_, "onQuueryVolumeStateChanged status:" + status);
       //查询音量 发送1 成功 0失败
    }
    
    @Override
    public void onSendDateStateChanged(int status) {
        Log._i_(_TAG_, "onSendDateStateChanged status:" + status);
        //设置时间 发送1 成功 0失败
    }
    
    @Override
    public void onQueryBatteryStateChanged(int status) {
        Log._i_(_TAG_, "onQueryBatteryStateChanged status:" + status);
        //查询电量 发送1 成功 0失败
    }
    
    @Override
    public void onQueryHistoryDataStateChanged(int status) {
        Log._i_(_TAG_, "onQueryHistoryDataStateChanged status:" + status);
       //查询历史记录 发送1 成功 0失败
    }
    
    @Override
    public void onClearHistoryDataStateChanged(int status) {
        Log._i_(_TAG_, "onClearHistoryDataStateChanged status:" + status);
        //清除历史记录 发送1 成功 0失败
    }
    
    @Override
    public void onSendAckChanged(int status) {
        Log._i_(_TAG_, "onClearHistoryDataStateChanged status:" + status);
        //发送ack发送1 成功 0失败
    }
    
    @Override
    public void onUpdateRemindTaskStateChanged(int status) {
        Log._i_(_TAG_, "onUpdateRemindTaskStateChanged status:" + status);
               //设置任务提醒发送1 成功  0失败
    }
    
    @Override
    public void onCancelRemindTaskStateChanged(int status) {
        Log._i_(_TAG_, "onCancelRemindTaskStateChanged status:" + status);
        //取消任务提醒发送1 成功  0失败
    }
    
    @Override
    public void onHistoryData(MeasurBean measurBean) {
        Log._i_(_TAG_, "onHistoryData ");
        //历史测量数据
    }
    
    @Override
    public void onMeasurData(MeasurBean measurBean) {
        Log._i_(_TAG_, "onMeasurData ");
        //当前测量数据
    }
    
    @Override
    public void onMeasurPressureValue(double value) {
        Log._i_(_TAG_, "onMeasurPressureValue value：" + value);
         //当前测量压力值
    }
    
    @Override
    public void onBattery(int battery) {
        Log._i_(_TAG_, "onBattery battery：" + battery);
       //当前音量
    }
    
    @Override
    public void onVolume(int volume) {
        Log._i_(_TAG_, "onVolume volume：" + volume);
        //当前音量
    }
    
    @Override
    public void onError(int error) {
        Log._i_(_TAG_, "onError error：" + error);
        //测量错误码
    }

}


public class MeasurBean implements Serializable {

    public int sys1 ;//SYS收缩压值
    public int sys2;//SYS收缩压值
   
    public  int dia;//舒张压值
    public  int bpm; //心率值
    public  int afib; //房颤状态（0：无，1：有）
    public  int ihb ; //心率不齐状态（0：无，1：有）
    public  int as ;//动脉硬化（（0：无，1：有）
    public  int userId;//用户ID (0：用户1 1：用户2）
    public  int year; //APP年份-2000+(对应值）
    public  int month;//月份
    public  int day;//天数
    public  int hour;//小时
    public  int min;//分钟
    public  int sec;//秒
    public  int state ;//测量状态（0：升压  1：降压 2：双模 ）
    public  int battery ;//电量（XX %）如05：代表5%电量 APP显示百分比 OXFF: USB供电时无电量显示 ）
    public  int pMode ;//供电模式 （00：电池供电  01：USB供电 ）
    public  int rssi ;//蓝牙信号强度（-dbm ~0dbm）APP显示要加负号  0XFF: 历史数据上传
    
}


_/**_
_ * 蓝牙设备_
_ */_
public class BleDevice implements Serializable {
    _/**_
_     * 蓝牙名称_
_     */_
_    _private String name ;
    _/**_
_     * 蓝牙mac地址_
_     */_
_    _private String address ;
 
}
```

**2.1.3、蓝牙打开状态: onBluetoothAdapterStateChange**

**2.1.3.1、返回参数**

**2.1.3.2、示例**

```json
@Override
public void onBluetoothAdapterStateChange(boolean currentState) {
    if (!currentState) {
      //蓝牙关闭

    } else {
      //蓝牙打开
    }
}
```

**2.1.4、蓝牙开始扫描：onAdapterDiscoveryStarted**

**2.1.4.1、返回参数：无**

**2.1.4.2、示例**

```json
@Override
public void onAdapterDiscoveryStarted() {
    //开启搜索
}
```

**2.1.5、蓝牙扫描到的蓝牙设备：onDeviceFound（BleDevice bleDevice）**

**2.1.5.1、返回参数：BleDevice**

**2.1.5.2、示例**

```java
@Override
public void onDeviceFound(BleDevice bleDevice) {
  //搜索到的设备
}
```

**2.1.6、蓝牙扫描搜索结束：onAdapterDiscoveryFinished()**

**2.1.6.1、返回参数：**

**2.1.6.2、示例**

```java
@Override
public void onAdapterDiscoveryFinished() {
   //搜索结束
}
```

**2.1.7、蓝牙连接状态通知：onBleConnectedStateChanged(BleDevice bleDevice, int connectState, int reasonState)**

**2.1.7.1、返回参数：**

**2.1.7.2、示例**

```java
@Override
public void onBleConnectedStateChanged(BleDevice bleDevice, int connectState, int reasonState) {
    //连接状态
｝
```

**2.1.8、启动测量结果通知：onStartMeasurStateChanged(int status)**

**2.1.8.1、返回参数：**

**2.1.8.2、示例**

```java
@Override
public void onStartMeasurStateChanged(int status) {
    //启动测量
}
```

**2.1.9、停止测量结果通知：onStopMeasurStateChanged(int status)**

**2.1.9.1、返回参数：**

**2.1.9.2、示例**

```java
@Override
public void onStopMeasurStateChanged(int status) {
    Log._i_(_TAG_, "onStopMeasurStateChanged status:" + status);
    //停止测量
}
```

**2.1.10、设置音量结果通知：onSetVolumeStateChanged(int status)**

**2.1.10.1、返回参数：**

**2.1.10.2、示例**

```java
@Override
public void onSetVolumeStateChanged(int status) {
    Log._i_(_TAG_, "onSetVolumeStateChanged status:" + status);
    //设置音量通知
}
```

**2.1.11、查询音量结果通知：onQueryVolumeStateChanged(int status)**

**2.1.11.1、返回参数：**

**2.1.11.2、示例**

```java
@Override
public void onQueryVolumeStateChanged(int status) {
    //**查询**音量通知
   
}
```

**2.1.12、设置时间结果通知：onSendDateStateChanged(int status)**

**2.1.12.1、返回参数：**

**2.1.12.2、示例**

```java
@Override
public void onSendDateStateChanged(int status) {
   //**设置时间结果通知**
}
```

**2.1.13、查询电量结果通知：onQueryBatteryStateChanged(int status)**

**2.1.13.1、返回参数：**

**2.1.13.2、示例**

```java
@Override
public void onQueryBatteryStateChanged(int status) {
    //**查询电量结果通知**
}
```

**2.1.14、查询历史记录结果通知：onQueryHistoryDataStateChanged(int status)**

**2.1.14.1、返回参数：**

**2.1.14.2、示例**

```java
@Override
public void onQueryHistoryDataStateChanged(int status) {
   //**查询历史记录结果通知**
}
```

**2.1.15、清除历史记录结果通知：onClearHistoryDataStateChanged(int status)**

**2.1.15.1、返回参数：**

**2.1.15.2、示例**

```java
@Override
public void onClearHistoryDataStateChanged(int status) {
   //**清除历史记录结果通知**
}
```

**2.1.16、发送 ack 结果通知：onSendAckChanged(int status)**

**2.1.16.1、返回参数：**

**2.1.16.2、示例**

```java
@Override
public void onSendAckChanged(int status) {
  //**发送ack结果通知**
}
```

**2.1.17 设置任务提醒结果通知：onUpdateRemindTaskStateChanged(int status)**

**2.1.17.1、返回参数：**

**2.1.17.2、示例**

```java
@Override
public void onUpdateRemindTaskStateChanged(int status) {
   //**设置任务提醒结果通知**
}
```

**2.1.18 取消任务提醒结果通知：onCancelRemindTaskStateChanged(int status)**

**2.1.18.1、返回参数：**

**2.1.18.2、示例**

```java
@Override
public void onCancelRemindTaskStateChanged(int status) {
   //取消任务提醒
}
```

**2.1.19 请求获取历史记录结果通知：onHistoryData(MeasurBean measurBean)**

**2.1.19.1、返回参数：**

**2.1.19.2、示例**

```java
@Override
public void onHistoryData(MeasurBean measurBean) {
    //** 请求获取历史记录结果通知**
}
```

**2.1.20 当前测量数据记录结果通知：onMeasurData(MeasurBean measurBean)**

**2.1.0.1、返回参数：**

**2.1.20.2、示例**

```java
@Override
public void onMeasurData(MeasurBean measurBean) {
//**当前测量数据记录结果通知**
｝
```

**2.1.21 当前测量压力值结果通知：onMeasurPressureValue(double value)**

**2.1.21.1、返回参数：**

**2.1.21.2、示例**

```java
@Override
public void onMeasurPressureValue(double value) {
    //**当前测量压力值结果通知**
}
```

**2.1.2 当前测量压力值结果通知：onBattery(int battery)**

**2.1.22.1、返回参数：**

**2.1.22.2、示例**

```java
@Override
public void onBattery(int battery) {
    //电量通知
}
```

**2.1.23 当前测量压力值结果通知：onVolume(int volume)**

**2.1.23.1、返回参数：**

**2.1.23.2、示例**

```java
@Override
public void onVolume(int volume) {
    //音量值
}
```

**2.1.24 当前测量压力值结果通知：onError(int error)**

**2.1.24.1、返回参数：**

**2.1.24.2、示例**

```java
@Override
public void onError(int error) {
    //错误码
}
```

### **2.2、打开蓝牙模块**

打开蓝牙模块

**2.2.1、方法: openBluetoothAdapter**

**2.2.2、参数:****无**

**2.2.3、返回参数:**

通过 IBluetoothStateChangeListener 接口，监听 onBluetoothAdapterStateChange 方法

### **2.3、关闭蓝牙模块**

关闭蓝牙模块。调用该方法将断开所有已建立的连接并释放系统资源。建议在使用蓝牙流程后，与 `openBluetoothAdapter` 成对调用。

**2.3.1、方法: ****closeBluetoothAdapter**

**2.3.2、参数:****无**

**2.3.3、返回参数:**

通过 IBluetoothStateChangeListener 接口监听状态 onBluetoothAdapterStateChange 方法

### **2.4、扫描蓝牙设备**

开始搜寻附近的蓝牙外围设备。此操作比较耗费系统资源，请在搜索并连接到设备后调用 `stopBluetoothDevicesDiscovery` 方法停止搜索。

**2.4.1、方法: startBluetoothDevicesDiscovery**

**2.4.2、参数:**

**2.3.3、返回参数:****无，通过 IBluetoothStateChangeListener 接口监听状态**

**2.4.5 示例：**

```json
@Override
public void onAdapterDiscoveryStarted() {
  
    //扫描开始
}
```

### **2.5、扫描新设备的****监听****事件**

监听寻找到新设备的事件

**2.5.1、****IBluetoothStateChangeListener 接口****方法: ****onDeviceFound**

**2.5.2、参数:**

**2.5.3、返回参数:**

**2.5.5 示例：**

```json
@Override
public void onDeviceFound(BleDevice bleDevice) {
    Log._i_(_TAG_, "onDeviceFound name:" + bleDevice.getName() + " address:" + bleDevice.getAddress());
   
    //扫描到设备监听方法
}
```

### **2.6、停止扫描蓝牙设备**

停止搜寻附近的蓝牙外围设备。若已经找到需要的蓝牙设备并不需要继续搜索时，建议调用该接口停止蓝牙搜索。

**2.6.1、方法: stopBluetoothDevicesDiscovery**

**2.6.2、参数:****无**

**2.6.3、返回参数:****无，通过 IBluetoothStateChangeListener 接口监听状态**

**2.6.5 示例：**

```json
@Override
public void onAdapterDiscoveryFinished() {
    //扫描结束
}
```

### **2.7、监听蓝牙状态**

监听蓝牙适配器状态变化事件

**2.7.1、方法: ****void onBluetoothAdapterStateChange(boolean currentState);**

**2.7.2、参数:**

**2.7.5 示例：**

```json
@Override
public void onBluetoothAdapterStateChange(boolean currentState) {
    if (!currentState) {
      
        //蓝牙关闭
    } else {
      
        //蓝牙打开
    }
}
```

### **2.8、获取蓝牙状态**

获取本机蓝牙适配器状态。

**2.8.1、方法: getBluetoothAdapterState**

**2.8.2、参数:**

**2.8.3、返回参数:**

**2.8.4 示例：**

```json
MonitercenterManager._getInstance_().getBluetoothAdapterState() ；//获取蓝牙状态
```

### **2.9、设备连接**

连接低功耗蓝牙设备。

**2.9.1、方法:****createBLEConnect**

**2.9.2、参数:**

**2.9.3、返回参数:****无，通过 IBluetoothStateChangeListener 接口监听连接状态**

**2.9.4 示例：**

```json
@Override
public void onBleConnectedStateChanged(BleDevice bleDevice, int connectState, int reasonState) {
    Log._i_(_TAG_, "onBleConnectedStateChanged bleDevice:" + bleDevice.getName()
            + " address:" + bleDevice.getAddress() + "  connectState:" + connectState + "  reasonState:" + reasonState);
            //连接状态
    switch (connectState) {
        case BleDef._PROFILE_STATE_CONNECTED_: {
           
        }
        break;
        ｝
```

### **2.10、断开设备连接**

**2.10.1、方法:****closeBLEConnect**

**2.10.2、参数:**

**2.10.3、返回参数:****无，通过 IBluetoothStateChangeListener 接口监听连接状态**

**2.10.4 示例：**

```json
@Override
public void onBleConnectedStateChanged(BleDevice bleDevice, int connectState, int reasonState) {
    Log._i_(_TAG_, "onBleConnectedStateChanged bleDevice:" + bleDevice.getName()
            + " address:" + bleDevice.getAddress() + "  connectState:" + connectState + "  reasonState:" + reasonState);
        //连接状态            
    ｝
```

### **2.11、发送时间**

app 连接成功后，主动发送当前时间给设备

**2.11.1、方法:sendDate**

**2.11.2、参数:**

**2.11.3、返回参数:****无，通过 IDataChangeListener 接口，回调 void onSendDateStateChanged(int status); 方法通知是否设置成功，status1 成功，0 失败**

**2.11.5 示例：**

```json
@Override
public void onSendDateStateChanged(int status) {
    Log._i_(_TAG_, "onSendDateStateChanged status:" + status);
    
    // status == 1 ? "设置时间成功" : "设置时间失败";
   
}
```

### **2.12、开始测量**

发送开始测量指令

**2.12.1、方法:startMeasuring**

**2.12.2、参数:**

**2.12.3、返回参数:****无，通过 IDataChangeListener 接口，回调 void onStartMeasurStateChanged(int status); 方法通知是否启动成功，status1 成功，0 失败**

**2.12.5 示例：**

```json
@Override
public void onStartMeasurStateChanged(int status) {
    Log._i_(_TAG_, "onStartMeasurStateChanged status:" + status);
  
   // status == 1 ? "启动设备测量成功" : "启动设备测量失败";
  
}
```

### **2.13、结束测量**

在测量的过程中，发送结束（终止）测量指令

**2.13.1、方法:stopMeasuring**

**2.13.2、参数:**

**2.13.3、返回参数:****无，通过 IDataChangeListener 接口，回调 void onStopMeasurStateChanged(int status); 方法通知是否停止成功，status1 成功，0 失败**

**2.13.5 示例：**

```json
@Override
public void onStopMeasurStateChanged(int status) {
    Log._i_(_TAG_, "onStopMeasurStateChanged status:" + status);
   
   // status == 1 ? "停止设备测量成功" : "停止设备测量失败";
 
}
```

### **2.14、设置音量**

(0/1/2/3 档）

静音：NUM=0  默认：NUM=2

**2.14.1、方法:****setVolume**

**2.14.2、参数:**

**2.14.3、返回参数:****无，通过 IDataChangeListener 接口，回调 void onSetVolumeStateChanged(int status); 方法通知是否设置成功成功，status1 成功，0 失败**

**2.14.5 示例：**

```json
@Override
public void onSetVolumeStateChanged(int status) {
    Log._i_(_TAG_, "onSetVolumeStateChanged status:" + status);

   // = status == 1 ? "设置音量成功" : "设置音量失败";
 
}
```

### **2.15、获取音量**

(0/1/2/3 档）

静音：NUM=0  默认：NUM=2

**2.15.1、方法:getVol****ume**

**2.15.2、参数:**

**2.15.3、返回参数:****无，通过 IDataChangeListener 接口，回调 void onQueryVolumeStateChanged(int status); 方法通知是否获取成功，status1 成功，0 失败，音量数据通过 void onVolume(int volume) 接口通知音量大小**

**2.15.5 示例：**

```json
@Override
public void onQueryVolumeStateChanged(int status) {
    Log._i_(_TAG_, "onQuueryVolumeStateChanged status:" + status);
    
    //message.obj = status == 1 ? "获取音量成功" : "获取音量失败";
   
}

@Override
public void onVolume(int volume) {
    Log._i_(_TAG_, "onVolume volume：" + volume);
  
    //message.obj = "接收音量:" + volume;
   
}
```

### **2.16、获取电量**

获取设备当前电量，单位：%

**2.16.1、方法:getBattery**

**2.16.2、参数:**

**2.16.3、返回参数:****无，通过 IDataChangeListener 接口，回调 void onSendDateStateChanged(int status); 方法通知是否获取成功，status1 成功，0 失败，电量数据通过 void onBattery(int battery) 接口通知音量大小**

**2.16.5 示例：**

```json
@Override
public void onQueryBatteryStateChanged(int status) {
    Log._i_(_TAG_, "onQueryBatteryStateChanged status:" + status);
    // status == 1 ? "查询电量成功" : "查询电量失败";
}

@Override
public void onBattery(int battery) {
    Log._i_(_TAG_, "onBattery battery：" + battery);
   
   // "接收电量:" + battery;
}
```

### **2.17、获取历史数据**

主动发起获取历史记录的方法

**2.17.1、方法:getHistoryData**

**2.17.2、参数:**

**2.17.3、返回参数:****无，通过 IDataChangeListener 接口，回调 void onQueryHistoryDataStateChanged(int status); 方法通知是否获取成功，status1 成功，0 失败，电量数据通过 void onHistoryData(MeasurBean measurBean) 接口通知历史数据**

**2.17.5 示例：**

```json
@Override
public void onQueryHistoryDataStateChanged(int status) {
    Log._i_(_TAG_, "onQueryHistoryDataStateChanged status:" + status);
   
   // status == 1 ? "查询历史记录成功" : "查询历史记录失败";
   
}

@Override
public void onHistoryData(MeasurBean measurBean) {
    Log._i_(_TAG_, "onHistoryData ");
    //历史测量数据，全局接口有MeasureBean定义
｝
```

### **2.18、删除历史数据**

主动发起删除历史数据

**2.18.1、方法:clearHistoryData**

**2.18.2、参数:**** **

**2.18.3、返回参数:****无，通过 IDataChangeListener 接口，回调 void onClearHistoryDataStateChanged(int status); 方法通知是否获取成功**

**2.18.5 示例：**

```json
@Override
public void onClearHistoryDataStateChanged(int status) {
    Log._i_(_TAG_, "onClearHistoryDataStateChanged status:" + status);
    
    // status == 1 ? "清除历史记录成功" : "清除历史记录失败";

}
```

### **2.19、设置提醒任务**

主动发起删除历史数据

**2.19.1、方法:setRemindTask**

**2.19.2、参数:**

**2.17.3、返回参数:****无，通过 IDataChangeListener 接口，回调 void onUpdateRemindTaskStateChanged(int status); 方法通知是否设置成功**

**2.19.4 示例：**

```json
@Override
public void onUpdateRemindTaskStateChanged(int status) {
    Log._i_(_TAG_, "onUpdateRemindTaskStateChanged status:" + status);
    
    // status == 1 ? "设置提醒任务成功" : "设置提醒任务失败";
}
```

### **2.20、取消提醒任务**

主动发起删除历史数据

**2.20.1、方法:cancelRemindTask**

**2.20.2、参数:**

**2.20.3、返回参数:****无，通过 IDataChangeListener 接口，回调 void onCancelRemindTaskStateChanged(int status); 方法通知是否设置成功**

**2.20.5 示例：**

```json
@Override
public void onCancelRemindTaskStateChanged(int status) {
    Log._i_(_TAG_, "onCancelRemindTaskStateChanged status:" + status);
   
    //status == 1 ? "取消提醒任务成功" : "取消提醒任务失败";
   
}
```

### **2.21、监听测量数据**

监听测量数据，包括测量数据，压力数据，错误测量数据

**2.21.1、****IDataChangeListener ****方法:****void onMeasurData(MeasurBean measurBean) 测量数据**

```
 void onMeasurPressureValue(double value) 压力值
```

**2.21.2、参数:无**

**2.21.3、返回参数:****MeasurBean **

**2.21.5 示例：**

```json
@Override
public void onMeasurData(MeasurBean measurBean) {
｝

@Override
public void onMeasurPressureValue(double value) {
    Log._i_(_TAG_, "onMeasurPressureValue value：" + value);
  
   // "接收当前压力值:" + value;
}
```

### **2.22、发送 ack 数据接收确认（获取历史数或者实时数据都需要发送 ack 指令确认）**

**2.22.1、方法:sendACK**

**2.22.2、参数:**

**2.22.3、返回参数:****无，通过 IDataChangeListener 接口，回调 void void onSendAckChanged(int status); 方法通知是否设置成功**

**2.22.5 示例：**

```json
@Override
public void onSendAckChanged(int status) {
    Log._i_(_TAG_, "onClearHistoryDataStateChanged status:" + status);
 
   // status == 1 ? "发送ack成功" : "发送ack失败";

}
```
