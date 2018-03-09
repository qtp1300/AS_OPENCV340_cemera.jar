package com.qtp000.a03cemera_preview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bkrcl.control_car_video.camerautil.CameraCommandUtil;

/**
 * Created by 祁天培 on 2018/2/6.
 */
/*目前用途
* 调试摄像头
* */

public class test extends Activity{

    public static ImageView img_preview;
    private Button btn_back;
    private Button btn_cemera_init,btn_cemera_32,btn_cemera_33,btn_cemera_34,btn_cemera_35,btn_cemera_36,btn_cemera_37,btn_cemera_38,btn_cemera_39;
    private Button btn_cemera_up,btn_cemera_left,btn_cemera_right,btn_cemera_down,btn_cemera_fast,btn_cemera_slow;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        
        btn_back = findViewById(R.id.back_btn);
        btn_cemera_init = findViewById(R.id.btn_camera_init);
        btn_cemera_32 = findViewById(R.id.btn_camera_32);
        btn_cemera_33 = findViewById(R.id.btn_camera_33);
        btn_cemera_34 = findViewById(R.id.btn_camera_34);
        btn_cemera_35 = findViewById(R.id.btn_camera_35);
        btn_cemera_36 = findViewById(R.id.btn_camera_36);
        btn_cemera_37 = findViewById(R.id.btn_camera_37);
        btn_cemera_38 = findViewById(R.id.btn_camera_38);
        btn_cemera_39 = findViewById(R.id.btn_camera_39);
        btn_cemera_up = findViewById(R.id.btn_cemera_up);
        btn_cemera_down = findViewById(R.id.btn_cemera_down);
        btn_cemera_left = findViewById(R.id.btn_cemera_left);
        btn_cemera_right = findViewById(R.id.btn_cemera_right);
        btn_cemera_fast = findViewById(R.id.btn_cemera_fast);
        btn_cemera_slow = findViewById(R.id.btn_cemera_slow);


        img_preview = findViewById(R.id.img_preview2);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

                btn_cemera_init.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                MainActivity.state_camera = 25;
            }
        });
        btn_cemera_32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.state_camera = 32;
            }
        });
        btn_cemera_33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.state_camera = 33;
            }
        });
        btn_cemera_34.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.state_camera = 34;
            }
        });
        btn_cemera_35.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.state_camera = 35;
            }
        });
        btn_cemera_36.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.state_camera = 36;
            }
        });
        btn_cemera_37.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.state_camera = 37;
            }
        });
        btn_cemera_38.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.state_camera = 38;
            }
        });
        btn_cemera_39.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.state_camera = 39;
            }
        });
        btn_cemera_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.state_camera = 500;
            }
        });
        btn_cemera_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.state_camera = 502;
            }
        });
        btn_cemera_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.state_camera = 504;
            }
        });
        btn_cemera_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.state_camera = 506;
            }
        });
        btn_cemera_fast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.cemera_step = 2;
                Toast.makeText(getApplicationContext(),"摄像头移动快",Toast.LENGTH_SHORT).show();
            }
        });
        btn_cemera_slow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.cemera_step = 1;
                Toast.makeText(getApplicationContext(),"摄像头移动慢",Toast.LENGTH_SHORT).show();
            }
        });


        set_preview();


    }

//    public Handler preview = new Handler(){
//        /**
//         * Subclasses must implement this to receive messages.
//         *
//         * @param msg
//         */
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 10){
//                img_preview.setImageBitmap(MainActivity.bitmap);
//            }
//        }
//    };
    public void set_preview()
    {

            Thread set_pre = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
//                        img_preview.setImageBitmap(MainActivity.bitmap);
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
            set_pre.start();
    }
}
