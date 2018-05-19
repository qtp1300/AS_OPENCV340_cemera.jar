package com.qtp000.a03cemera_preview;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.qtp000.a03cemera_preview.Image.Traffic_Light;
import com.qtp000.a03cemera_preview.Serial.SerialAcyivity_two;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by 祁天培 on 2018/2/9.
 */

public class Socket_connect {
    private int port = 60000;
    private DataInputStream bInputStream;
    private DataOutputStream bOutputStream;
    private Socket socket;
    public byte[] rbyte = new byte[40];     //无线接收到的数据
    public byte[] sbyte = new byte[40];     //串口接收到的数据
    private Handler reHandler;
    private short TYPE = 0xAA;
    private short MAJOR = 0x00;
    private short FIRST = 0x00;
    private short SECOND = 0x00;
    private short THRID = 0x00;
    private short CHECKSUM = 0x00;
    private short HEAD = 0x55;
    private short TAIL = 0xBB;
    private Handler qrhandler;
    private Context context;
    private Algorithm algorithm;

    public Socket_connect() {
    }

    public Socket_connect(Context context, Handler handler) {
        this.context = context;
        this.qrhandler = handler;
        algorithm = new Algorithm();
    }

    public void onDestory() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                bInputStream.close();
                bOutputStream.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void connect(Handler handler, String IP) {
        try {
            this.reHandler = handler;
            socket = new Socket(IP, port);
            bInputStream = new DataInputStream(socket.getInputStream());
            bOutputStream = new DataOutputStream(socket.getOutputStream());
            reThread.start();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Thread reThread = new Thread(new Runnable() {
        @Override
        public void run() {
            Log.i("UDP网络协议", "已建立");
            // TODO Auto1-generated method stub
            while (socket != null && !socket.isClosed()) {
                try {
                    bInputStream.read(rbyte);
                    Log.i("WiFi信息更新", String.valueOf(rbyte[1]) + "  " + String.valueOf(rbyte[2]));
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = rbyte;
                    reHandler.sendMessage(msg);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    });

    //发送数据
    private void send() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CHECKSUM = (byte) ((MAJOR + FIRST + SECOND + THRID) % 256);//计算通信协议校验和
                // 发送数据字节数组，此处根据小车的通信协议来写
                byte[] sendbyte = {(byte) HEAD, (byte) TYPE, (byte) MAJOR, (byte) FIRST, (byte) SECOND, (byte) THRID, (byte) CHECKSUM, (byte) 0xBB};

                if (ValuesApplication.isserial == true) {
                    try {
                        SerialAcyivity_two.sPort.write(sendbyte, 5000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        bOutputStream.write(sendbyte, 0, sendbyte.length);
                        bOutputStream.flush();
                        Log.i("WiFi信息更新", "已发送");
                    } catch (IOException e) {
                        Log.e("消息发送失败", "通信协议出错");
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

    //发送字符数据
    private void senddata() {
        try {
            //计算通信协议校验和
            //CHECKSUM = (byte) ((MAJOR+FIRST+SECOND+THRID) % 256);
            // 发送数据字节数组，此处根据小车的通信协议来写
            byte[] mbyte = {(byte) HEAD, (byte) TYPE, (byte) MAJOR, (byte) FIRST, (byte) SECOND, (byte) THRID, (byte) CHECKSUM, (byte) TAIL};
            bOutputStream.write(mbyte, 0, mbyte.length);
            bOutputStream.flush();
            Log.e("消息发送成功", "通信协议OK");
        } catch (IOException e) {
            Log.e("消息发送失败", "通信协议出错");
            e.printStackTrace();
        }
    }

    public void Mine_sent_start() {
//        MAJOR = 0xD1;
//        FIRST = 0x00;
//        SECOND = 0x00;
//        THRID = 0x00;
//        send();
//        Log.i("自定协议", "开始指令发送完毕");
//        Log.i("此时串口数据", sbyte.toString());
        Thread tese_recive = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("线程", "开始运行");
                if (ValuesApplication.isserial == true) {
                    while (true) {
                        Log.i("此时串口数据", sbyte.toString());
//                        Log.i("此时串口数据第二位", new String(sbyte));
                        if (sbyte[1] == (byte) 0xAA) {
                            Log.i("串口数据", "标志位AA收到");
//                            byte init = (byte) 0x99;
//                            if (sbyte[2] != init) {
//                                init = sbyte[3];
                            Log.i("此时串口有效数据", String.valueOf(sbyte[2]));
//                            }
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    while (true) {
                        Log.i("此时WiFi数据", rbyte.length + "");
//                        Log.i("此时WiFi数据第二位",/* String.valueOf(rbyte[2])*/new String(rbyte));
                        if (rbyte[1] == (byte) 0xAA) {
                            Log.i("WiFi数据", "标志位E2收到");
//                            byte init = (byte) 0x99;
//                            if (rbyte[3] != init) {
//                                init = sbyte[3];
                            Log.i("此时WiFi有效数据", String.valueOf(rbyte[2]));
//                            }
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        tese_recive.start();

    }

    public void Res_Tracking() {
        //	TYPE = 0xAA;
        MAJOR = (short) 0xA0;
        FIRST = (byte) 0x00;
        SECOND = (byte) 0x00;
        THRID = (byte) 0x00;
        send();
    }

    //循迹管发射功率设置
    public void set_rate_of_word(int rate) {
        //	TYPE = 0xAA;
        MAJOR = (short) 0xA1;
        FIRST = (short) ((byte) (rate / 256));
        SECOND = (short) ((byte) (rate % 256));
        THRID = (byte) 0x00;
        send();
    }

    public void set_Tracking_Threshold(int LED_number, int LED_threshold) {
        //	TYPE = 0xAA;
        MAJOR = (short) 0xA2;
        FIRST = (short) ((byte) LED_number);
        SECOND = (short) ((byte) (LED_threshold >> 8) & 0xFF);
        THRID = (short) ((byte) (LED_threshold & 0xFF));
        send();
    }

    public void sendvoice(String src) {      //发送语音
        byte[] sbyte = null;
        try {
            sbyte = bytesend(src.getBytes("GBK"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        send_voice(sbyte);
    }

    //合成语音
    private byte[] bytesend(byte[] sbyte) {
        byte[] textbyte = new byte[sbyte.length + 5];
        textbyte[0] = (byte) 0xFD;
        textbyte[1] = (byte) (((sbyte.length + 2) >> 8) & 0xff);
        textbyte[2] = (byte) ((sbyte.length + 2) & 0xff);
        textbyte[3] = 0x01;// 合成语音命令
        textbyte[4] = (byte) 0x01;// 编码格式
        for (int i = 0; i < sbyte.length; i++) {
            textbyte[i + 5] = sbyte[i];
        }
        return textbyte;
    }


    public void send_voice(final byte[] textbyte) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // 发送数据字节数组
                    if (socket != null && !socket.isClosed()) {
                        bOutputStream.write(textbyte, 0, textbyte.length);
                        bOutputStream.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    // 前进
    public void go(int sp_n, int en_n) {
        MAJOR = 0x02;
        FIRST = (byte) (sp_n & 0xFF);
        SECOND = (byte) (en_n & 0xff);
        THRID = (byte) (en_n >> 8);
        send();
    }

    // 后退
    public void back(int sp_n, int en_n) {
        MAJOR = 0x03;
        FIRST = (byte) (sp_n & 0xFF);
        SECOND = (byte) (en_n & 0xff);
        THRID = (byte) (en_n >> 8);
        send();
    }

    //左转
    public void left(int sp_n, int en_n) {

        MAJOR = 0x04;
        FIRST = (byte) (sp_n & 0xFF);
        SECOND = (byte) (en_n & 0xff);
        THRID = (byte) (en_n >> 8);
        send();
    }

    // 右转
    public void right(int sp_n, int en_n) {
        MAJOR = 0x05;
        FIRST = (byte) (sp_n & 0xFF);
        SECOND = (byte) (en_n & 0xff);
        THRID = (byte) (en_n >> 8);
        send();
    }

    // 左转
    public void left(int sp_n) {
        MAJOR = 0x04;
        FIRST = (byte) (sp_n & 0xFF);
        SECOND = 0x00;
        THRID = 0x00;
        send();
    }

    // 右转
    public void right(int sp_n) {
        MAJOR = 0x05;
        FIRST = (byte) (sp_n & 0xFF);
        SECOND = 0x00;
        THRID = 0x00;
        send();
    }

    // 停车
    public void stop() {
        MAJOR = 0x01;
        FIRST = 0x00;
        SECOND = 0x00;
        THRID = 0x00;
        send();
    }

    // 循迹
    public void line(int sp_n) {
        MAJOR = 0x06;
        FIRST = (byte) (sp_n & 0xFF);
        SECOND = 0x00;
        THRID = 0x00;
        send();
    }

    //清零码盘
    public void clear() {
        MAJOR = 0x07;
        FIRST = 0x00;
        SECOND = 0x00;
        THRID = 0x00;
        send();
    }

    public void vice(int i) {//主从车状态转换
        if (i == 1) {//从车状态
            TYPE = 0x02;
            MAJOR = 0x80;
            FIRST = 0x01;
            SECOND = 0x00;
            THRID = 0x00;
            send();
            yanchi(500);

            TYPE = (byte) 0xAA;
            MAJOR = 0x80;
            FIRST = 0x01;
            SECOND = 0x00;
            THRID = 0x00;
            send();
            TYPE = 0x02;
        } else if (i == 2) {//主车状态
            TYPE = 0x02;
            MAJOR = 0x80;
            FIRST = 0x00;
            SECOND = 0x00;
            THRID = 0x00;
            send();
            yanchi(500);

            TYPE = (byte) 0xAA;
            MAJOR = 0x80;
            FIRST = 0x00;
            SECOND = 0x00;
            THRID = 0x00;
            send();
            TYPE = 0xAA;
        }

    }

    // 红外
    public void infrared(byte one, byte two, byte thrid, byte four, byte five, byte six) {
        MAJOR = 0x10;
        FIRST = one;
        SECOND = two;
        THRID = thrid;
        send();
        yanchi(1000);
        MAJOR = 0x11;
        FIRST = four;
        SECOND = five;
        THRID = six;
        send();
        yanchi(1000);
//        MAJOR = 0x12;
//        FIRST = 0x00;
//        SECOND = 0x00;
//        THRID = 0x00;
//        send();


    }

    // 双色LED灯
    public void lamp(byte command) {
        MAJOR = 0x40;
        FIRST = command;
        SECOND = 0x00;
        THRID = 0x00;
        send();
    }

    // 指示灯
    public void light(int left, int right) {
        if (left == 1 && right == 1) {
            MAJOR = 0x20;
            FIRST = 0x01;
            SECOND = 0x01;
            THRID = 0x00;
            send();
        } else if (left == 1 && right == 0) {
            MAJOR = 0x20;
            FIRST = 0x01;
            SECOND = 0x00;
            THRID = 0x00;
            send();
        } else if (left == 0 && right == 1) {
            MAJOR = 0x20;
            FIRST = 0x00;
            SECOND = 0x01;
            THRID = 0x00;
            send();
        } else if (left == 0 && right == 0) {
            MAJOR = 0x20;
            FIRST = 0x00;
            SECOND = 0x00;
            THRID = 0x00;
            send();
        }
    }

    // 蜂鸣器
    public void buzzer(int i) {
        if (i == 1)
            FIRST = 0x01;
        else if (i == 0)
            FIRST = 0x00;
        MAJOR = 0x30;
        SECOND = 0x00;
        THRID = 0x00;
        Log.e("已接受到消息", "蜂鸣器准备启动");
        send();
    }

    public void picture(int i) {// 图片上翻和下翻
        if (i == 1)
            MAJOR = 0x50;
        else
            MAJOR = 0x51;
        FIRST = 0x00;
        SECOND = 0x00;
        THRID = 0x00;
        send();
    }

    public void gear(int i) {// 光照档位加
        if (i == 1)
            MAJOR = 0x61;
        else if (i == 2)
            MAJOR = 0x62;
        else if (i == 3)
            MAJOR = 0x63;
        FIRST = 0x00;
        SECOND = 0x00;
        THRID = 0x00;
        send();
    }

    public void fan() {// 风扇
        MAJOR = 0x70;
        FIRST = 0x00;
        SECOND = 0x00;
        THRID = 0x00;
        send();
    }

    //立体显示
    public void infrared_stereo(short[] data) {
        MAJOR = 0x10;
        FIRST = 0xff;
        SECOND = data[0];
        THRID = data[1];
        send();
        yanchi(500);
        MAJOR = 0x11;
        FIRST = data[2];
        SECOND = data[3];
        THRID = data[4];
        send();
        yanchi(500);
        MAJOR = 0x12;
        FIRST = 0x00;
        SECOND = 0x00;
        THRID = 0x00;
        send();
        yanchi(500);
    }

    public void gate(int i) {// 闸门
        byte type = (byte) TYPE;
        if (i == 1) {
            TYPE = 0x03;
            MAJOR = 0x01;
            FIRST = 0x01;
            SECOND = 0x00;
            THRID = 0x00;
            send();
        } else if (i == 2) {
            TYPE = 0x03;
            MAJOR = 0x01;
            FIRST = 0x02;
            SECOND = 0x00;
            THRID = 0x00;
            send();
        }
        TYPE = type;
    }

    //LCD显示标志物进入计时模式
    public void digital_close() {//数码管关闭
        byte type = (byte) TYPE;
        TYPE = 0x04;
        MAJOR = 0x03;
        FIRST = 0x00;
        SECOND = 0x00;
        THRID = 0x00;
        send();
        TYPE = type;
    }

    public void digital_open() {//数码管打开
        byte type = (byte) TYPE;
        TYPE = 0x04;
        MAJOR = 0x03;
        FIRST = 0x01;
        SECOND = 0x00;
        THRID = 0x00;
        send();
        TYPE = type;
    }

    public void digital_clear() {//数码管清零
        byte type = (byte) TYPE;
        TYPE = 0x04;
        MAJOR = 0x03;
        FIRST = 0x02;
        SECOND = 0x00;
        THRID = 0x00;
        send();
        TYPE = type;
    }

    public void digital_dic(int dis) {//LCD显示标志物第二排显示距离
        byte type = (byte) TYPE;
        TYPE = 0x04;
        MAJOR = 0x04;
        FIRST = 0x00;
        SECOND = (short) (dis / 100);
        THRID = (short) (dis % 100);
        send();
        TYPE = type;
    }

    //	public void digital_dic(int dis){//LCD显示标志物第二排显示距离
//		byte type=(byte) TYPE;
//		int a=0,b=0,c=0;
//
//		a = (dis/100)&(0xF);
//		b = (dis%100/10)&(0xF);
//		c = (dis%10)&(0xF);
//		b = b<<4;
//		b = b|c;
//
//		TYPE = 0x04;
//		MAJOR = 0x04;
//		FIRST = 0x00;
//		SECOND = (short) (a);
//		THRID = (short) (b);
//		send();
//		TYPE = type;
//	}
    public void digital(int i, int one, int two, int three) {// 数码管
        byte type = (byte) TYPE;
        TYPE = 0x04;
        if (i == 1) {//数据写入第一排数码管
            MAJOR = 0x01;
            FIRST = (byte) one;
            SECOND = (byte) two;
            THRID = (byte) three;
        } else if (i == 2) {//数据写入第二排数码管
            MAJOR = 0x02;
            FIRST = (byte) one;
            SECOND = (byte) two;
            THRID = (byte) three;
        }
        send();
        TYPE = type;
    }

    public void arm(int MAIN, int KIND, int COMMAD, int DEPUTY) {
        MAJOR = (short) MAIN;
        FIRST = (byte) KIND;
        SECOND = (byte) COMMAD;
        THRID = (byte) DEPUTY;
        send();
    }

    public void TFT_LCD(int MAIN, int KIND, int COMMAD, int DEPUTY) {
        byte type = (byte) TYPE;
        TYPE = (short) 0x0B;
        MAJOR = (short) MAIN;
        FIRST = (byte) KIND;
        SECOND = (byte) COMMAD;
        THRID = (byte) DEPUTY;
        send();
        TYPE = type;
    }

    public void magnetic_suspension(int MAIN, int KIND, int COMMAD, int DEPUTY) {
        byte type = (byte) TYPE;
        TYPE = (short) 0x0A;
        MAJOR = (short) MAIN;
        FIRST = (byte) KIND;
        SECOND = (byte) COMMAD;
        THRID = (byte) DEPUTY;
        send();
        TYPE = type;
    }

    // 沉睡
    public void yanchi(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
/*********************************************************************************************************/
/*********************************************************************************************************/
    /**
     * 以下为小车自动部分所需
     **/
    public void start() {
        HEAD = 0x32;
        TYPE = 0xEE;
        MAJOR = 0x01;
        FIRST = 0x00;
        SECOND = 0x00;
        THRID = 0x00;
        send();
        yanchi(500);
        HEAD = 0x55;
        TYPE = 0xAA;
        TAIL = 0xBB;
    }

    //RFID数据发送
    public void RFID_data_send(byte[] arr) {

        HEAD = 0x32;
        TYPE = 0x12;

//		MAJOR = (byte) arr[0];
//		FIRST = (byte) arr[1];
//		SECOND = (byte) arr[2];
//		THRID = (byte) arr[3];
//		CHECKSUM = (byte) arr[4];
//		TAIL = (byte) arr[5];
        MAJOR = 65;
        FIRST = 66;
        SECOND = 67;
        THRID = 68;
        CHECKSUM = 69;
        TAIL = 70;
        senddata();
        yanchi(1000);
        HEAD = 0x55;
        TYPE = 0xAA;
        TAIL = 0xBB;
    }

    //发送二维码数据结果
    public void QRCODE_data_send(byte[] mybyte, short num) {
        HEAD = 0x32;
        TYPE = 0x13;

        MAJOR = mybyte[1];
        FIRST = mybyte[2];
        SECOND = mybyte[4];
        try {
            THRID = mybyte[5];
        } catch (Exception e) {
            THRID = 0x00;
        }
        CHECKSUM = num;
        TAIL = 0x00;
        senddata();
        yanchi(1000);
        HEAD = 0x55;
        TYPE = 0xAA;
        TAIL = 0xBB;
    }

    //交通灯发送
    public void TRAFFIC_data_send(int i) {
        HEAD = 0x32;
        TYPE = 0x15;

        if (i == 1)//左转
            MAJOR = 0x01;
        if (i == 2)//右转
            MAJOR = 0x02;
        if (i == 3)//掉头
            MAJOR = 0x03;
        if (i == 4)//禁止左转
            MAJOR = 0x04;
        if (i == 5)//禁止右转
            MAJOR = 0x05;


        FIRST = 0x00;
        SECOND = 0x00;
        THRID = 0x00;
        TAIL = 0xBB;
        send();
        yanchi(1000);
        HEAD = 0x55;
        TYPE = 0xAA;
        TAIL = 0xBB;
    }

    //车牌数据发送
    public void LICENSE_data_send(byte[] sbyte) {
        HEAD = 0x32;
        TYPE = 0x14;

        MAJOR = sbyte[0];
        FIRST = sbyte[3];
        SECOND = sbyte[4];
        THRID = sbyte[5];
        CHECKSUM = sbyte[6];

        try {
            TAIL = sbyte[7];
        } catch (Exception e) {
            TAIL = 0x00;
        }

        senddata();
        yanchi(1000);
        HEAD = 0x55;
        TYPE = 0xAA;
        TAIL = 0xBB;
    }

    //图形数据发送
    public void PICTURE_data_send(byte tr, byte rec, byte cir) {
        HEAD = 0x32;
        TYPE = 0x17;

        MAJOR = rec;
        FIRST = cir;
        SECOND = tr;
        THRID = 0x00;
        CHECKSUM = 0x00;
        TAIL = 0x00;
        senddata();
        yanchi(1000);
        HEAD = 0x55;
        TYPE = 0xAA;
        TAIL = 0xBB;
    }

    /**************************************************************************************************/
    //Start_motion函数参数说明
    //Start_dot
    // B1-------1
    // A2-------2
    // A4-------3
    //fulfil_dot
    //	D------4
    //	E------5
    //	F------6
    private void Start_motion(int Start_dot, int fulfil_dot) {
        TYPE = (short) 0xAA;
        MAJOR = (short) 0xFF;
//        FIRST = (short) 0x00;
//        SECOND = (short) 0x00;
        FIRST = (short) Start_dot;
        SECOND = (short) fulfil_dot;
        THRID = (short) 0x00;
        send();
//        while (rbyte[2] != (byte) (0xF4)) ;        //等待 小车到达TFT标志物
        /*        while (rbyte[2] != (byte) (0xC1)) ;        //等待 小车到达TFT标志物*/
    }

    private void mine_send_qr_result() {
        MAJOR = 0xB5;           //图像内容传输完毕标志
        FIRST = 0x01;
        SECOND = 0x00;
        THRID = 0x00;
        send();
        yanchi(1000);
    }

    private void mine_send_TraffifcLight_result() {
        MAJOR = 0xC0;           //交通灯内容
        if (ValuesApplication.Traffic_Light_Status == ValuesApplication.Traffic_Light_Mode.RED) {
            FIRST = 0x01;           //01红，02绿,03黄
        }
        if (ValuesApplication.Traffic_Light_Status == ValuesApplication.Traffic_Light_Mode.GREEN) {
            FIRST = 0x02;           //01红，02绿,03黄
        }
        if (ValuesApplication.Traffic_Light_Status == ValuesApplication.Traffic_Light_Mode.YELLOW) {
            FIRST = 0x03;           //01红，02绿,03黄
        }

        SECOND = 0x00;
        THRID = 0x00;
        send();
        yanchi(1000);
        MAJOR = 0xC5;           //交通灯传输完毕标志
        FIRST = 0x01;
        SECOND = 0x00;
        THRID = 0x00;
        send();
        yanchi(1000);
    }

    private void mine_send_shape_value() {
        MAJOR = 0xAA;           //图像内容传输完毕标志
        FIRST = 0x01;
        SECOND = 0x00;
        THRID = 0x00;
        send();
        yanchi(1000);
    }

    private void mine_send_car_text_value() {
        TYPE = (short) 0xAA;
        MAJOR = (short) 0xA0;
        FIRST = (short) 0x00;
        SECOND = (short) 0x00;
        THRID = (short) 0x00;
        send();
        yanchi(500);
        TYPE = (short) 0xAA;
        MAJOR = (short) 0xA1;
        FIRST = (short) 0x00;
        SECOND = (short) 0x00;
        THRID = (short) 0x00;
        send();
        yanchi(500);
        TYPE = (short) 0xAA;
        MAJOR = (short) 0xA2;
        FIRST = (short) 0x02;
        SECOND = (short) 0x00;
        THRID = (short) 0x00;
        send();
        yanchi(500);
    }

    //颜色形状、车牌、光源目标档位
    private void send_Car_text_Fruit() {

        yanchi(3000);
        TYPE = (short) 0xAA;
        MAJOR = (short) 0x20;                  //下翻页
        FIRST = (short) 0x02;
        SECOND = (short) 0x00;
        THRID = (short) 0x00;
        send();
        yanchi(2000);

        //B830R5  (7-1)
        //0x42 0x38 0x33 0x00 0x52 0x35
        //H495R6  (7-2)
        //0x48 0x34 0x39 0x35 0x52 0x36
        //P779G9
        //0x50 0x37 0x37 0x39 0x47 0x39
        infrared((byte) 0x50, (byte) 0x37, (byte) 0x37,
                (byte) 0x39, (byte) 0x47, (byte) 0x39);    //发送车牌识别结果  默认 A1B2C3
        yanchi(2000);

        TYPE = (short) 0xAA;
        MAJOR = (short) 0xB2;                        //保存车牌
        FIRST = (short) 0x00;
        SECOND = (short) 0x00;
        THRID = (short) 0x00;
        send();
        yanchi(2000);

        //A1B1C2D1E2  （7-1）
        //A2B2C1D1E3（7-2）
        infrared((byte) 0x02,                        //矩形个数
                (byte) 0x02,                        //圆个数
                (byte) 0x04,                        //三角形个数
                (byte) 0x00,                        //菱形个数
                (byte) 0x00,                        //五角星个数    也是光源目标档位
                (byte) 0x00);
        yanchi(2000);

        TYPE = (short) 0xAA;
        MAJOR = (short) 0x20;                  //上翻页
        FIRST = (short) 0x01;
        SECOND = (short) 0x00;
        THRID = (short) 0x00;
        send();
        yanchi(2000);


        TYPE = (short) 0xAA;
        MAJOR = (short) 0xB3;                        //保存形状数量识别结果
        FIRST = (short) 0x00;
        SECOND = (short) 0x00;
        THRID = (short) 0x00;
        send();
        yanchi(1000);
        while (rbyte[2] != (byte) (0xC2)) ;      //等待到达二维码标志物
    }

    private void send_Caution_Text() {
        infrared((byte) order_data[0], (byte) order_data[1],            //发送红外报警
                (byte) order_data[2], (byte) order_data[3], (byte) order_data[4],
                (byte) order_data[5]);

        yanchi(2000);
        MAJOR = 0x12;
        FIRST = order_data_2[0];
        Log.e("发送光照", Integer.toString(order_data_2[0]));
        SECOND = order_data_2[1];
        Log.e("发送RFID", Integer.toString(order_data_2[1]));
        THRID = 0x00;
        send();

        yanchi(2000);
        TYPE = (short) 0xAA;
        MAJOR = (short) 0xB4;                        //保存红外报警
        FIRST = (short) 0x00;
        SECOND = (short) 0x00;
        THRID = (short) 0x00;
        send();
        yanchi(1000);
    }

    private void algorithm_Data_MyhandlerMsg(int num, String initial_value) {
        if (num == 1)        //RSA密码解密
        {
            algorithm.RSA_Code(initial_value, order_data);
        } else if (num == 2)//仿射密码解密
        {
            algorithm.Affine(initial_value, order_data);
        } else if (num == 3)//CRC校验
        {
            algorithm.CRC_Code(initial_value, order_data);
        }
        //祁天培加的
        else if (num == 4) {
            algorithm.MOSI_Code(initial_value, order_data, order_data_2);

        }
        //qrhandler_show_task(num,31,order_data);
        qrhandler.sendEmptyMessage(31);
    }

    public byte[] order_data = new byte[6];
    public byte[] order_data_2 = new byte[3];

    public int mark = 0;
    private int num = 3;

    public void Full_motion_model_112() {
        switch (mark) {
            case 5:
//                Start_motion(num, 5);                    //开始运行  发送主车起始坐标 与终点坐标
//                if (num == 2)
                mark = 10;
//                else
//                    mark = 11;
                break;

            case 11:
//                MainActivity_two.state_camera = 8;        //调用摄像头1
//                yanchi(3000);
                mark = 10;
                break;

            case 10:
//                send_Car_text_Fruit();                //进行传输 形状个数、车牌参数
//                yanchi(3000);
                mark = 15;
                break;
            case 15:
                MainActivity_two.state_camera = 9;        //调用摄像头1
//                yanchi(6000);
                mark = 20;
                break;
            case 20:
                qrhandler.sendEmptyMessage(10);        //识别二维码
                mark = -25;
                break;
            case 25:
//                algorithm_Data_MyhandlerMsg(3, MainActivity_two.result_qr);
//                mark = 30;
                break;
            case 30:
//                MainActivity_two.state_camera = 10;        //调用摄像头2
                mark = 40;
                break;
            case 40:
//                send_Caution_Text();                //发送红外报警数据
//                mark = 400;
//                MainActivity_two.model_112 = -1;
                break;
            default:
                break;
        }
    }

    /************************************************************************************************************/
    int num_221 = 3;

    public void Full_motion_model_221() {
        switch (mark) {
            case 5:
                Start_motion(num_221, 5);                    //开始运行  发送主车起始坐标 与终点坐标
                if (num_221 == 2)
                    mark = 10;
                else
                    mark = 11;
                break;

            case 11:
                if (num_221 != 2) {
                    MainActivity_two.state_camera = 8;        //调用摄像头1
                    yanchi(4000);
                }
                mark = 10;
                break;

            case 10:
                send_Car_text_Fruit_221();                //进行传输 形状个数、车牌参数
                yanchi(3000);
                mark = 15;
                break;
            case 15:
                MainActivity_two.state_camera = 9;        //调用摄像头1
                yanchi(6000);
                mark = 20;
                break;
            case 20:
                qrhandler.sendEmptyMessage(10);        //识别二维码
                mark = -25;
                break;
            case 25:
                algorithm_Data_MyhandlerMsg(3, MainActivity_two.result_qr);
                mark = 30;
                break;
            case 30:
                MainActivity_two.state_camera = 10;        //调用摄像头2
                mark = 40;
                break;
            case 40:
                send_Caution_Text();                //发送红外报警数据
                mark = 400;
                MainActivity_two.model_221 = -1;
                break;
            default:
                break;
        }
    }

    //颜色形状、车牌、光源目标档位
    private void send_Car_text_Fruit_221() {

        yanchi(3000);
        TYPE = (short) 0xAA;
        MAJOR = (short) 0x20;                  //下翻页
        FIRST = (short) 0x02;
        SECOND = (short) 0x00;
        THRID = (short) 0x00;
        send();
        yanchi(2000);

        //B830R5  (7-1)
        //0x42 0x38 0x33 0x00 0x52 0x35
        //H495R6  (7-2)
        //0x48 0x34 0x39 0x35 0x52 0x36
        infrared((byte) 0x48, (byte) 0x34, (byte) 0x39,
                (byte) 0x35, (byte) 0x52, (byte) 0x36);    //发送车牌识别结果  默认 A1B2C3
        yanchi(2000);

        TYPE = (short) 0xAA;
        MAJOR = (short) 0xB2;                        //保存车牌
        FIRST = (short) 0x00;
        SECOND = (short) 0x00;
        THRID = (short) 0x00;
        send();
        yanchi(2000);

        //A1B1C2D1E2  （7-1）
        //A2B2C1D1E3（7-2）
        infrared((byte) 0x02,                        //矩形个数
                (byte) 0x02,                        //圆个数
                (byte) 0x01,                        //三角形个数
                (byte) 0x01,                        //菱形个数
                (byte) 0x03,                        //五角星个数    也是光源目标档位
                (byte) 0x00);
        yanchi(2000);

        TYPE = (short) 0xAA;
        MAJOR = (short) 0x20;                  //上翻页
        FIRST = (short) 0x01;
        SECOND = (short) 0x00;
        THRID = (short) 0x00;
        send();
        yanchi(2000);


        TYPE = (short) 0xAA;
        MAJOR = (short) 0xB3;                        //保存形状数量识别结果
        FIRST = (short) 0x00;
        SECOND = (short) 0x00;
        THRID = (short) 0x00;
        send();
        yanchi(1000);
        while (rbyte[2] != (byte) (0xC2)) ;      //等待到达二维码标志物
    }


    private void send_TFT_value() {

        short same;
        same = MainActivity_two.set_shape;
        MAJOR = 0x10;
        FIRST = same;                // 0x01/矩形    0x02/圆形  0x03/三角形   0x04/菱形  0x05/梯形   0x06/饼图  0x07/靶图   0x08/条形图
        SECOND = same;
        THRID = same;
        send();
        yanchi(1000);
        MAJOR = 0x11;
        FIRST = same;
        SECOND = same;
        THRID = same;
        send();
        yanchi(1000);
        MAJOR = 0xAA;           //TFT内容传输完毕标志
        FIRST = 0x01;
        SECOND = 0x00;
        THRID = 0x00;
        send();
        yanchi(1000);
    }

    private void send_LCD_value() {
        //AI57N9
//        infrared((byte) 0x41, (byte) 0x49, (byte) 0x35, (byte) 0x37, (byte) 0x4E, (byte) 0x39);
        //B830R5  (7-1)
        //0x42 0x38 0x33 0x00 0x52 0x35
        //H495R6  (7-2)
        //0x48 0x34 0x39 0x35 0x52 0x36
        //P779G9
        //0x50 0x37 0x37 0x39 0x47 0x39
        //C423Q8
        //0x43 0x34 0x32 0x33 0x51 0x38
        //infrared((byte) 0x43, (byte) 0x34, (byte) 0x32, (byte) 0x33, (byte) 0x51, (byte) 0x38);    //发送车牌识别结果
        //L6683B
        //0x4C 0x36 0x36 0x38 0x33 0x42
        //infrared((byte) 0x4C, (byte) 0x36, (byte) 0x36, (byte) 0x38, (byte) 0x33, (byte) 0x42);    //发送车牌识别结果
        //A886F7
        //0x41 0x38 0x38 0x36 0x46 0x37
        //infrared((byte) 0x41, (byte) 0x38, (byte) 0x38, (byte) 0x36, (byte) 0x46, (byte) 0x37);    //发送车牌识别结果

        if (MainActivity_two.run_time == 1) {
            //0x46 0x35 0x37 0x33 0x59 0x38    第一轮
            infrared((byte) 0x46, (byte) 0x35, (byte) 0x37, (byte) 0x33, (byte) 0x59, (byte) 0x38);    //发送车牌识别结果
        }
        if (MainActivity_two.run_time == 2) {
            //0x4A 0x33 0x38 0x36 0x44 0x34     第二轮
            infrared((byte) 0x4A, (byte) 0x33, (byte) 0x38, (byte) 0x36, (byte) 0x44, (byte) 0x34);    //发送车牌识别结果
        }
        if (MainActivity_two.run_time == 3) {
            //0x41 0x38 0x38 0x36 0x46 0x37     测试
            infrared((byte) 0x41, (byte) 0x38, (byte) 0x38, (byte) 0x36, (byte) 0x46, (byte) 0x37);    //发送车牌识别结果
        }


//        infrared((byte) 0x43, (byte) 0x34, (byte) 0x32,
//                (byte) 0x33, (byte) 0x51, (byte) 0x38);    //发送车牌识别结果

        TYPE = (short) 0xAA;
        MAJOR = (short) 0xB3;
        FIRST = (short) 0x00;
        SECOND = (short) 0x00;
        THRID = (short) 0x00;
        send();
        yanchi(2000);
    }

    private void send_QR_value() {
        send_Caution_Text();
    }

//
//    public void moni1_status() {
//
//        MainActivity_two.state_camera = 39;     //调用摄像头4位置，正前方。
////        Start_motion(2, 5);      //  1:B1    2:A2     3:A4
////        while (rbyte[2] != (byte) (0xC1)) ;     //等待到达TFT位置
////        send_Car_text_Fruit();                  //识别车牌，图形图像，内部有等待到达二维码位置
////        while (rbyte[2] != (byte) (0xC2)) ;     //等待到达二维码位置
////        MainActivity_two.state_camera = 33;        //调用摄像头1位置，右下转二维码位置。
//        yanchi(1000);
////        MainActivity_two.state_camera = 506;
////        yanchi(500);
////        MainActivity_two.state_camera = 506;
////        yanchi(500);
//        Log.e("即将识别二维码", MainActivity_two.result_qr);
//        qrhandler.sendEmptyMessage(10);        //识别二维码
//        yanchi(2000);
//        if (mark == 25) {
//            Log.e("二维码是", MainActivity_two.result_qr);
//        }
//
////        algorithm_Data_MyhandlerMsg(4, MainActivity_two.result_qr);
////        send_Caution_Text();                //发送二维码解码信息
////        MainActivity_two.state_camera = 39;     //调用摄像头4位置，正前方。
//
//
//    }

//    public void moni1_2() {
//        switch (mark) {
//            case 5:
//                MainActivity_two.state_camera = 33;        //调用摄像头1
//                Start_motion(num, 5);                    //开始运行  发送主车起始坐标 与终点坐标
//
////                if (num == 2)
//                mark = 10;
////                else
////                    mark = 11;
//                break;
//
//            case 11:
//                MainActivity_two.state_camera = 33;        //调用摄像头1
//                yanchi(3000);
//                mark = 10;
//                break;
//
//            case 10:
//                send_Car_text_Fruit();                //进行传输 形状个数、车牌参数
//                yanchi(1000);
//                mark = 15;
//                break;
//            case 15:
//                MainActivity_two.state_camera = 39;        //调用摄像头4
//                yanchi(1000);
//                mark = 20;
//                break;
//            case 20:
//                qrhandler.sendEmptyMessage(10);        //识别二维码
//                mark = -25;
//                break;
//            case 25:
//                algorithm_Data_MyhandlerMsg(4, MainActivity_two.result_qr);
//                mark = 30;
//                break;
//            case 30:
//                MainActivity_two.state_camera = 10;        //调用摄像头2
//                mark = 40;
//                break;
//            case 40:
//                send_Caution_Text();                //发送红外报警数据
//                yanchi(2000);
////                mark = 400;
//                MainActivity_two.moni1_status = false;
//                break;
//            default:
//                break;
//        }
//    }
//
//    public void moni1_3() {
//        switch (mark) {
//            case 5:
//                MainActivity_two.state_camera = 33;      //一号预设位，正前方
//                Start_motion(0x00, 0x00);                    //开始运行  发送主车起始坐标 与终点坐标
//                mark = 10;
//                break;
//
//            case 10:
//                MainActivity_two.state_camera = 33;
//                //TFT识别形状
//                while (rbyte[2] != (byte) (0xF4)) ; //F4代表到达TFT，需要识别形状
//                yanchi(2000);
//                //识别形状
//                send_TFT_value();//发送B2   之后第一位   0x01/矩形    0x02/圆形  0x03/三角形   0x04/菱形  0x05/梯形   0x06/饼图  0x07/靶图   0x08/条形图  通过type 0x10 0x11 发送
//                mark = 15;
//                break;
//
//            case 15:
//                //LCD车牌识别
//                while (rbyte[2] != (byte) (0xF2)) ; //F2代表到达LCD，需要摄像头右转
//                MainActivity_two.state_camera = 39;
//                yanchi(2000);
//                //车牌识别
//                send_LCD_value();//发送type B3     0x10 0x11 发送6位车牌号的16进制
//                mark = 20;
//                break;
//
//            case 20:
//                //二维码识别
//                while (rbyte[2] != (byte) (0xE2)) ; //F2代表到达LCD，需要摄像头继续右转
//                //识别
//                MainActivity_two.state_camera = 39;
//                qrhandler.sendEmptyMessage(10);
//                mark = -25;
//                break;
//
//            case 25:
//                algorithm_Data_MyhandlerMsg(4, MainActivity_two.result_qr);
//                mark = 30;
//                break;
//
//            case 30:
//                MainActivity_two.state_camera = 33;        //调用摄像头2
//                mark = 40;
//                break;
//
//            case 40:
//                send_QR_value();//发送type B4     0x10 0x11报警码 0x12光强 RFID
//                MainActivity_two.moni1_status = false;
//                break;
//            default:
//                break;
//
//        }
//    }

//    public void moni1_4() {
//        switch (mark) {
//            case 5:
//                MainActivity_two.state_camera = 33;      //一号预设位，正前方
//                qrhandler.sendEmptyMessage(201);
//                Start_motion(0x00, 0x00);                    //开始运行  发送主车起始坐标 与终点坐标
//                qrhandler.sendEmptyMessage(202);
//                mark = 10;
//                break;
//            case 10:
////                MainActivity_two.state_camera = 37;
//                Log.e("等待:", "WIFI");
//                //二维码   接受F5    发B4   摄像头向左
//                while (rbyte[2] != (byte) (0xF5)) ; //F5代表到达二维码处，需要摄像头左转
//                qrhandler.sendEmptyMessage(203);
//                Log.e("WiFi收到", new String(rbyte));
//                //识别
//                MainActivity_two.state_camera = 35;      //二号预设位，左方
//                qrhandler.sendEmptyMessage(204);
//                yanchi(200);
//                qrhandler.sendEmptyMessage(10);
//                mark = -15;
//                break;
//
//            case 15:
//                algorithm_Data_MyhandlerMsg(4, MainActivity_two.result_qr);
//                qrhandler.sendEmptyMessage(205);
//                mark = 20;
//                break;
//
//            case 20:
//                send_QR_value();//发送type B4     0x10 0x11报警码 0x12光强 RFID
//                qrhandler.sendEmptyMessage(206);
//                mark = 25;
//                break;
//
//            case 25:
//                //TFT      接收F4    发B2
//                while (rbyte[2] != (byte) (0xF4)) ;
//                qrhandler.sendEmptyMessage(207);
//                MainActivity_two.state_camera = 37;      //三号预设位，右前方
//                yanchi(2000);
//                //识别形状
//                send_TFT_value();//发送B2   之后第一位   0x01/矩形    0x02/圆形  0x03/三角形   0x04/菱形  0x05/梯形   0x06/饼图  0x07/靶图   0x08/条形图  通过type 0x10 0x11 发送
//                qrhandler.sendEmptyMessage(208);
//                mark = 30;
//                break;
//
//            case 30:
//                //LCD车牌识别
//                //LCD      接收F2    发B3
//                while (rbyte[2] != (byte) (0xF2)) ; //F2代表到达LCD，需要摄像头右转
//                qrhandler.sendEmptyMessage(209);
//                MainActivity_two.state_camera = 39;     //四号预设位，右方
//                qrhandler.sendEmptyMessage(210);
//                yanchi(2000);
//                //车牌识别
//                send_LCD_value();//发送type B3     0x10 0x11 发送6位车牌号的16进制
//                qrhandler.sendEmptyMessage(211);
//                mark = 35;
//                break;
//
//
//            case 35:
//                MainActivity_two.state_camera = 33;        //调用摄像头1
//                qrhandler.sendEmptyMessage(212);
//                mark = 40;
//                break;
//
//            case 40:
//                MainActivity_two.moni1_status = false;
//                break;
//            default:
//                break;
//
//
//            //TFT      接收F4    发B2
//            //LCD      接收F2    发B3
//        }
//
//    }
//}

    public void moni1_5() {

        /*这种写法用来解决while时其它线程变量数据不能被本函数调用的神学问题
        if ((rbyte10[2] != (byte) (0x01)) && (sbyte10[2] != (byte) (0x01))) {
            yanchi(300);
            break;
        }
        else if((rbyte10[0] == (byte)(0x66)) || (sbyte10[0] == (byte)(0x66))) {}*/

        switch (mark) {
            case 5:
                MainActivity_two.state_camera = 39;      //一号预设位，正前方
                qrhandler.sendEmptyMessage(201);
                Start_motion(0x00, 0x00);                    //开始运行  发送主车起始坐标 与终点坐标
                yanchi(100);
                Start_motion(0x00, 0x00);                    //开始运行  发送主车起始坐标 与终点坐标
                yanchi(100);
                Start_motion(0x00, 0x00);                    //开始运行  发送主车起始坐标 与终点坐标
                qrhandler.sendEmptyMessage(202);
                mark = 10;
                break;

            case 10:
                Log.i("等待:", "WIFI01");
//                while ((rbyte[2] != (byte) (0x01)) && (sbyte[2] != (byte) (0x01))) ;
                byte[] rbyte10 = rbyte;
                byte[] sbyte10 = sbyte;
                if ((rbyte10[2] != (byte) (0x01)) && (sbyte10[2] != (byte) (0x01))) {
                    yanchi(300);
                    break;
                }
                else if((rbyte10[0] == (byte)(0x66)) || (sbyte10[0] == (byte)(0x66))) {
                    qrhandler.sendEmptyMessage(203);
//                Log.e("WiFi01收到", String.valueOf(rbyte[2]));
                    MainActivity_two.state_camera = 33;
                    qrhandler.sendEmptyMessage(204);
                    yanchi(1000);
                    mark = 15;
                }
                break;

            case 15:
                mine_send_car_text_value();
                yanchi(10);
                mine_send_car_text_value();
                yanchi(10);
                mine_send_car_text_value();
                yanchi(400);
                mine_send_shape_value();
                yanchi(10);
                mine_send_shape_value();
                yanchi(10);
                mine_send_shape_value();
                yanchi(10);
                mark = 20;
                break;

            case 20:
                Log.i("等待:", "WIFI02");
                //二维码   接受F5    发B4   摄像头向左
                /*while ((rbyte[2] != (byte) (0x02)) && (sbyte[2] != (byte) (0x02)))*/
                byte[] rbyte20 = rbyte;
                byte[] sbyte20 = sbyte;
                if ((rbyte20[2] != (byte) (0x02)) && (sbyte20[2] != (byte) (0x02))) {
                    yanchi(300);
                    break;
                }
                else if((rbyte20[0] == (byte)(0x66)) || (sbyte20[0] == (byte)(0x66))) {
                    qrhandler.sendEmptyMessage(203);
                    Log.i("WiFi02收到4", String.valueOf(rbyte));
                    //识别
                    MainActivity_two.state_camera = 35;
                    qrhandler.sendEmptyMessage(204);
                    yanchi(200);
                    qrhandler.sendEmptyMessage(10);
                    mark = -25;
                }
                break;

            case 25:
//                algorithm_Data_MyhandlerMsg(4, MainActivity_two.result_qr);
                qrhandler.sendEmptyMessage(205);
                mark = 30;
                break;

            case 30:
                yanchi(500);
                mine_send_qr_result();
                yanchi(10);
                mine_send_qr_result();
                yanchi(10);
                mine_send_qr_result();
                yanchi(10);
                MainActivity_two.state_camera = 37;
//                send_QR_value();//发送type B4     0x10 0x11报警码 0x12光强 RFID
                qrhandler.sendEmptyMessage(206);
                mark = 35;
                break;

            case 35:
                //TFT      接收F4    发B2
                Log.i("等待:", "WIFI03");
//                while ((rbyte[2] != (byte) (0x03)) && (sbyte[2] != (byte) (0x03))) ;
                byte[] rbyte35 = rbyte;
                byte[] sbyte35 = sbyte;
//                if (rbyte35[0] == (byte) (0x66)){
                    if ((rbyte35[2] != (byte) (0x03)) && (sbyte35[2] != (byte) (0x03))) {
                        yanchi(300);
                        break;
                    }
                    else if((rbyte35[0] == (byte)(0x66)) || (sbyte35[0] == (byte)(0x66))) {
//                }
                        qrhandler.sendEmptyMessage(207);
                        yanchi(1000);
                        Traffic_Light traffic_light = new Traffic_Light();
                        traffic_light.get_traffic_light_mode(MainActivity_two.bitmap);
                        Log.i("交通灯","识别完毕"+ValuesApplication.Traffic_Light_Status.toString());
                        mine_send_TraffifcLight_result();
                        yanchi(10);
                        mine_send_TraffifcLight_result();
                        yanchi(10);
                        mine_send_TraffifcLight_result();
                        yanchi(10);
                        Log.i("交通灯","识别并发送完毕"+ValuesApplication.Traffic_Light_Status.toString());

                        qrhandler.sendEmptyMessage(208);
                        mark = 40;
                    }
                break;
            case 40:
                MainActivity_two.moni1_status = false;
                break;
            default:
                break;


        }

    }
}
