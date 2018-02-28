package com.qtp000.a03cemera_preview;

import android.os.Message;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by 祁天培 on 2018/2/28.
 */


public class M_SocketConnect_Send {

    private DataInputStream bInputStream;
    private DataOutputStream bOutputStream;

    public short HEAD = 0x55;
    public short TYPE=0xAA;
    public short MAJOR = 0x00;
    public short FIRST = 0x00;
    public short SECOND = 0x00;
    public short THRID = 0x00;
    public short CHECKSUM=0x00;
    public short TAIL = 0xBB;

    private Socket socket;
    public byte[] recivebyte = new byte[40];


    public void send_start_motion(int start_dot,int final_dot){
        TYPE = (short) 0xAA;
        MAJOR = (short) 0xB1;
        FIRST = (short) start_dot;
        SECOND = (short) final_dot;
        THRID = (short) 0x00;
        send();
        while(recivebyte[2] != (byte)(0xC1));		//等待 小车到达TFT标志物


    }

    //发送数据
    public void send()
    {
        try
        {
            //计算通信协议校验和
            CHECKSUM = (byte) ((MAJOR+FIRST+SECOND+THRID) % 256);
            // 发送数据字节数组，此处根据小车的通信协议来写
            byte[] sbyte = { (byte)HEAD,(byte)TYPE, (byte) MAJOR, (byte) FIRST, (byte) SECOND, (byte) THRID ,(byte)CHECKSUM,(byte) 0xBB};
            bOutputStream.write(sbyte, 0, sbyte.length);
            bOutputStream.flush();
        }
        catch (IOException e)
        {
            Log.e("消息发送失败", "通信协议出错");
            e.printStackTrace();
        }
    }

    private Thread reThread = new Thread(new Runnable() {
        @Override
        public void run() {
            // TODO Auto1-generated method stub
            while (socket != null && !socket.isClosed())
            {
                try {
                    bInputStream.read(recivebyte);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = recivebyte;
                    reHandler.sendMessage(msg);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    });

    public void wait(int time)
    {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
