package com.qtp000.a03cemera_preview;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 祁天培 on 2018/5/6.
 */
/*
* 此文件有Manifest启动，为Application而文件，一般用于传递全局变量等操作，
* 本文件初始化了有线网络
* 并新建了一个线程池*/

public class First_Init_Values extends Application {
    public enum Mode{
        SOCKET, SERIAL, USB_SERIAL
    }
//    public  static String cameraip="192.168.1.101:81";


    public static String purecameraip =null;

    private static First_Init_Values app;
    public static ExecutorService executorServicetor = Executors.newCachedThreadPool();
    public static Mode isserial = Mode.USB_SERIAL;
    public static First_Init_Values getApp() {
        return app;
    }

    public void onCreate(){
        super.onCreate();
        app =this;
        Intent sintent = new Intent();
        //ComponentName的参数1:目标app的包名,参数2:目标app的Service完整类名
        sintent.setComponent(new ComponentName("com.android.settings", "com.android.settings.ethernet.CameraInitService"));
        //设置要传送的数据
        sintent.putExtra("purecameraip","0.0.0.0");
        startService(sintent);


    }


}


