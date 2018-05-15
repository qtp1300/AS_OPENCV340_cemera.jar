package com.qtp000.a03cemera_preview;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bkrcl.control_car_video.camerautil.CameraCommandUtil;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.qtp000.a03cemera_preview.Image.RgbLuminanceSource;
import com.qtp000.a03cemera_preview.Image.ShapeActivity;
import com.qtp000.a03cemera_preview.used_package.Camera_Server;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.qtp000.a03cemera_preview.FunctionActivity.result;


public class MainActivity_two extends AppCompatActivity {

    Button btn1;
    Button btn_function, btn_moni1, bakbtn4, bakbtn5, btn_old_112, btn_stop;
    Button btn_cemera_init, btn_cemera_32, btn_cemera_33, btn_cemera_34, btn_cemera_35, btn_cemera_36, btn_cemera_37, btn_cemera_38, btn_cemera_39;
    Button btn_lingxing, btn_juxing, btn_yuanxing, btn_sanjiao;
    Button btn_car_1, btn_car_2, btn_car_test;
    public static short set_shape = 0x02;       //默认 0x01/矩形    0x02/圆形  0x03/三角形   0x04/菱形  0x05/梯形   0x06/饼图  0x07/靶图   0x08/条形图
    ProgressBar progressBar;

    ImageView imageView;
    ImageButton btn_up;
    ImageButton btn_down;
    ImageButton btn_left;
    ImageButton btn_right;
    TextView textView, textView_isrunning;
    public static int state_camera;
    public static final String A_S = "com.a_s";
    private CameraCommandUtil cameraCommandUtil;
    public static String IPCamera = "bkrcjk.eicp.net:88";
    private Function_method fmod;
    private WifiManager wifiManager;
    // 服务器管理器
    private DhcpInfo dhcpInfo;
    // 小车ip
    private String IPCar;
    // 摄像头IP
    public static String result_qr = "#$...(.--.*&-.)&.-..@%%-.-#&&*%$%...--^)&*.--%|0x17";
    public static int model_221 = 0;
    public static int model_112 = 0;
    private Socket_connect socket_connect;
    private Timer timer;
    public int mark = 0;

    private String RFID_result = "";
    private int RFID_status = 0;
    private int QRCODE_status = 0;
    private int TRAFFIC_status = 0;
    private int LICENSE_status = 0;
    private int PICTURE_status = 0;
    public static int cemera_step = 1;
    public static boolean moni1 = false;
    public static int QR_time = 0;
    public static int run_time = 1;

    private byte[] mByte = new byte[11];


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("opencv_java");
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_activity_main);


        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());
        send_handler2serial();
        init_view();
        addlistener();
        search();
        Camer_Init();


        if (ValuesApplication.isserial == true) {
            ethernet_Init();                //用有线时启用这个
        } else {
            wifi_Init();                    //用wifi时启用这个
        }

        socket_connect = new Socket_connect(this, qrHandler);
        connect_thread();

        fmod = new Function_method(socket_connect, state_camera, MainActivity_two.this);

        FunctionActivity.set_handle(this.qrHandler);

    }


