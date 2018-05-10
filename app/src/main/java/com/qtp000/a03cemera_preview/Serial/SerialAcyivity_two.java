package com.qtp000.a03cemera_preview.Serial;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;
import com.qtp000.a03cemera_preview.MainActivity_two;
import com.qtp000.a03cemera_preview.R;
import com.qtp000.a03cemera_preview.Socket_connect;
import com.qtp000.a03cemera_preview.ValuesApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 祁天培 on 2018/5/7.
 */

public class SerialAcyivity_two extends AppCompatActivity {

    // 接受传感器
    long psStatus = 0;// 状态
    long UltraSonic = 0;// 超声波
    long Light = 0;// 光照
    long CodedDisk = 0;// 码盘值
    private TextView Data_show =null;
    private EditText speededit = null;
    private  EditText coded_discedit =null;
    private ImageButton up_bt,blew_bt,stop_bt,left_bt,right_bt;
    // 速度与码盘值
    private int sp_n, en_n,angle_n;

    private  final int MESSAGE_REFRESH= 101;
    private  final long REFRESH_TIMEOUT_MILLIS = 5000;
    private int num =0;
    private UsbManager mUsbManager;
    private List<UsbSerialPort> mEntries = new ArrayList<UsbSerialPort>();
    private final String TAG = MainActivity_two.class.getSimpleName();
//    private Socket_connect sock_con;
    public Socket_connect sock_con;
    public static UsbSerialPort sPort = null;
    private byte[] mByte = new byte[11];

    Button to_main;

