package com.qtp000.a03cemera_preview;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;

//import static com.qtp000.a03cemera_preview.ValuesApplication.Mode.SerialAcyivity;

/**
 * Created by 祁天培 on 2018/5/6.
 */

public class ValuesApplication {
    public static String purecameraip =null;
//    public enum Mode{
//        SerialAcyivity,Socket
//    }
//    public static Mode SerialOrSocket = SerialAcyivity;
    public static boolean isserial = true;

/*    @Override
    public void onCreate() {
        super.onCreate();
        Intent sintent = new Intent();
        //ComponentName的参数1:目标app的包名,参数2:目标app的Service完整类名
        sintent.setComponent(new ComponentName("com.android.settings", "com.android.settings.ethernet.CameraInitService"));
        //设置要传送的数据
        sintent.putExtra("purecameraip","0.0.0.0");
        startService(sintent);
    }*/
}