//    class btntouchListener implements View.OnTouchListener{
//
//
//
//        /**
//         * Called when a touch event is dispatched to a view. This allows listeners to
//         * get a chance to respond before the target view.
//         *
//         * @param v     The view the touch event has been dispatched to.
//         * @param event The MotionEvent object containing full information about
//         *              the event.
//         * @return True if the listener has consumed the event, false otherwise.
//         */
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            if (event.getAction() == MotionEvent.ACTION_DOWN){
//                switch (v.getId()){
//                    case R.id.btn_up:
//                        btn_up.setImageResource(R.drawable.up2);
//                        break;
//                    case R.id.btn_down:
//                        btn_down.setImageResource(R.drawable.down2);
//                        break;
//                    case R.id.btn_left:
//                        btn_left.setImageResource(R.drawable.left2);
//                        break;
//                    case R.id.btn_right:
//                        btn_right.setImageResource(R.drawable.right2);
//                        break;
//                }
//                //获取到的差值相同
////                if (v.getId() == R.id.btn_up){
////                    Log.e("EventTime",String.valueOf(event.getEventTime()));
////                    Log.e("DownTime",String.valueOf(event.getDownTime()));
////                    Log.e("差",String.valueOf(event.getEventTime() - event.getDownTime()));
////                }
//
//            }
//            if (event.getAction() == MotionEvent.ACTION_UP){
//                switch (v.getId()){
//                    case R.id.btn_up:
//                        btn_up.setImageResource(R.drawable.up);
//                        socket_connect.go(40,70);
//                        break;
//                    case R.id.btn_down:
//                        btn_down.setImageResource(R.drawable.down);
//                        socket_connect.back(40,70);
//                        break;
//                    case R.id.btn_left:
//                        btn_left.setImageResource(R.drawable.left);
//                        break;
//                    case R.id.btn_right:
//                        btn_right.setImageResource(R.drawable.right);
//                        break;
////                    case R.id.btn_moni1:
////                        to_run_moni1_with_wifi();
////                        break;
//                }
//            }
//            return false;
//        }
//
//    }

    class btnclickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
//            Toast toast = Toast.makeText(getApplicationContext(),"点击了",Toast.LENGTH_SHORT);
//            toast.show();
            switch (id) {
                case R.id.btn1:
                    Intent intent2test = new Intent(MainActivity_two.this, test.class);
                    startActivity(intent2test);
                    break;
                case R.id.btn_up:
                    Toast toast_up = Toast.makeText(getApplicationContext(), "上", Toast.LENGTH_SHORT);
                    toast_up.show();
                    socket_connect.go(80, 2000);
                    break;
                case R.id.btn_left:
                    Toast toast_left = Toast.makeText(getApplicationContext(), "左", Toast.LENGTH_SHORT);
                    socket_connect.Mine_sent_start();
                    toast_left.show();
                    break;
                case R.id.btn_right:
                    Toast toast_right = Toast.makeText(getApplicationContext(), "右", Toast.LENGTH_SHORT);
                    toast_right.show();
                    break;
                case R.id.btn_down:
                    Toast toast_down = Toast.makeText(getApplicationContext(), "下", Toast.LENGTH_SHORT);
                    toast_down.show();
                    break;
                case R.id.lingxing:
                    Toast.makeText(getApplication(), "菱形 0x04", Toast.LENGTH_SHORT).show();
                    set_shape = 0x04;
//                    Log.e("按键","Button1");
                    break;
                case R.id.juxing:
                    Toast.makeText(getApplication(), "矩形 0x01", Toast.LENGTH_SHORT).show();
                    set_shape = 0x01;
                    break;
                case R.id.yuanxing:
                    Toast.makeText(getApplication(), "圆形 0x02", Toast.LENGTH_SHORT).show();
                    set_shape = 0x02;
                    break;
                case R.id.sanjiaoxing:
                    Toast.makeText(getApplication(), "三角形 0x03", Toast.LENGTH_SHORT).show();
                    set_shape = 0x03;
                    break;
                case R.id.btn_function:
                    Toast.makeText(getApplication(), "跳转至功能页", Toast.LENGTH_SHORT).show();
                    Intent intent2FunctionActivity = new Intent(MainActivity_two.this, FunctionActivity.class);
                    startActivity(intent2FunctionActivity);

                    break;
                case R.id.btn_moni1:
                    Toast.makeText(getApplication(), "模拟1", Toast.LENGTH_SHORT).show();
                    to_run_moni1_with_wifi();
                    textView_isrunning.setText("运行中");
//                    btn_moni1.setSelected(true);
                    break;
//                case R.id.car_1:
//                    Toast.makeText(getApplication(),"Button4",Toast.LENGTH_SHORT).show();
//                    break;
                case R.id.btn_toshape:
                    Toast.makeText(getApplication(), "Button5", Toast.LENGTH_SHORT).show();
                    Intent toshape = new Intent(MainActivity_two.this, ShapeActivity.class);
                    startActivity(toshape);
                    break;
                case R.id.old_112:
                    Toast.makeText(getApplication(), "原112", Toast.LENGTH_SHORT).show();
                    state_camera = 10;
                    model_112 = 10;
                    full_Thread_model_112();
                    break;
                case R.id.btn_stop:
                    Toast.makeText(getApplication(), "停止", Toast.LENGTH_SHORT).show();
                    moni1 = false;
                    break;
                case R.id.car_1:
                    run_time = 1;
                    Toast.makeText(getApplication(), "一轮", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.car_2:
                    run_time = 2;
                    Toast.makeText(getApplication(), "二轮", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.car_test:
                    run_time = 3;
                    Toast.makeText(getApplication(), "测试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void init_view() {
        btn1 = findViewById(R.id.btn1);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);

        imageView = findViewById(R.id.cemera_preview);
        btn_up = findViewById(R.id.btn_up);
        btn_left = findViewById(R.id.btn_left);
        btn_right = findViewById(R.id.btn_right);
        btn_down = findViewById(R.id.btn_down);
        textView = findViewById(R.id.textView);
        textView_isrunning = findViewById(R.id.isrunning);

        btn_lingxing = findViewById(R.id.lingxing);
        btn_juxing = findViewById(R.id.juxing);
        btn_yuanxing = findViewById(R.id.yuanxing);
        btn_sanjiao = findViewById(R.id.sanjiaoxing);


        btn_function = findViewById(R.id.btn_function);
        btn_moni1 = findViewById(R.id.btn_moni1);
        bakbtn4 = findViewById(R.id.car_1);
        bakbtn5 = findViewById(R.id.btn_toshape);
        btn_old_112 = findViewById(R.id.old_112);
        btn_stop = findViewById(R.id.btn_stop);

        btn_car_1 = findViewById(R.id.car_1);
        btn_car_2 = findViewById(R.id.car_2);
        btn_car_test = findViewById(R.id.car_test);


    }

    private void addlistener() {
        btn1.setOnClickListener(new btnclickListener());

        btn_lingxing.setOnClickListener(new btnclickListener());
        btn_juxing.setOnClickListener(new btnclickListener());
        btn_yuanxing.setOnClickListener(new btnclickListener());
        btn_sanjiao.setOnClickListener(new btnclickListener());

        btn_function.setOnClickListener(new btnclickListener());
        btn_moni1.setOnClickListener(new btnclickListener());
        bakbtn4.setOnClickListener(new btnclickListener());
        bakbtn5.setOnClickListener(new btnclickListener());
        btn_old_112.setOnClickListener(new btnclickListener());
        btn_up.setOnClickListener(new btnclickListener());
        btn_left.setOnClickListener(new btnclickListener());
        btn_right.setOnClickListener(new btnclickListener());
        btn_down.setOnClickListener(new btnclickListener());
        btn_stop.setOnClickListener(new btnclickListener());

        btn_car_1.setOnClickListener(new btnclickListener());
        btn_car_2.setOnClickListener(new btnclickListener());
        btn_car_test.setOnClickListener(new btnclickListener());

//        btn_up.setOnTouchListener(new btntouchListener());
//        btn_down.setOnTouchListener(new btntouchListener());
//        btn_left.setOnTouchListener(new btntouchListener());
//        btn_right.setOnTouchListener(new btntouchListener());
//        btn_lingxing.setOnTouchListener(new btntouchListener());
//        btn_function.setOnTouchListener(new btntouchListener());
//        btn_moni1.setOnTouchListener(new btntouchListener());
//        bakbtn4.setOnTouchListener(new btntouchListener());
//        bakbtn5.setOnTouchListener(new btntouchListener());
//        btn_old_112.setOnTouchListener(new btntouchListener());
//        btn_stop.setOnTouchListener(new btntouchListener());


    }

    private void search() {
        Intent intent = new Intent();
        intent.setClass(MainActivity_two.this, Camera_Server.class);
        startService(intent);
    }

    private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context arg0, Intent arg1) {
            IPCamera = arg1.getStringExtra("IP");
            ValuesApplication.purecameraip = arg1.getStringExtra("pureip");
            phThread.start();
        }
    };

    // 显示图片
    public Handler phHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 10) {
//                Log.e("显示图片：","已经进行到此步骤");
                imageView.setImageBitmap(bitmap);
                ShapeActivity.input_bitmap = Bitmap.createBitmap(bitmap);
                if (bitmap == null || bitmap.equals("")) {
//                    Log.e("图片：", "为空");
                } else {
//                    Log.e("图片：", "不空");
                }
            }
        }
    };


    // 图片
    public static Bitmap bitmap;

    // 得到当前摄像头的图片信息
    public void getBitmap() {
        bitmap = cameraCommandUtil.httpForImage(IPCamera);
        phHandler.sendEmptyMessage(10);
    }

    private Thread phThread = new Thread(new Runnable() {
        public void run() {
            while (true) {
                getBitmap();        //获取并显示摄像头图像
                switch (state_camera) {

                    case 25:
                        cameraCommandUtil.postHttp(IPCamera, 25, 0);
                        break;
                    case 32:
                        cameraCommandUtil.postHttp(IPCamera, 32, 0);
                        break;
                    case 33:
                        cameraCommandUtil.postHttp(IPCamera, 33, 0);
                        break;
                    case 34:
                        cameraCommandUtil.postHttp(IPCamera, 34, 0);
                        break;
                    case 35:
                        cameraCommandUtil.postHttp(IPCamera, 35, 0);
                        break;
                    case 36:
                        cameraCommandUtil.postHttp(IPCamera, 36, 0);
                        break;
                    case 37:
                        cameraCommandUtil.postHttp(IPCamera, 37, 0);
                        break;
                    case 38:
                        cameraCommandUtil.postHttp(IPCamera, 38, 0);
                        break;
                    case 39:
                        cameraCommandUtil.postHttp(IPCamera, 39, 0);
                        break;
                    case 500:            //抬头
                        cameraCommandUtil.postHttp(IPCamera, 0, cemera_step);
                        break;
                    case 502:           //低头
                        cameraCommandUtil.postHttp(IPCamera, 2, cemera_step);
                        break;
                    case 504:           //向左
                        cameraCommandUtil.postHttp(IPCamera, 4, cemera_step);
                        break;
                    case 506:           //向右
                        cameraCommandUtil.postHttp(IPCamera, 6, cemera_step);
                        break;
                    /*自己加的*/
                    default:
                        break;
                }
                state_camera = 0;
            }
        }
    });

    private void Camer_Init() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(A_S);
        registerReceiver(myBroadcastReceiver, intentFilter);
        // 搜索摄像头图片工具
        cameraCommandUtil = new CameraCommandUtil();
    }

    private void wifi_Init() {
        // 得到服务器的IP地址
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        dhcpInfo = wifiManager.getDhcpInfo();
        IPCar = Formatter.formatIpAddress(dhcpInfo.gateway);
    }

    private void ethernet_Init() {
        Intent ipintent = new Intent();
        //ComponentName的参数1:目标app的包名,参数2:目标app的Service完整类名
        ipintent.setComponent(new ComponentName("com.android.settings", "com.android.settings.ethernet.CameraInitService"));
        //设置要传送的数据
        ipintent.putExtra("purecameraip", ValuesApplication.purecameraip);
        startService(ipintent);   //摄像头设为静态192.168.16.20时，可以不用发送

    }


    // 二维码、车牌处理
    public Handler qrHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    if (bitmap != null) {
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                QR_time = QR_time++;
                                if (QR_time < 5) {
                                    Result result = null;
                                    RgbLuminanceSource rSource = new RgbLuminanceSource(bitmap);
                                    try {
                                        BinaryBitmap binaryBitmap = new BinaryBitmap(
                                                new HybridBinarizer(rSource));
                                        Map<DecodeHintType, String> hint = new HashMap<DecodeHintType, String>();
                                        hint.put(DecodeHintType.CHARACTER_SET, "utf-8");
                                        QRCodeReader reader = new QRCodeReader();
                                        result = reader.decode(binaryBitmap, hint);
                                        if (result.toString() != null) {
                                            result_qr = result.toString();
                                            timer.cancel();
                                            qrHandler.sendEmptyMessage(20);
                                        }
                                    } catch (NotFoundException e) {
                                        e.printStackTrace();
                                    } catch (ChecksumException e) {
                                        e.printStackTrace();
                                    } catch (FormatException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (QR_time == 5) {
                                    if (socket_connect.mark < 0) {
                                        socket_connect.mark = -socket_connect.mark;
                                    }
                                    timer.cancel();
                                    qrHandler.sendEmptyMessage(20);
                                }
                            }
                        }, 3, 200);
                    } else {

                        qrHandler.sendEmptyMessage(20);
                    }
                    break;
                case 20:
                    Toast.makeText(MainActivity_two.this, result_qr, Toast.LENGTH_SHORT).show();
                    if (socket_connect.mark < 0) {
                        socket_connect.mark = -socket_connect.mark;
                    }
                    //mark = -mark;
                    break;
                case 31:
                    textView.setText("结果:" + "0x" + new Algorithm().BToH((char) ((socket_connect.order_data[0]) & 0xFF)) + "; " +
                            "0x" + new Algorithm().BToH((char) ((socket_connect.order_data[1]) & 0xFF)) + "; " +
                            "0x" + new Algorithm().BToH((char) ((socket_connect.order_data[2]) & 0xFF)) + "; " +
                            "0x" + new Algorithm().BToH((char) ((socket_connect.order_data[3]) & 0xFF)) + "; " +
                            "0x" + new Algorithm().BToH((char) ((socket_connect.order_data[4]) & 0xFF)) + "; " +
                            "0x" + new Algorithm().BToH((char) ((socket_connect.order_data[5]) & 0xFF)) + "; " +
                            "光照：" + new Algorithm().BToH((char) ((socket_connect.order_data_2[0]) & 0xFF)) + "; " +
                            "RFID：0x" + new Algorithm().BToH((char) ((socket_connect.order_data_2[1]) & 0xFF)));
                    break;
                /*自己加的*/
                case 100:
                    textView.setText("识别结果" + result);
                    break;
                case 101:               //
                    textView_isrunning.setText("运行结束");
