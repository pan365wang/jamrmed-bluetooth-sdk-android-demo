package com.haiwang.sphygmomanometerdemo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.haiwang.bluetooth.sphygmomanometer.BleDef;
import com.haiwang.bluetooth.sphygmomanometer.event.MonitercenterManager;
import com.haiwang.bluetooth.sphygmomanometer.impl.IBluetoothStateChangeListener;
import com.haiwang.bluetooth.sphygmomanometer.impl.IDataChangeListener;
import com.haiwang.bluetooth.sphygmomanometer.model.BleDevice;
import com.haiwang.bluetooth.sphygmomanometer.model.MeasurBean;
import com.haiwang.bluetooth.sphygmomanometer.util.TypeConversion;

public class MainActivity extends AppCompatActivity implements IBluetoothStateChangeListener, IDataChangeListener, View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Button btSearch;
    private TextView tvCurConState;
    private TextView tvName;
    private TextView tvAddress;
    private Button btConnect;
    private Button btDisconnect;


    private TextView tvSendResult;
    private TextView tvReceive;
    private LinearLayout llDeviceList;
    private LinearLayout llDataSendReceive,llytConnect,llytData;
    private ListView lvDevices;
    private LVDevicesAdapter lvDevicesAdapter;
    private static final int CONNECT_SUCCESS = 0x01;
    private static final int CONNECT_FAILURE = 0x02;
    private static final int DISCONNECT_SUCCESS = 0x03;
    private static final int SEND_SUCCESS = 0x04;
    private static final int SEND_FAILURE = 0x05;
    private static final int RECEIVE_SUCCESS = 0x06;
    private static final int RECEIVE_FAILURE = 0x07;
    private static final int START_DISCOVERY = 0x08;
    private static final int STOP_DISCOVERY = 0x09;
    private static final int DISCOVERY_DEVICE = 0x0A;
    private static final int DISCOVERY_OUT_TIME = 0x0B;
    private static final int SELECT_DEVICE = 0x0C;
    private static final int BT_OPENED = 0x0D;
    private static final int BT_CLOSED = 0x0E;
    private BleDevice curBluetoothDevice;  //当前连接的设备
    //当前设备连接状态
    private boolean curConnState = false;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case START_DISCOVERY:
                    Log.d(TAG, "开始搜索设备...");
                    break;

                case STOP_DISCOVERY:
                    Log.d(TAG, "停止搜索设备...");
                    break;

                case DISCOVERY_DEVICE:  //扫描到设备
                    BleDevice bleDevice = (BleDevice) msg.obj;
                    lvDevicesAdapter.addDevice(bleDevice);

                    break;

                case SELECT_DEVICE:
                    BleDevice bluetoothDevice = (BleDevice) msg.obj;
                    tvName.setText(bluetoothDevice.getName());
                    tvAddress.setText(bluetoothDevice.getAddress());
                    curBluetoothDevice = bluetoothDevice;
                    break;

                case CONNECT_FAILURE: //连接失败
                    Log.d(TAG, "连接失败");
                    tvCurConState.setText("connection failed");
                    curConnState = false;
                    break;

                case CONNECT_SUCCESS:  //连接成功
                    Log.d(TAG, "连接成功");
                    tvCurConState.setText("Connection successful");
                    curConnState = true;
                    llDataSendReceive.setVisibility(View.VISIBLE);
                    llDeviceList.setVisibility(View.GONE);
                    llytData.setVisibility(View.VISIBLE);
                    break;

                case DISCONNECT_SUCCESS:
                    Log.d(TAG, "断开成功");
                    tvCurConState.setText("Successfully disconnected");
                    curConnState = false;
                    lvDevicesAdapter.clear();
                    tvName.setText("");
                    tvAddress.setText("");
                    llDeviceList.setVisibility(View.VISIBLE);
                    llytData.setVisibility(View.GONE);
                    curBluetoothDevice = null;
                    break;

                case SEND_FAILURE: //发送失败
                    String sendBufFail = (String) msg.obj;
                    tvSendResult.setText("Sending data failed：" + sendBufFail+ "\n" + tvSendResult.getText().toString().trim());
                    break;

                case SEND_SUCCESS:  //发送成功
                    String sendBufSuc = (String) msg.obj;
                    tvSendResult.setText("Successfully sent data：" + sendBufSuc + "\n" + tvSendResult.getText().toString().trim());
                    break;

                case RECEIVE_FAILURE: //接收失败
                    String receiveError = (String) msg.obj;
                    tvReceive.setText(receiveError);
                    break;
                case RECEIVE_SUCCESS:  //接收成功
                    String recBufSuc = (String) msg.obj;
                    tvReceive.setText( recBufSuc+ " \n" + tvReceive.getText().toString());
                    break;

                case BT_CLOSED:
                    Log.d(TAG, "系统蓝牙已关闭");
                    tvReceive.setText("Bluetooth turned off"+ " \n" + tvReceive.getText().toString() );
                    llytConnect.setVisibility(View.GONE);
                    llytData.setVisibility(View.GONE);
                    tvName.setText("");
                    tvAddress.setText("");
                    lvDevicesAdapter.clear();
                    llDeviceList.setVisibility(View.VISIBLE);
                    llytData.setVisibility(View.GONE);
                    curBluetoothDevice = null;
                    break;

                case BT_OPENED:
                    Log.d(TAG, "系统蓝牙已打开");
                    tvReceive.setText("Bluetooth is turned on" + " \n" + tvReceive.getText().toString());
                    llytConnect.setVisibility(View.VISIBLE);
                    llytData.setVisibility(View.GONE);
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        iniListener();
        MonitercenterManager.getInstance().registerBluetoothSettingChangeListener(this);
        MonitercenterManager.getInstance().registerDataChangeListener(this);
        MonitercenterManager.getInstance().initPermissions(this);
        initData();
    }


    /**
     * 初始化视图
     */
    private void initView() {
        btSearch = findViewById(R.id.bt_search);
        tvCurConState = findViewById(R.id.tv_cur_con_state);
        btConnect = findViewById(R.id.bt_connect);
        btDisconnect = findViewById(R.id.bt_disconnect);
        tvName = findViewById(R.id.tv_name);
        tvAddress = findViewById(R.id.tv_address);

        tvSendResult = findViewById(R.id.tv_send_result);
        tvReceive = findViewById(R.id.tv_receive_result);
        llDeviceList = findViewById(R.id.ll_device_list);
        llDataSendReceive = findViewById(R.id.ll_data_send_receive);
        lvDevices = findViewById(R.id.lv_devices);
        llytConnect = findViewById(R.id.llytConnect);
        llytData = findViewById(R.id.llytData) ;
    }

    /**
     * 初始化监听
     */
    private void iniListener() {
        btSearch.setOnClickListener(this);
        btConnect.setOnClickListener(this);
        btDisconnect.setOnClickListener(this);

        findViewById(R.id.bt_openbt).setOnClickListener(this);
        findViewById(R.id.bt_closebt).setOnClickListener(this);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
        findViewById(R.id.btn_setVol).setOnClickListener(this);
        findViewById(R.id.btn_getVol).setOnClickListener(this);
        findViewById(R.id.btn_battery).setOnClickListener(this);
        findViewById(R.id.btn_getHistory).setOnClickListener(this);
        findViewById(R.id.btn_clearHistory).setOnClickListener(this);
        findViewById(R.id.btn_set_remind_task).setOnClickListener(this);
        findViewById(R.id.btn_cancel_remind_task).setOnClickListener(this);
        findViewById(R.id.btn_set_time).setOnClickListener(this);



        lvDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BleDevice bleDevice = (BleDevice) lvDevicesAdapter.getItem(i);


                Message message = new Message();
                message.what = SELECT_DEVICE;
                message.obj = bleDevice;
                mHandler.sendMessage(message);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //列表适配器
        lvDevicesAdapter = new LVDevicesAdapter(MainActivity.this);
        lvDevices.setAdapter(lvDevicesAdapter);
        llytData.setVisibility(View.VISIBLE);
        //初始化ble管理器
        if (!MonitercenterManager.getInstance().checkBle(this)) {
            Log.d(TAG, "该设备不支持低功耗蓝牙");
            Toast.makeText(this, "This device does not support low-power Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (!MonitercenterManager.getInstance().getBluetoothAdapterState()) {
                llytConnect.setVisibility(View.GONE);
                llytData.setVisibility(View.GONE);
                //去打开蓝牙
                MonitercenterManager.getInstance().openBluetoothAdapter();
            }else {
                llytConnect.setVisibility(View.VISIBLE);
            }
        }
    }

    // 在你的Activity中调用这个方法来显示弹框
    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter search criteria");

        // 设置EditText为弹框的输入框
        final EditText input = new EditText(this);
        input.setText("BPM");
        builder.setView(input);

        // 设置确定按钮及其点击事件
        builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputText = input.getText().toString();
                // 这里处理输入的文本
                llDataSendReceive.setVisibility(View.GONE);
                llDeviceList.setVisibility(View.VISIBLE);
                searchBtDevice(inputText);
            }
        });

        // 设置取消按钮
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 这里处理取消事件
            }
        });

        builder.create().show();
    }

    //////////////////////////////////  搜索设备  /////////////////////////////////////////////////
    private void searchBtDevice(String prefix) {
        if (lvDevicesAdapter != null) {
            lvDevicesAdapter.clear();  //清空列表
        }

        //开始搜索
        MonitercenterManager.getInstance().startBluetoothDevicesDiscovery(prefix);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_openbt: {
                MonitercenterManager.getInstance().openBluetoothAdapter();
            }
            break;
            case R.id.bt_closebt: {
                MonitercenterManager.getInstance().closeBluetoothAdapter();
            }
            break;
            case R.id.bt_search:  //搜索蓝牙
                if(!MonitercenterManager.getInstance().getBluetoothAdapterState()){
                    MonitercenterManager.getInstance().openBluetoothAdapter();
                }else {
                    showInputDialog();
                }
                break;

            case R.id.bt_connect: //连接蓝牙
                if (!curConnState) {
                    if(curBluetoothDevice != null) {
                        MonitercenterManager.getInstance().createBLEConnect(curBluetoothDevice.getAddress(), 15000);
                    }else {
                        Toast.makeText(this, "Please search and select a device", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "The current device is connected", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bt_disconnect: //断开连接
                if (curConnState) {
                    if(curBluetoothDevice != null) {
                        MonitercenterManager.getInstance().closeBLEConnect(curBluetoothDevice.getAddress());
                    }else {
                        curConnState = false ;
                        Toast.makeText(this, "The current device is not connected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "The current device is not connected", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_start: {
                MonitercenterManager.getInstance().startMeasuring();
            }
            break;
            case R.id.btn_stop: {
                MonitercenterManager.getInstance().stopMeasuring();
            }
            break;
            case R.id.btn_setVol: {
               // MonitercenterManager.getInstance().setVolume(1);
                showVolumeNumberPicker();
            }
            break;
            case R.id.btn_getVol: {
                MonitercenterManager.getInstance().getVolume();
            }
            break;
            case R.id.btn_battery: {
                MonitercenterManager.getInstance().getBattery();
            }
            break;
            case R.id.btn_getHistory: {
                //MonitercenterManager.getInstance().getHistoryData(0);
                showHistoryNumberPicker() ;
            }
            break;
            case R.id.btn_clearHistory: {
                MonitercenterManager.getInstance().clearHistoryData();
            }
            break;
            case R.id.btn_set_remind_task: {
               // MonitercenterManager.getInstance().setRemindTask("2024-06-14", "2024-06-14", 14, 40);
                showStartTaskDateDialog();
            }
            break;
            case R.id.btn_cancel_remind_task: {
                MonitercenterManager.getInstance().cancelRemindTask();
            }
            break;
            case R.id.btn_set_time: {
               // MonitercenterManager.getInstance().sendDate("2024-06-14 14:36:33");
                showDateDialog();
            }
            break;

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showStartTaskDateDialog(){

        DatePickerDialog datePickerDialog=new DatePickerDialog(this);
        datePickerDialog.setTitle("Set start reminder date");
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                //i年 i1月 i2日
                //关闭窗体
                datePickerDialog.cancel();
                int m = i1 +1 ;
                String month = m>9?m+"":"0" + m;
                String day = i2>9?i2+"":"0" + i2;
                showEndTaskDateDialog(i+ "-" + month + "-" + day);
                Log.i(TAG," onDateSet year:" + i + "  month:" + i1 + "  day:" + i2);
            }
        });
        datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showEndTaskDateDialog(String startDate){

        DatePickerDialog datePickerDialog=new DatePickerDialog(this);
        datePickerDialog.setTitle("Set end reminder date");
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                //i年 i1月 i2日
                //关闭窗体
                datePickerDialog.cancel();
                int m = i1 +1 ;
                String month = m>9?m+"":"0" + m;
                String day = i2>9?i2+"":"0" + i2;
                showTaskTime(startDate,i+ "-" + month + "-" + day);
                Log.i(TAG," onDateSet year:" + i + "  month:" + i1 + "  day:" + i2);
            }
        });
        datePickerDialog.show();
    }

    public void showTaskTime(String startDate,String endDate){
        Log.i(TAG,"showTime startDate:" + startDate + "  endDate:" + endDate) ;
        TimePickerDialog timePickerDialog=new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                Log.i(TAG,"showTime i:" + i+  "  i1:" +i1) ;
                String hour = i>9?i+"":"0" + i;
                String min = i1>9?i1+"":"0" + i1;

                MonitercenterManager.getInstance().setRemindTask(startDate, endDate, i, i1);
            }
        },0,0,true);
        timePickerDialog.show();

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showDateDialog(){

        DatePickerDialog datePickerDialog=new DatePickerDialog(this);
        datePickerDialog.setTitle("Set time and date");
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                //i年 i1月 i2日
                //关闭窗体
                datePickerDialog.cancel();
                int m = i1 +1 ;
                String month = m>9?m+"":"0" + m;
                String day = i2>9?i2+"":"0" + i2;
                showTime(i+ "-" + month + "-" + day);
                Log.i(TAG," onDateSet year:" + i + "  month:" + i1 + "  day:" + i2);
            }
        });
        datePickerDialog.show();
    }

    public void showTime(String date){
        Log.i(TAG,"showTime date:" + date) ;
        TimePickerDialog timePickerDialog=new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                Log.i(TAG,"showTime i:" + i+  "  i1:" +i1) ;
                String hour = i>9?i+"":"0" + i;
                String min = i1>9?i1+"":"0" + i1;
                MonitercenterManager.getInstance().sendDate(date + " " + hour + ":" + min +":00" /*"2024-06-14 14:36:33"*/);
            }
        },0,0,true);
        timePickerDialog.show();

    }

    public  void showVolumeNumberPicker() {
        NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMinValue(0); // 设置最小值
        numberPicker.setMaxValue(3); // 设置最大值
        numberPicker.setValue(0); // 设置初始值
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        }); // 设置值改变监听器

        new AlertDialog.Builder(this)
                .setTitle("Select volume")
                .setView(numberPicker)
                .setPositiveButton("confirm", (dialog, which) -> {
                    // 处理确定按钮点击事件
                    Log.i(TAG,"which:" + numberPicker.getValue()  ) ;
                    MonitercenterManager.getInstance().setVolume( numberPicker.getValue());
                })
                .setNegativeButton("cancel", null)
                .show();
    }

    public  void showHistoryNumberPicker() {
        NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMinValue(0); // 设置最小值
        numberPicker.setMaxValue(3); // 设置最大值
        numberPicker.setValue(0); // 设置初始值
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        }); // 设置值改变监听器

        new AlertDialog.Builder(this)
                .setTitle("Select Users")
                .setView(numberPicker)
                .setPositiveButton("confirm", (dialog, which) -> {
                    // 处理确定按钮点击事件
                    Log.i(TAG,"which:" + numberPicker.getValue()  ) ;
                    MonitercenterManager.getInstance().getHistoryData( numberPicker.getValue());
                })
                .setNegativeButton("cancel", null)
                .show();
    }

    @Override
    public void onBluetoothAdapterStateChange(boolean currentState) {
        if (!currentState) {
            Message message = new Message();
            message.what = BT_CLOSED;
            mHandler.sendMessage(message);

        } else {
            Message message = new Message();
            message.what = BT_OPENED;
            mHandler.sendMessage(message);

        }
    }

    @Override
    public void onAdapterDiscoveryStarted() {
        Message message = new Message();
        message.what = START_DISCOVERY;
        mHandler.sendMessage(message);
    }

    @Override
    public void onDeviceFound(BleDevice bleDevice) {
        Log.i(TAG, "onDeviceFound name:" + bleDevice.getName() + " address:" + bleDevice.getAddress());
        Message message = new Message();
        message.what = DISCOVERY_DEVICE;
        message.obj = bleDevice;
        mHandler.sendMessage(message);
    }

    @Override
    public void onAdapterDiscoveryFinished() {
        Message message = new Message();
        message.what = STOP_DISCOVERY;
        mHandler.sendMessage(message);
    }

    @Override
    public void onBleConnectedStateChanged(BleDevice bleDevice, int connectState, int reasonState) {
        Log.i(TAG, "onBleConnectedStateChanged bleDevice:" + bleDevice.getName()
                + " address:" + bleDevice.getAddress() + "  connectState:" + connectState + "  reasonState:" + reasonState);
        switch (connectState) {
            case BleDef.PROFILE_STATE_CONNECTED: {
                Message message = new Message();
                message.what = CONNECT_SUCCESS;
                mHandler.sendMessage(message);
            }
            break;
            case BleDef.PROFILE_STATE_DISCONNECTED: {
                Message message = new Message();
                message.what = DISCONNECT_SUCCESS;
                message.obj = reasonState;
                mHandler.sendMessage(message);
            }
            break;
            case BleDef.PROFILE_STATE_CONNECTING: {

            }
            break;
            case BleDef.PROFILE_STATE_DISCONNECTING: {

            }
            break;
            case BleDef.PROFILE_STATE_CONNECTED_FAILED: {
                Message message = new Message();
                message.what = CONNECT_FAILURE;
                mHandler.sendMessage(message);
            }
            break;
            case BleDef.PROFILE_STATE_DISCONNECT_FAILED: {

            }
            break;
        }

    }

    @Override
    public void onStartMeasurStateChanged(int status) {
        Log.i(TAG, "onStartMeasurStateChanged status:" + status);
        Message message = new Message();
        message.what = status == 1 ? SEND_SUCCESS : SEND_FAILURE;
        message.obj = status == 1 ? "Successfully started measurement" : "Failed to start measurement";
        mHandler.sendMessage(message);
    }

    @Override
    public void onStopMeasurStateChanged(int status) {
        Log.i(TAG, "onStopMeasurStateChanged status:" + status);
        Message message = new Message();
        message.what = status == 1 ? SEND_SUCCESS : SEND_FAILURE;
        message.obj = status == 1 ? " measurement stopped successfully" : "Stopping measurement failed";
        mHandler.sendMessage(message);
    }

    @Override
    public void onSetVolumeStateChanged(int status) {
        Log.i(TAG, "onSetVolumeStateChanged status:" + status);
        Message message = new Message();
        message.what = status == 1 ? SEND_SUCCESS : SEND_FAILURE;
        message.obj = status == 1 ? "Successfully set volume" : "Setting volume failed";
        mHandler.sendMessage(message);
    }

    @Override
    public void onQueryVolumeStateChanged(int status) {
        Log.i(TAG, "onQuueryVolumeStateChanged status:" + status);
        Message message = new Message();
        message.what = status == 1 ? SEND_SUCCESS : SEND_FAILURE;
        message.obj = status == 1 ? "Successfully obtained volume" : "Failed to obtain volume";
        mHandler.sendMessage(message);
    }

    @Override
    public void onSendDateStateChanged(int status) {
        Log.i(TAG, "onSendDateStateChanged status:" + status);
        Message message = new Message();
        message.what = status == 1 ? SEND_SUCCESS : SEND_FAILURE;
        message.obj = status == 1 ? "Time set successfully" : "Setting time failed";
        mHandler.sendMessage(message);
    }

    @Override
    public void onQueryBatteryStateChanged(int status) {
        Log.i(TAG, "onQueryBatteryStateChanged status:" + status);
        Message message = new Message();
        message.what = status == 1 ? SEND_SUCCESS : SEND_FAILURE;
        message.obj = status == 1 ? "Successfully checked battery level" : "Failed to check battery level";
        mHandler.sendMessage(message);
    }

    @Override
    public void onQueryHistoryDataStateChanged(int status) {
        Log.i(TAG, "onQueryHistoryDataStateChanged status:" + status);
        Message message = new Message();
        message.what = status == 1 ? SEND_SUCCESS : SEND_FAILURE;
        message.obj = status == 1 ? "Query history successful" : "Failed to query history";
        mHandler.sendMessage(message);
    }

    @Override
    public void onClearHistoryDataStateChanged(int status) {
        Log.i(TAG, "onClearHistoryDataStateChanged status:" + status);
        Message message = new Message();
        message.what = status == 1 ? SEND_SUCCESS : SEND_FAILURE;
        message.obj = status == 1 ? "Clear history successful" : "Clear history successful";
        mHandler.sendMessage(message);
    }

    @Override
    public void onSendAckChanged(int status) {
        Log.i(TAG, "onSendAckChanged status:" + status);
        Message message = new Message();
        message.what = status == 1 ? SEND_SUCCESS : SEND_FAILURE;
        message.obj = status == 1 ? "Successfully sent ack" : "Sending ack failed";
        mHandler.sendMessage(message);

    }

    @Override
    public void onUpdateRemindTaskStateChanged(int status) {
        Log.i(TAG, "onUpdateRemindTaskStateChanged status:" + status);
        Message message = new Message();
        message.what = status == 1 ? SEND_SUCCESS : SEND_FAILURE;
        message.obj = status == 1 ? "Successfully set reminder task" : "Failed to set reminder task";
        mHandler.sendMessage(message);
    }

    @Override
    public void onCancelRemindTaskStateChanged(int status) {
        Log.i(TAG, "onCancelRemindTaskStateChanged status:" + status);
        Message message = new Message();
        message.what = status == 1 ? SEND_SUCCESS : SEND_FAILURE;
        message.obj = status == 1 ? "Cancel reminder task successfully" : "Cancel reminder task failed";
        mHandler.sendMessage(message);
    }

    @Override
    public void onHistoryData(MeasurBean measurBean) {
        Log.i(TAG, "onHistoryData ");
        String history = "sys:" + measurBean.sys +
                " dia:" + measurBean.dia + " bpm:" + measurBean.bpm + " fib:" + measurBean.afib + " ihb:" + measurBean.ihb +
                " as:" + measurBean.as + " userId:" + measurBean.userId + " year:" + measurBean.year + " month:" + measurBean.month + " day:" + measurBean.day +
                " hour:" + measurBean.hour + " min:" + measurBean.min + " sec:" + measurBean.sec + " state:" + measurBean.state + " battery:" + measurBean.battery +
                " pMode:" + measurBean.pMode + " rssi:" + measurBean.rssi;
        //rssi = 255 表示历史数据
        Message message = new Message();
        message.what = RECEIVE_SUCCESS;
        message.obj = "Receive History:" + history;
        mHandler.sendMessage(message);
        MonitercenterManager.getInstance().sendACK(); //需要调用，才不会重复接收到数据
    }

    @Override
    public void onMeasurData(MeasurBean measurBean) {
        Log.i(TAG, "onMeasurData ");
        String history = "sys:" + measurBean.sys +
                " dia:" + measurBean.dia + " bpm:" + measurBean.bpm + " fib:" + measurBean.afib + " ihb:" + measurBean.ihb +
                " as:" + measurBean.as + " userId:" + measurBean.userId + " year:" + measurBean.year + " month:" + measurBean.month + " day:" + measurBean.day +
                " hour:" + measurBean.hour + " min:" + measurBean.min + " sec:" + measurBean.sec + " state:" + measurBean.state + " battery:" + measurBean.battery +
                " pMode:" + measurBean.pMode + " rssi:" + measurBean.rssi;

        Message message = new Message();
        message.what = RECEIVE_SUCCESS;
        message.obj = "Receive current measurement records:" + history;
        mHandler.sendMessage(message);
        MonitercenterManager.getInstance().sendACK(); //需要调用，才不会重复接收到数据
    }

    @Override
    public void onMeasurPressureValue(double value) {
        Log.i(TAG, "onMeasurPressureValue value：" + value);
        Message message = new Message();
        message.what = RECEIVE_SUCCESS;
        message.obj = "Receive current pressure value:" + value;
        mHandler.sendMessage(message);
    }

    @Override
    public void onBattery(int battery) {
        Log.i(TAG, "onBattery battery:" + battery);
        Message message = new Message();
        message.what = RECEIVE_SUCCESS;
        message.obj = "Receiving power:" + battery;
        mHandler.sendMessage(message);
    }

    @Override
    public void onVolume(int volume) {
        Log.i(TAG, "onVolume volume:" + volume);
        Message message = new Message();
        message.what = RECEIVE_SUCCESS;
        message.obj = "Receive volume:" + volume;
        mHandler.sendMessage(message);
    }

    @Override
    public void onError(int error) {
        Log.i(TAG, "onError error：" + error);
        Message message = new Message();
        message.what = RECEIVE_SUCCESS;
        message.obj = "Error code:" + error;
        mHandler.sendMessage(message);
    }


}