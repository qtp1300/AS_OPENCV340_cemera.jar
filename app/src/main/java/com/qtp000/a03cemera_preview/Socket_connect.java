package com.qtp000.a03cemera_preview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

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
    public byte[] rbyte = new byte[40];
    private Handler reHandler;
    public short TYPE = 0xAA;
    public short MAJOR = 0x00;
    public short FIRST = 0x00;
    public short SECOND = 0x00;
    public short THRID = 0x00;
    public short CHECKSUM = 0x00;
    public short HEAD = 0x55;
    public short TAIL = 0xBB;

    private Handler qrhandler;
    @SuppressWarnings("unused")
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
            // TODO Auto1-generated method stub
            while (socket != null && !socket.isClosed()) {
                try {
                    bInputStream.read(rbyte);
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
    public void send() {
        try {
            //计算通信协议校验和
            CHECKSUM = (byte) ((MAJOR + FIRST + SECOND + THRID) % 256);
            // 发送数据字节数组，此处根据小车的通信协议来写
            byte[] sbyte = {(byte) HEAD, (byte) TYPE, (byte) MAJOR, (byte) FIRST, (byte) SECOND, (byte) THRID, (byte) CHECKSUM, (byte) 0xBB};
            bOutputStream.write(sbyte, 0, sbyte.length);
            bOutputStream.flush();
//            Log.e("消息发送", "已发送");
        } catch (IOException e) {
            Log.e("消息发送失败", "通信协议出错");
            e.printStackTrace();
        }
    }

    //发送字符数据
    public void senddata() {
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

    //	public void send_voice(byte [] textbyte)
//	{
//		try {
//			// 发送数据字节数组
//			if (socket != null && !socket.isClosed()) {
//				bOutputStream.write(textbyte, 0, textbyte.length);
//				bOutputStream.flush();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
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

    //左转45or右转45
    public void halfturn(int i) {
//		MAJOR = 0x09;
        if (i == 1)//左
        {
            MAJOR = 0x08;
            FIRST = 0x00;
            SECOND = 0x00;
            THRID = 0x00;
            send();
        } else if (i == 2)//右
        {
            MAJOR = 0x09;
            FIRST = 0x00;
            SECOND = 0x00;
            THRID = 0x00;
            send();
        }
//		TYPE = (byte) 0xAA;
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
    public void Start_motion(int Start_dot, int fulfil_dot) {
        TYPE = (short) 0xAA;
        MAJOR = (short) 0xB1;
//        FIRST = (short) 0x00;
//        SECOND = (short) 0x00;
        FIRST = (short) Start_dot;
        SECOND = (short) fulfil_dot;
        THRID = (short) 0x00;
        send();
        while (rbyte[2] != (byte) (0xF4)) ;        //等待 小车到达TFT标志物
/*        while (rbyte[2] != (byte) (0xC1)) ;        //等待 小车到达TFT标志物*/
    }

    //颜色形状、车牌、光源目标档位
    public void send_Car_text_Fruit() {

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

    public void send_Caution_Text() {
        infrared((byte) order_data[0], (byte) order_data[1],            //发送红外报警
                (byte) order_data[2], (byte) order_data[3], (byte) order_data[4],
                (byte) order_data[5]);

        yanchi(2000);
        MAJOR = 0x12;
        FIRST = order_data_2[0];
        Log.e("发送光照",Integer.toString(order_data_2[0]));
        SECOND = order_data_2[1];
        Log.e("发送RFID",Integer.toString(order_data_2[1]));
        THRID = 0x00;
        send();
//
        yanchi(2000);
        TYPE = (short) 0xAA;
        MAJOR = (short) 0xB4;                        //保存红外报警
        FIRST = (short) 0x00;
        SECOND = (short) 0x00;
        THRID = (short) 0x00;
        send();
        yanchi(1000);
    }

    public void algorithm_Data_MyhandlerMsg(int num, String initial_value) {
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
            algorithm.MOSI_Code(initial_value, order_data,order_data_2);

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
//                MainActivity.state_camera = 8;        //调用摄像头1
//                yanchi(3000);
                mark = 10;
                break;

            case 10:
//                send_Car_text_Fruit();                //进行传输 形状个数、车牌参数
//                yanchi(3000);
                mark = 15;
                break;
            case 15:
                MainActivity.state_camera = 9;        //调用摄像头1
//                yanchi(6000);
                mark = 20;
                break;
            case 20:
                qrhandler.sendEmptyMessage(10);        //识别二维码
                mark = -25;
                break;
            case 25:
//                algorithm_Data_MyhandlerMsg(3, MainActivity.result_qr);
//                mark = 30;
                break;
            case 30:
//                MainActivity.state_camera = 10;        //调用摄像头2
                mark = 40;
                break;
            case 40:
//                send_Caution_Text();                //发送红外报警数据
//                mark = 400;
//                MainActivity.model_112 = -1;
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
                    MainActivity.state_camera = 8;        //调用摄像头1
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
                MainActivity.state_camera = 9;        //调用摄像头1
                yanchi(6000);
                mark = 20;
                break;
            case 20:
                qrhandler.sendEmptyMessage(10);        //识别二维码
                mark = -25;
                break;
            case 25:
                algorithm_Data_MyhandlerMsg(3, MainActivity.result_qr);
                mark = 30;
                break;
            case 30:
                MainActivity.state_camera = 10;        //调用摄像头2
                mark = 40;
                break;
            case 40:
                send_Caution_Text();                //发送红外报警数据
                mark = 400;
                MainActivity.model_221 = -1;
                break;
            default:
                break;
        }
    }

    //颜色形状、车牌、光源目标档位
    public void send_Car_text_Fruit_221() {

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




    public void send_TFT_value(){
        short same = 0x02;
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
        MAJOR = 0xB2;           //TFT内容传输完毕标志
        FIRST = 0x01;
        SECOND = 0x00;
        THRID = 0x00;
        send();
        yanchi(1000);
    }
    public void send_LCD_value(){
        //B830R5  (7-1)
        //0x42 0x38 0x33 0x00 0x52 0x35
        //H495R6  (7-2)
        //0x48 0x34 0x39 0x35 0x52 0x36
        //P779G9
        //0x50 0x37 0x37 0x39 0x47 0x39
        //C423Q8            一轮
        //0x43 0x34 0x32 0x33 0x51 0x38
        infrared((byte) 0x43, (byte) 0x34, (byte) 0x32, (byte) 0x33, (byte) 0x51, (byte) 0x38);    //发送车牌识别结果
        //L6683B            二轮
        //0x4C 0x36 0x36 0x38 0x33 0x42
        //infrared((byte) 0x4C, (byte) 0x36, (byte) 0x36, (byte) 0x38, (byte) 0x33, (byte) 0x42);    //发送车牌识别结果
        //A886F7            测试
        //0x41 0x38 0x38 0x36 0x46 0x37
//        infrared((byte) 0x41, (byte) 0x38, (byte) 0x38, (byte) 0x36, (byte) 0x46, (byte) 0x37);    //发送车牌识别结果

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
    public void send_QR_value(){
        send_Caution_Text();
    }


    public void moni1() {

        MainActivity.state_camera = 39;     //调用摄像头4位置，正前方。
//        Start_motion(2, 5);      //  1:B1    2:A2     3:A4
//        while (rbyte[2] != (byte) (0xC1)) ;     //等待到达TFT位置
//        send_Car_text_Fruit();                  //识别车牌，图形图像，内部有等待到达二维码位置
//        while (rbyte[2] != (byte) (0xC2)) ;     //等待到达二维码位置
//        MainActivity.state_camera = 33;        //调用摄像头1位置，右下转二维码位置。
        yanchi(1000);
//        MainActivity.state_camera = 506;
//        yanchi(500);
//        MainActivity.state_camera = 506;
//        yanchi(500);
        Log.e("即将识别二维码",MainActivity.result_qr);
        qrhandler.sendEmptyMessage(10);        //识别二维码
        yanchi(2000);
        if(mark == 25)
        {
            Log.e("二维码是",MainActivity.result_qr);
        }

//        algorithm_Data_MyhandlerMsg(4, MainActivity.result_qr);
//        send_Caution_Text();                //发送二维码解码信息
//        MainActivity.state_camera = 39;     //调用摄像头4位置，正前方。


    }

    public void moni1_2() {
        switch (mark) {
            case 5:
                MainActivity.state_camera = 33;        //调用摄像头1
                Start_motion(num, 5);                    //开始运行  发送主车起始坐标 与终点坐标

//                if (num == 2)
                mark = 10;
//                else
//                    mark = 11;
                break;

            case 11:
                MainActivity.state_camera = 33;        //调用摄像头1
                yanchi(3000);
                mark = 10;
                break;

            case 10:
                send_Car_text_Fruit();                //进行传输 形状个数、车牌参数
                yanchi(1000);
                mark = 15;
                break;
            case 15:
                MainActivity.state_camera = 39;        //调用摄像头4
                yanchi(1000);
                mark = 20;
                break;
            case 20:
                qrhandler.sendEmptyMessage(10);        //识别二维码
                mark = -25;
                break;
            case 25:
                algorithm_Data_MyhandlerMsg(4, MainActivity.result_qr);
                mark = 30;
                break;
            case 30:
                MainActivity.state_camera = 10;        //调用摄像头2
                mark = 40;
                break;
            case 40:
                send_Caution_Text();                //发送红外报警数据
                yanchi(2000);
//                mark = 400;
                MainActivity.moni1 = false;
                break;
            default:
                break;
        }
    }
    public void moni1_3(){
        switch(mark){
            case 5:
                MainActivity.state_camera = 33;      //一号预设位，正前方
                Start_motion(0x00,0x00);                    //开始运行  发送主车起始坐标 与终点坐标
                mark = 10;
                break;

            case 10:
                MainActivity.state_camera = 33;
                //TFT识别形状
                while (rbyte[2] != (byte) (0xF4)) ; //F2代表到达TFT，需要识别形状
                //识别形状
                send_TFT_value();//发送B2   之后第一位   0x01/矩形    0x02/圆形  0x03/三角形   0x04/菱形  0x05/梯形   0x06/饼图  0x07/靶图   0x08/条形图  通过type 0x10 0x11 发送
                mark = 15;
                break;

            case 15:
                //LCD车牌识别
                while (rbyte[2] != (byte) (0xF2)) ; //F2代表到达LCD，需要摄像头右转
                MainActivity.state_camera = 39;
                //车牌识别
                send_LCD_value();//发送type B3     0x10 0x11 发送6位车牌号的16进制
                mark = 20;
                break;

            case 20:
                //二维码识别
                while (rbyte[2] != (byte) (0xE2)) ; //F2代表到达LCD，需要摄像头继续右转
                //识别
                MainActivity.state_camera = 39;
                qrhandler.sendEmptyMessage(10);
                mark = -25;
                break;

            case 25:
                algorithm_Data_MyhandlerMsg(4, MainActivity.result_qr);
                mark = 30;
                break;

            case 30:
                MainActivity.state_camera = 33;        //调用摄像头2
                mark = 40;
                break;

            case 40:
                send_QR_value();//发送type B4     0x10 0x11报警码 0x12光强 RFID
                MainActivity.moni1 = false;
                break;
            default:
                break;

        }
    }

}