//                case 102:
//                    if (msg.arg1 == 30){
//
//                    }
                case 201:
                    progressBar.setProgress(9);
                    break;
                case 202:
                    progressBar.setProgress(18);
                    break;
                case 203:
                    progressBar.setProgress(27);
                    break;
                case 204:
                    progressBar.setProgress(36);
                    break;
                case 205:
                    progressBar.setProgress(45);
                    break;
                case 206:
                    progressBar.setProgress(54);
                    break;
                case 207:
                    progressBar.setProgress(63);
                    break;
                case 208:
                    progressBar.setProgress(72);
                    break;
                case 209:
                    progressBar.setProgress(81);
                    break;
                case 210:
                    progressBar.setProgress(90);
                    break;
                case 211:
                    progressBar.setProgress(94);
                    break;
                case 212:
                    progressBar.setProgress(98);
                    break;
                case 213:
                    progressBar.setProgress(100);
                    break;


                /*自己加的*/
                default:
                    break;
            }
        }

        ;
    };

    private void connect_thread() {
        new Thread(new Runnable() {
            public void run() {
                socket_connect.connect(rehHandler, IPCar);
            }
        }).start();
    }

    // 接受显示小车发送的数据
    private Handler rehHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mByte = (byte[]) msg.obj;
                if (mByte[0] == 0x55) {
                    //数据显示
                    if (mByte[1] == (byte) 0xcc) {
                        // 显示数据
                        textView.setText("WIFIIP:" + IPCar + "" + "CameraIP" + IPCamera + "\n" +
                                (char) mByte[2] + (char) mByte[3] + (char) mByte[4] + (char) mByte[5] +
                                (char) mByte[6] + (char) mByte[7] + (char) mByte[8] + (char) mByte[9]);

                        for (int i = 2; i < 10; i++)
                            RFID_result += String.valueOf((char) mByte[i]);

                        return;
                    } else if (mByte[1] == (byte) 0xcd) {
                        // 显示数据
                        textView.setText("WIFI模块IP:" + IPCar + "\n" +
                                (char) mByte[2] + (char) mByte[3] + (char) mByte[4] + (char) mByte[5] +
                                (char) mByte[6] + (char) mByte[7] + (char) mByte[8] + (char) mByte[9]);

                        for (int i = 2; i < 10; i++)
                            RFID_result += String.valueOf((char) mByte[i]);

                        Log.e("RFID", RFID_result);

                        RFID_status = 1;
                        yanchi(1000);

                    } else if (mByte[1] == (byte) 0xc1) {
                        // 显示数据
                        textView.setText("WIFI模块IP:" + IPCar + "\n" + "该识别二维码了");

                        QRCODE_status = 1;
                        yanchi(1000);
                    } else if (mByte[1] == (byte) 0xc2) {
                        // 显示数据
                        textView.setText("WIFI模块IP:" + IPCar + "\n" + "该识别交通灯了");

                        TRAFFIC_status = 1;
                        yanchi(1000);
                    } else if (mByte[2] == (byte) 0x01) {
                        // 显示数据
                        textView.setText("WIFI模块IP:" + IPCar + "\n" + "该识别车牌了");

                        LICENSE_status = 1;
                        yanchi(1000);
                    } else if (mByte[2] == (byte) 0x02) {
                        // 显示数据
                        textView.setText("WIFI模块IP:" + IPCar + "\n" + "该识别图形了");

                        PICTURE_status = 1;
                        yanchi(1000);
                    }
                }
            }
        }
    };

    // 沉睡
    private void yanchi(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void full_Thread_model_112()            //112
    {
        socket_connect.mark = 5;
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    if (model_112 == 10)
                        socket_connect.Full_motion_model_112();
                }
            }
        }).start();
    }

    private void to_run_moni1_with_wifi() {
        moni1 = true;
        socket_connect.mark = 5;
        QR_time = 0;
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (moni1) {
                    socket_connect.moni1_4();
                }
                Log.e("模拟1状态:", "应该结束了");
                qrHandler.sendEmptyMessage(101);
//                textView_isrunning.setText("运行结束");
                qrHandler.sendEmptyMessage(213);
            }
        }).start();
    }

    private void to_run_moni1_with_serial() {
        moni1 = true;
//        socket_connect.mark = 5;
        QR_time = 0;
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (moni1) {
                    socket_connect.moni1_4();
                }
                Log.e("模拟1状态:", "应该结束了");
                qrHandler.sendEmptyMessage(101);
//                textView_isrunning.setText("运行结束");
                qrHandler.sendEmptyMessage(213);
            }
        }).start();
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();

    ValuesApplication valuesApplication;
    private Handler for_serial_handler = new Handler() {
        /**
         * Subclasses must implement this to receive messages.
         *
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mByte = (byte[]) msg.obj;
                Log.i("串口向main发送数据了", "成功接收" + mByte.length + "的长度");

            }
        }
    };

    private void send_handler2serial() {
        valuesApplication = (ValuesApplication) getApplication();
        valuesApplication.set_Mainhandler(for_serial_handler);
    }

}
