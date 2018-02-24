package com.qtp000.a03cemera_preview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Button btn1;
    Button bakbtn1,bakbtn2,bakbtn3,bakbtn4,bakbtn5,bakbtn6,bakbtn7;

    ImageView imageView;
    ImageButton btn_up;
    ImageButton btn_down;
    ImageButton btn_left;
    ImageButton btn_right;
    TextView textView;
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
    public static String result_qr = null;
    public static int model_221 = 0;
    public static int model_112 = 0;
    private Socket_connect socket_connect;
    private Timer timer;
    public int mark = 0;
    private byte[] mByte = new byte[11];
    private String RFID_result = "";
    private int RFID_status = 0;
    private int QRCODE_status = 0;
    private int TRAFFIC_status = 0;
    private int LICENSE_status = 0;
    private int PICTURE_status = 0;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("opencv_java");
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());

        init_btn();
        addlistener();
        search();
        Camer_Init();
        wifi_Init();
        socket_connect = new Socket_connect(this,qrHandler);
        connect_thread();
        fmod = new Function_method(socket_connect,state_camera,MainActivity.this);


    }


    class btntouchListener implements View.OnTouchListener{
        /**
         * Called when a touch event is dispatched to a view. This allows listeners to
         * get a chance to respond before the target view.
         *
         * @param v     The view the touch event has been dispatched to.
         * @param event The MotionEvent object containing full information about
         *              the event.
         * @return True if the listener has consumed the event, false otherwise.
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                switch (v.getId()){
                    case R.id.btn_up:
                        btn_up.setImageResource(R.drawable.up2);
                        break;
                    case R.id.btn_down:
                        btn_down.setImageResource(R.drawable.down2);
                        break;
                    case R.id.btn_left:
                        btn_left.setImageResource(R.drawable.left2);
                        break;
                    case R.id.btn_right:
                        btn_right.setImageResource(R.drawable.right2);
                        break;
                }
                //获取到的差值相同
//                if (v.getId() == R.id.btn_up){
//                    Log.e("EventTime",String.valueOf(event.getEventTime()));
//                    Log.e("DownTime",String.valueOf(event.getDownTime()));
//                    Log.e("差",String.valueOf(event.getEventTime() - event.getDownTime()));
//                }


            }
            if (event.getAction() == MotionEvent.ACTION_UP){
                switch (v.getId()){
                    case R.id.btn_up:
                        btn_up.setImageResource(R.drawable.up);

                        break;
                    case R.id.btn_down:
                        btn_down.setImageResource(R.drawable.down);
                        break;
                    case R.id.btn_left:
                        btn_left.setImageResource(R.drawable.left);
                        break;
                    case R.id.btn_right:
                        btn_right.setImageResource(R.drawable.right);
                        break;
                }
            }
            return true;
        }
    }

    class btnclickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            int id = v.getId();
//            Toast toast = Toast.makeText(getApplicationContext(),"点击了",Toast.LENGTH_SHORT);
//            toast.show();
            switch (id){
                case R.id.btn1:
                    Intent intent = new Intent(MainActivity.this , test.class);
                    startActivity(intent);
                    break;
                case R.id.btn_up:
                    Toast toast_up = Toast.makeText(getApplicationContext(),"上",Toast.LENGTH_SHORT);
                    toast_up.show();
                    break;
                case R.id.btn_left:
                    Toast toast_left = Toast.makeText(getApplicationContext(),"左",Toast.LENGTH_SHORT);
                    toast_left.show();
                    break;
                case R.id.btn_right:
                    Toast toast_right = Toast.makeText(getApplicationContext(),"右",Toast.LENGTH_SHORT);
                    toast_right.show();
                    break;
                case R.id.btn_down:
                    Toast toast_down = Toast.makeText(getApplicationContext(),"下",Toast.LENGTH_SHORT);
                    toast_down.show();
                    break;
                case R.id.button1:
                    Toast.makeText(getApplication(),"Button1",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.button2:
                    Toast.makeText(getApplication(),"Button2",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.button3:
                    Toast.makeText(getApplication(),"Button3",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.button4:
                    Toast.makeText(getApplication(),"Button4",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.button5:
                    Toast.makeText(getApplication(),"Button5",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.button6:
                    Toast.makeText(getApplication(),"Button6",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.button7:
                    Toast.makeText(getApplication(),"Button7",Toast.LENGTH_SHORT).show();

                    break;
            }



        }
    }

    private void init_btn(){
        btn1 = findViewById(R.id.btn1);

        imageView = findViewById(R.id.cemera_preview);
        btn_up = findViewById(R.id.btn_up);
        btn_left = findViewById(R.id.btn_left);
        btn_right = findViewById(R.id.btn_right);
        btn_down = findViewById(R.id.btn_down);
        textView = findViewById(R.id.textView);

        bakbtn1 = findViewById(R.id.button1);
        bakbtn2 = findViewById(R.id.button2);
        bakbtn3 = findViewById(R.id.button3);
        bakbtn4 = findViewById(R.id.button4);
        bakbtn5 = findViewById(R.id.button5);
        bakbtn6 = findViewById(R.id.button6);
        bakbtn7 = findViewById(R.id.button7);




    }

    private  void addlistener(){
        btn1.setOnClickListener(new btnclickListener());

//        btn_up.setOnClickListener(new btnclickListener());
//        btn_down.setOnClickListener(new btnclickListener());
//        btn_left.setOnClickListener(new btnclickListener());
//        btn_right.setOnClickListener(new btnclickListener());

//        bakbtn1.setOnClickListener(new btnclickListener());
//        bakbtn2.setOnClickListener(new btnclickListener());
//        bakbtn3.setOnClickListener(new btnclickListener());
//        bakbtn4.setOnClickListener(new btnclickListener());
//        bakbtn5.setOnClickListener(new btnclickListener());
//        bakbtn6.setOnClickListener(new btnclickListener());
//        bakbtn7.setOnClickListener(new btnclickListener());
        btn_up.setOnTouchListener(new btntouchListener());
        btn_down.setOnTouchListener(new btntouchListener());
        btn_left.setOnTouchListener(new btntouchListener());
        btn_right.setOnTouchListener(new btntouchListener());
        bakbtn1.setOnTouchListener(new btntouchListener());
        bakbtn2.setOnTouchListener(new btntouchListener());
        bakbtn3.setOnTouchListener(new btntouchListener());
        bakbtn4.setOnTouchListener(new btntouchListener());
        bakbtn5.setOnTouchListener(new btntouchListener());
        bakbtn6.setOnTouchListener(new btntouchListener());
        bakbtn7.setOnTouchListener(new btntouchListener());


    }

    private void search(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,Camera_Server.class);
        startService(intent);
    }
    private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context arg0, Intent arg1)
        {
            IPCamera = arg1.getStringExtra("IP");
            phThread.start();
        }
    };

    // 显示图片
    public Handler phHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 10)
            {
                imageView.setImageBitmap(bitmap);
            }
        }
    };

    // 图片
    public static Bitmap bitmap;
    // 得到当前摄像头的图片信息
    public void getBitmap()
    {
        bitmap = cameraCommandUtil.httpForImage(IPCamera);
        phHandler.sendEmptyMessage(10);
    }

    private Thread phThread = new Thread(new Runnable()
    {
        public void run()
        {
            while (true)
            {
                getBitmap();
                switch (state_camera) {
                    case 1:
                        cameraCommandUtil.postHttp(IPCamera, 0, 1);
                        break;
                    case 2:
                        cameraCommandUtil.postHttp(IPCamera, 2, 1);
                        break;
                    case 3:
                        cameraCommandUtil.postHttp(IPCamera, 4, 1);
                        break;
                    case 4:
                        cameraCommandUtil.postHttp(IPCamera, 6, 1);
                        break;
                    // /预设位1到3
                    case 5:
                        cameraCommandUtil.postHttp(IPCamera, 30, 0);
                        break;
                    case 6:
                        cameraCommandUtil.postHttp(IPCamera, 32, 0);
                        break;
                    case 7:
                        cameraCommandUtil.postHttp(IPCamera, 34, 0);
                        break;
                    case 8:
                        cameraCommandUtil.postHttp(IPCamera, 31, 0);
                        break;
                    case 9:
                        cameraCommandUtil.postHttp(IPCamera, 33, 0);
                        break;
                    case 10:
                        cameraCommandUtil.postHttp(IPCamera, 35, 0);
                        break;
                    default:
                        break;
                }
                state_camera = 0;
            }
        }
    });
    private void Camer_Init()
    {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(A_S);
        registerReceiver(myBroadcastReceiver, intentFilter);
        // 搜索摄像头图片工具
        cameraCommandUtil = new CameraCommandUtil();
    }

    private void wifi_Init()
    {
        // 得到服务器的IP地址
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        dhcpInfo = wifiManager.getDhcpInfo();
        IPCar = Formatter.formatIpAddress(dhcpInfo.gateway);
    }

    // 二维码、车牌处理
    public	Handler qrHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    if(bitmap != null)
                    {
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
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
//							else {
//								MainActivity.Timer_flag++;
//								if(MainActivity.Timer_flag >= 10)
//								{
//									result_qr = "^-^";
//									Toast.makeText(MainActivity.this, result_qr, Toast.LENGTH_SHORT).show();
//
//									MainActivity.Timer_flag = 0;
//									timer.cancel();
//									qrHandler.sendEmptyMessage(20);
//								}
//							}
                                    System.out.println("正在识别");
                                } catch (NotFoundException e) {
                                    e.printStackTrace();
                                } catch (ChecksumException e) {
                                    e.printStackTrace();
                                } catch (FormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 3, 200);
                    } else {

                        qrHandler.sendEmptyMessage(20);
                    }
                    break;
                case 20:
                    Toast.makeText(MainActivity.this, result_qr, Toast.LENGTH_SHORT).show();
                    socket_connect.mark = -socket_connect.mark;
                    mark = -mark;
                    break;
                case 31:
                    textView.setText("结果:"+ "0x"+new Algorithm().BToH((char)((socket_connect.order_data[0])&0xFF))+"; "+
                            "0x"+new Algorithm().BToH((char)((socket_connect.order_data[1])&0xFF))+"; "+
                            "0x"+new Algorithm().BToH((char)((socket_connect.order_data[2])&0xFF))+"; "+
                            "0x"+new Algorithm().BToH((char)((socket_connect.order_data[3])&0xFF))+"; "+
                            "0x"+new Algorithm().BToH((char)((socket_connect.order_data[4])&0xFF))+"; "+
                            "0x"+new Algorithm().BToH((char)((socket_connect.order_data[5])&0xFF)));
                    break;
                default:
                    break;
            }
        };
    };

    private void connect_thread()
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                socket_connect.connect(rehHandler, IPCar);
            }
        }).start();
    }

    // 接受显示小车发送的数据
    private Handler rehHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if (msg.what == 1)
            {
                mByte = (byte[]) msg.obj;
                if (mByte[0] == 0x55)
                {
                    //数据显示
                    if (mByte[1] == (byte)0xcc)
                    {
                        // 显示数据
                        textView.setText("WIFIIP:"+IPCar+""+"CameraIP"+IPCamera+"\n" +
                                (char)mByte[2] + (char)mByte[3] + (char)mByte[4] + (char)mByte[5] +
                                (char)mByte[6] + (char)mByte[7] + (char)mByte[8] + (char)mByte[9]);

                        for(int i=2;i<10;i++)
                            RFID_result += String.valueOf((char)mByte[i]);

                        return;
                    }
                    else if (mByte[1] == (byte)0xcd)
                    {
                        // 显示数据
                        textView.setText("WIFI模块IP:"+IPCar+"\n"  +
                                (char)mByte[2] + (char)mByte[3] + (char)mByte[4] + (char)mByte[5] +
                                (char)mByte[6] + (char)mByte[7] + (char)mByte[8] + (char)mByte[9]);

                        for(int i=2;i<10;i++)
                            RFID_result += String.valueOf((char)mByte[i]);

                        Log.e("RFID" , RFID_result);

                        RFID_status = 1;
                        yanchi(1000);

                    }
                    else if (mByte[1] == (byte)0xc1)
                    {
                        // 显示数据
                        textView.setText("WIFI模块IP:"+IPCar+"\n" + "该识别二维码了");

                        QRCODE_status = 1;
                        yanchi(1000);
                    }
                    else if (mByte[1] == (byte)0xc2)
                    {
                        // 显示数据
                        textView.setText("WIFI模块IP:"+IPCar+"\n" + "该识别交通灯了");

                        TRAFFIC_status = 1;
                        yanchi(1000);
                    }
                    else if (mByte[2] == (byte)0x01)
                    {
                        // 显示数据
                        textView.setText("WIFI模块IP:"+IPCar+"\n" + "该识别车牌了");

                        LICENSE_status = 1;
                        yanchi(1000);
                    }
                    else if (mByte[2] == (byte)0x02)
                    {
                        // 显示数据
                        textView.setText("WIFI模块IP:"+IPCar+"\n" + "该识别图形了");

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


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();



}
