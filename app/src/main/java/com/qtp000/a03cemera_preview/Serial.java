package com.qtp000.a03cemera_preview;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 祁天培 on 2018/5/7.
 */

public class Serial {
    private  final int MESSAGE_REFRESH= 101;
    private  final long REFRESH_TIMEOUT_MILLIS = 5000;
    private int num =0;
    private UsbManager mUsbManager;
    private List<UsbSerialPort> mEntries = new ArrayList<UsbSerialPort>();
    private final String TAG = MainActivity.class.getSimpleName();
    private Socket_connect sock_con;

    public Serial(){
        mHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH, REFRESH_TIMEOUT_MILLIS);  //开始usb的获取
        sock_con =new Socket_connect();
//        control_init();
//        Transparent.showLoadingMessage(this,"加载中",false);   //显示等待对话框

        Log.i("Serial.class:","加载中")；
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


    //通过异步任务AsyncTask实现usb的获取
    private void refreshDeviceList() {
        mUsbManager = (UsbManager) (Context.USB_SERVICE);

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
