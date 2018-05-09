package com.qtp000.a03cemera_preview;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;

//import java.util.logging.Handler;

//import static com.qtp000.a03cemera_preview.ValuesApplication.Mode.SerialAcyivity_two;

/**
 * Created by 祁天培 on 2018/5/6.
 */

public class ValuesApplication extends Application{

    public static String purecameraip =null;
//    public enum Mode{
//        SerialAcyivity_two,Socket
//    }
//    public static Mode SerialOrSocket = SerialAcyivity_two;
    public static boolean isserial = true;

    public static byte[] Serial_data = new byte[11];
    public static boolean Serial_data_update = false;

    private Handler Mainhandler = null;
    public void set_Mainhandler(Handler input_handler){
        this.Mainhandler = input_handler;
    }
    public Handler get_Mainhandler(){
        return Mainhandler;
    }

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


