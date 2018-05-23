package com.qtp000.a03cemera_preview;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Handler;

import java.security.PublicKey;

//import java.util.logging.Handler;

//import static com.qtp000.a03cemera_preview.ValuesApplication.Mode.SerialAcyivity_two;

/**
 * Created by 祁天培 on 2018/5/6.
 */

public class ValuesApplication extends Application {

    public static String purecameraip = null;
    //    public enum Mode{
//        SerialAcyivity_two,Socket
//    }
//    public static Mode SerialOrSocket = SerialAcyivity_two;
    public static boolean isserial = true;

    public static byte[] Serial_data = new byte[11];
    public static boolean Serial_data_update = false;

    public static Bitmap sourcebitmap;

    private Handler Mainhandler = null;

    public void set_Mainhandler(Handler input_handler) {
        this.Mainhandler = input_handler;
    }

    public Handler get_Mainhandler() {
        return Mainhandler;
    }

    public static Traffic_Light_Mode Traffic_Light_Status = Traffic_Light_Mode.YELLOW;

    public enum Traffic_Light_Mode {GREEN, RED, YELLOW}

    public enum TFT_status {SHAPE, License_Plate}

    public static TFT_status tft_status = TFT_status.SHAPE;

    public static int[][] shape_result = new int[9][5];
    /*0 红色  1 绿色    2 蓝色    3 黄色    4 品色    5 青色    6 黑色    7 白色    8 不区分颜色
     * 0 三角形 1 圆形    2 矩形    3 菱形    4 五角星*/
    public static int[][] shape_result_manual = new int[9][5];

    public static String license_plate_result;
    public static String license_plate_result_manual;
    public static char[] license_plate_result_char;
    public static byte[] license_plate_result_byte;

    public static int shibiecishu = 0;

    public enum AUTO_MANUAL {AUTO, MANUAL}
    public static AUTO_MANUAL shape_AutoOrManual = AUTO_MANUAL.AUTO;
    public static AUTO_MANUAL license_plate_AutoOrManual = AUTO_MANUAL.AUTO;

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