    ValuesApplication valuesApplication;
    Handler mainhandler = null;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_activity_main);
        mHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH, REFRESH_TIMEOUT_MILLIS);  //开始usb的获取
        sock_con =new Socket_connect();
        control_init();
        to_mainactivity();
        Transparent.showLoadingMessage(this,"加载中",false);   //显示等待对话框

        Log.i("SerialAcyivity_two.class:","加载中");
        valuesApplication = (ValuesApplication)getApplication();
        get_main_handler.start();
    }

    private void to_mainactivity(){
        to_main = findViewById(R.id.btn_main);
        to_main.setOnClickListener(new onClickListener3());

    }
    private class onClickListener3 implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {

            switch(v.getId())
            {
                case R.id.btn_main:
                    //Intent to mainactivity
                    Intent intent2main = new Intent(SerialAcyivity_two.this , MainActivity_two.class);
                    startActivity(intent2main);
                    Toast.makeText(SerialAcyivity_two.this,"进主页面",Toast.LENGTH_SHORT);



                    break;

            }
        }
    }

    Thread get_main_handler = new Thread(new Runnable() {
        @Override
        public void run() {
            while (valuesApplication.get_Mainhandler() == null){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mainhandler = valuesApplication.get_Mainhandler();
            }

    });

    private void control_init()
    {
        Data_show =(TextView) findViewById(R.id.rvdata);
        speededit =(EditText)findViewById(R.id.speed_data);
        coded_discedit =(EditText)findViewById(R.id.coded_disc_data);

        up_bt =(ImageButton) findViewById(R.id.up_button);
        blew_bt =(ImageButton) findViewById(R.id.below_button);
        stop_bt =(ImageButton) findViewById(R.id.stop_button);
        left_bt =(ImageButton) findViewById(R.id.left_button);
        right_bt =(ImageButton) findViewById(R.id.right_button);

        up_bt.setOnClickListener(new onClickListener2());
        blew_bt.setOnClickListener(new onClickListener2());
        stop_bt.setOnClickListener(new onClickListener2());
        left_bt.setOnClickListener(new onClickListener2());
        right_bt.setOnClickListener(new onClickListener2());
        up_bt.setOnLongClickListener(new onLongClickListener2());
    }

    //前进按钮长按实现寻迹的功能，短按实现前进的功能
    private class onClickListener2 implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {

            sp_n = getSpeed();

            switch(v.getId())
            {
                case R.id.up_button:
                    en_n = getEncoder();
                    sock_con.go(sp_n, en_n);
                    break;
                case R.id.left_button:
                    en_n = getEncoder();
                    sock_con.left(sp_n, en_n);
                    break;
                case R.id.right_button:
                    en_n = getEncoder();
                    sock_con.right(sp_n, en_n);
                    break;
                case R.id.below_button:
                    en_n = getEncoder();
                    sock_con.back(sp_n, en_n);
                    break;
                case R.id.stop_button:
                    sock_con.stop();
                    break;
            }
        }
    }
    //前进按钮长按实现寻迹的功能
    private class onLongClickListener2 implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            if (view.getId() == R.id.up_button) {
                sock_con.line(sp_n);//寻迹
            }
            return true;
        }
    }

    // 获取速度的方法
    private int getSpeed() {
        String src = speededit.getText().toString();
        int speed = 40;
        if (!src.equals("")) {
            speed = Integer.parseInt(src);
        } else {
            Toast.makeText(this, "请输入速度值", Toast.LENGTH_SHORT).show();
        }
        return speed;
    }
    //获取码盘方法
    private int getEncoder() {
        String src = coded_discedit.getText().toString();
        int encoder =20;
        if (!src.equals("")) {
            encoder = Integer.parseInt(src);
        } else {
            Toast.makeText(this, "请输入码盘值", Toast.LENGTH_SHORT).show();
        }
        return encoder;
    }



    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_REFRESH:
                    refreshDeviceList();//扫描usb列表，获取usb设备
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }

    };

    //获取usb相关的一些变量
    private void useUsbtoserial()
    {
        final UsbSerialPort port = mEntries.get(0);  //position =0
        final UsbSerialDriver driver = port.getDriver();
        final UsbDevice device = driver.getDevice();
        final String usbid = String.format("Vendor %s  ，Product %s",
                HexDump.toHexString((short) device.getVendorId()),
                HexDump.toHexString((short) device.getProductId()));
        SerialAcyivity_two.sPort = port;
        if(sPort !=null) {
            controlusb(); //使用usb功能   ////--------------------------------------------------
        }
    }


    private final BroadcastReceiver mUsbPermissionActionReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice usbDevice = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(null != usbDevice){
                            afterGetUsbPermission(usbDevice);
                        }
                    }
                    else {
                        Toast.makeText(context, String.valueOf("Permission denied for device" + usbDevice), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    };

    private void afterGetUsbPermission(UsbDevice usbDevice){
        Toast.makeText(SerialAcyivity_two.this, String.valueOf("Found USB device: VID=" + usbDevice.getVendorId() + " PID=" + usbDevice.getProductId()), Toast.LENGTH_LONG).show();
        doYourOpenUsbDevice(usbDevice);
    }

    //打开usb设备
    private void doYourOpenUsbDevice(UsbDevice usbDevice){
        connection = mUsbManager.openDevice(usbDevice);
    }


    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private UsbDeviceConnection connection;
    //  弹出选择对话框，尝试获取usb访问权限，否则就已经获取了usb权限，
    private void tryGetUsbPermission(){

        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbPermissionActionReceiver, filter);

        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        for (final UsbDevice usbDevice : mUsbManager.getDeviceList().values()) {
            if(mUsbManager.hasPermission(usbDevice)){  //已经获得了权限
                afterGetUsbPermission(usbDevice);
            }else{//还没有获取权限，弹出对话框，请求获取权限
                mUsbManager.requestPermission(usbDevice, mPermissionIntent);
            }
        }
    }

    //  //////////打开usb，  弹出选择对话框，尝试获取usb访问权限
    private void openUsbDevice(){
        tryGetUsbPermission();
    }

    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private SerialInputOutputManager mSerialIoManager;
    private void stopIoManager() {
        if (mSerialIoManager != null) {
            Log.e(TAG, "Stopping io manager ..");
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }


    // 接受显示小车发送的数据
    private Handler recvhandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mByte = (byte[]) msg.obj;
                if (mByte[0] == 0x55) {
                    // 光敏状态
                    psStatus = mByte[3] & 0xff;
                    // 超声波数据
                    UltraSonic = mByte[5] & 0xff;
                    UltraSonic = UltraSonic << 8;
                    UltraSonic += mByte[4] & 0xff;
                    // 光照强度
                    Light = mByte[7] & 0xff;
                    Light = Light << 8;
                    Light += mByte[6] & 0xff;
                    // 码盘
                    CodedDisk = mByte[9] & 0xff;
                    CodedDisk = CodedDisk << 8;
                    CodedDisk += mByte[8] & 0xff;

                    if (mByte[1] == (byte) 0xaa) {
                        // 显示数据
                        Data_show.setText("主车各状态信息: " + "超声波:" + UltraSonic
                                + "mm 光照:" + Light + "lx" + " 码盘:" + CodedDisk
                                + " 光敏状态:" + psStatus + " 状态:" + (mByte[2]));
                    }
                    if(mByte[1] == (byte) 0x02)
                    {
                        // 显示数据
                        Data_show.setText("从车各状态信息: " + "超声波:" + UltraSonic
                                + "mm 光照:" + Light + "lx" + " 码盘:" + CodedDisk
                                + " 光敏状态:" + psStatus + " 状态:" + (mByte[2]));
                    }
                }
            }
        }
    };

    private void updateReceivedData(byte[] data) {
        final String message = "Read " + data.length + " bytes: \n"
                + HexDump.dumpHexString(data) + "\n\n";
        Log.e("read data is :","  "+message);

    }

    private final SerialInputOutputManager.Listener mListener =  //读usb数据
            new SerialInputOutputManager.Listener() {
                @Override
                public void onRunError(Exception e) {
                    Log.e(TAG, "Runner stopped.");
                }

                @Override
                public void onNewData(final byte[] data) {   //读usb数据，通过usb接收小车回传的数据
                    SerialAcyivity_two.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg =recvhandler.obtainMessage(1,data);
                            msg.sendToTarget();

                            if (mainhandler != null){
                                Message msg1 = mainhandler.obtainMessage(1,data);
                                msg1.sendToTarget();
                            }

//                            ValuesApplication.Serial_data = data;
//                            ValuesApplication.Serial_data_update = true;


                            SerialAcyivity_two.this.updateReceivedData(data);       //调试界面显示数据
                        }
                    });
                }
            };

    private void startIoManager() {
        if (sPort != null) {
            Log.e(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sPort, mListener); //添加监听
            mExecutor.submit(mSerialIoManager); //在新的线程中监听串口的数据变化
        }
    }


    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
    }


    //打开usb设备，对usb参数进行设置。比如波特率、数据位、停止位、校验位
    protected void controlusb() {
        if (sPort == null) {
            Toast.makeText(SerialAcyivity_two.this,"No serial device.",Toast.LENGTH_SHORT).show();
        } else {
            openUsbDevice();  //打开usb设备
            if (connection == null) {
                mHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH, REFRESH_TIMEOUT_MILLIS);//如果打开不成功，重新获取
                Toast.makeText(SerialAcyivity_two.this,"Opening device failed",Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                sPort.open(connection);
                sPort.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            } catch (IOException e) {
                Toast.makeText(SerialAcyivity_two.this,"Error opening device: ",Toast.LENGTH_SHORT).show();
                try {
                    sPort.close();
                } catch (IOException e2) {
                }
                sPort = null;
                return;
            }
            Toast.makeText(SerialAcyivity_two.this,"SerialAcyivity_two device: " + sPort.getClass().getSimpleName(),Toast.LENGTH_SHORT).show();
        }
        onDeviceStateChange();
        Transparent.dismiss();//成功设置完usb，关闭等待对话框
    }

    private Handler usbHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what ==2) {
                useUsbtoserial();
            }
        }
    };

    //通过异步任务AsyncTask实现usb的获取
    private void refreshDeviceList() {
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        new AsyncTask<Void, Void, List<UsbSerialPort>>() {
            @Override
            protected List<UsbSerialPort> doInBackground(Void... params) {
                Log.e(TAG, "Refreshing device list ...");
                final List<UsbSerialDriver> drivers =
                        UsbSerialProber.getDefaultProber().findAllDrivers(mUsbManager);

                final List<UsbSerialPort> result = new ArrayList<UsbSerialPort>();
                for (final UsbSerialDriver driver : drivers) {
                    final List<UsbSerialPort> ports = driver.getPorts();
                    Log.e(TAG, String.format("+ %s: %s port%s",
                            driver, Integer.valueOf(ports.size()), ports.size() == 1 ? "" : "s"));
                    result.addAll(ports);  //保存usb端口
                }

                return result;
            }

            @Override
            protected void onPostExecute(List<UsbSerialPort> result) {
                mEntries.clear();
                mEntries.addAll(result); //保存usb端口
                usbHandler.sendEmptyMessage(2);
                Log.e(TAG, "Done refreshing, " + mEntries.size() + " entries found.");
            }
        }.execute((Void) null);
    }

}
