package com.qtp000.a03cemera_preview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.qtp000.a03cemera_preview.Image.ImageBack;
import com.qtp000.a03cemera_preview.Image.ImageBackCheck;
import com.qtp000.a03cemera_preview.Image.ImageShapeBack;
import com.qtp000.a03cemera_preview.Image.ImgPretreatment;
import com.qtp000.a03cemera_preview.Image.LicenseIdentify;
import com.qtp000.a03cemera_preview.Image.RgbLuminanceSource;
import com.qtp000.a03cemera_preview.Image.TrafficImage;
import com.qtp000.a03cemera_preview.used_package.FileService;

import org.opencv.core.Mat;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 祁天培 on 2018/2/9.
 */


public class Function_method extends Activity
{
    public static Socket_connect socket_car = new Socket_connect();
    private Context functionactivity;
    private Context MainActivityContext;
    private int state_camera;
    private TextView voiceText;
    private LicenseIdentify lic_id = new LicenseIdentify();
    private Mat currectMat = null;
//    /*自己加的部分
    public String result_tuxing;
//    * 将Mianactivity的qrhandle传进来，方便在主界面下方显示结果
//    */
//    private Handler mainactivity_handle;
//    public void set_handle(Handler in_handle){
//        mainactivity_handle = in_handle;
//        Log.e("wait","wait");
//        }
//    /*结束*/

    public Function_method(){

    }

    public Function_method(FunctionActivity functionActivity2)
    {
        this.functionactivity = functionActivity2;
    }
    public Function_method(com.qtp000.a03cemera_preview.Socket_connect socket_con,int i,Context ctx)
    {
        this.socket_car = socket_con;
        this.state_camera = i;
        MainActivityContext = ctx;
//		this.IPCamera = IPCamera;
    }
    // 指示灯遥控器
    public void lightController()
    {
        AlertDialog.Builder lt_builder = new AlertDialog.Builder(functionactivity);
        lt_builder.setTitle("指示灯");
        String[] item = { "左亮", "全亮", "右亮", "全灭" };
        lt_builder.setSingleChoiceItems(item, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        if (which == 0) {
                            socket_car.light(1, 0);
                        } else if (which == 1) {
                            socket_car.light(1, 1);
                        } else if (which == 2) {
                            socket_car.light(0, 1);
                        } else if (which == 3) {
                            socket_car.light(0, 0);
                        }
                        dialog.dismiss();
                    }
                });
        lt_builder.create().show();
    }
    // 蜂鸣器
    public void buzzerController()
    {
        AlertDialog.Builder build = new AlertDialog.Builder(functionactivity);
        build.setTitle("蜂鸣器");
        String[] im = { "开", "关" };
        build.setSingleChoiceItems(im, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which == 0)
                        {
                            // 打开蜂鸣器
                            socket_car.buzzer(1);
                        } else if (which == 1)
                        {
                            // 关闭蜂鸣器
                            socket_car.buzzer(0);
                        }
                        dialog.dismiss();
                    }
                });
        build.create().show();
    }
    //闸门控制
    public void gateController()
    {
        AlertDialog.Builder gt_builder = new AlertDialog.Builder(functionactivity);
        gt_builder.setTitle("闸门控制");
        String[] gt = { "开", "关" };
        gt_builder.setSingleChoiceItems(gt, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which == 0)
                        {
                            // 打开闸门
                            socket_car.gate(1);
                        }
                        else if (which == 1)
                        {
                            // 关闭闸门
                            socket_car.gate(2);
                        }
                        dialog.dismiss();
                    }
                });
        gt_builder.create().show();
    }
    // 数码管
    public void digital()
    {
        AlertDialog.Builder dig_timeBuilder = new AlertDialog.Builder(functionactivity);
        dig_timeBuilder.setTitle("数码管");
        String[] dig_item = { "数码管显示", "数码管计时", "显示距离" };
        dig_timeBuilder.setSingleChoiceItems(dig_item, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which == 0)
                        {	// 数码管显示
                            digitalController();
                        }
                        else if (which == 1)
                        {	// 数码管计时
                            digital_time();
                        }
                        else if (which == 2)
                        {	// 显示距离
                            digital_dis();
                        }
                        dialog.dismiss();
                    }
                });
        dig_timeBuilder.create().show();
    }

    private int dgtime_index = -1;
    private void digital_time() {// 数码管计时
        AlertDialog.Builder dg_timeBuilder = new AlertDialog.Builder(functionactivity);
        dg_timeBuilder.setTitle("数码管计时");
        String[] dgtime_item = { "计时结束", "计时开始", "计时清零" };
        dg_timeBuilder.setSingleChoiceItems(dgtime_item, dgtime_index,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which == 0)
                        {	// 计时结束
                            socket_car.digital_close();
                        }
                        else if (which == 1)
                        {	// 计时开启
                            socket_car.digital_open();
                        }
                        else if (which == 2)
                        {	// 计时清零
                            socket_car.digital_clear();
                        }
                        dialog.dismiss();
                    }
                });
        dg_timeBuilder.create().show();
    }

    private int dgdis_index = -1;
    private void digital_dis() {
        AlertDialog.Builder dis_timeBuilder = new AlertDialog.Builder(functionactivity);
        dis_timeBuilder.setTitle("显示距离");
        final String[] dis_item = { "10cm", "20cm", "40cm" };
        dis_timeBuilder.setSingleChoiceItems(dis_item, dgdis_index,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which == 0)
                        {	// 距离10cm
                            socket_car.digital_dic(Integer.parseInt(dis_item[which].substring(0, 2)));
                        }
                        else if (which == 1)
                        {	// 距离20cm
                            socket_car.digital_dic(Integer.parseInt(dis_item[which].substring(0, 2)));
                        }
                        else if (which == 2)
                        {	// 距离40cm
                            socket_car.digital_dic(Integer.parseInt(dis_item[which].substring(0, 2)));
                        }
                        dialog.dismiss();
                    }
                });
        dis_timeBuilder.create().show();
    }
    // 数码管显示方法
    private String[] itmes = { "1", "2" };
    int main, one, two, three;
    private void digitalController()
    {
        AlertDialog.Builder dg_Builder = new AlertDialog.Builder(functionactivity);
        View view = LayoutInflater.from(functionactivity).inflate(R.layout.item_digital, null);
        dg_Builder.setTitle("数码管显示");
        dg_Builder.setView(view);
        // 下拉列表
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        final EditText editText1 = (EditText) view.findViewById(R.id.editText1);
        final EditText editText2 = (EditText) view.findViewById(R.id.editText2);
        final EditText editText3 = (EditText) view.findViewById(R.id.editText3);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                functionactivity, android.R.layout.simple_spinner_item, itmes);
        spinner.setAdapter(adapter);
        // 下拉列表选择监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                main = position + 1;
            }
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        dg_Builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //获取你输入的信息
                        String ones = editText1.getText().toString();
                        String twos = editText2.getText().toString();
                        String threes = editText3.getText().toString();
                        // 显示数据，一个文本编译框最多两个数据显示数目管中两个数据
                        if (ones.equals(""))
                            one = 0x00;
                        else
                            one = Integer.parseInt(ones) / 10 * 16
                                    + Integer.parseInt(ones) % 10;
                        if (twos.equals(""))
                            two = 0x00;
                        else
                            two = Integer.parseInt(twos) / 10 * 16
                                    + Integer.parseInt(twos) % 10;
                        if (threes.equals(""))
                            three = 0x00;
                        else
                            three = Integer.parseInt(threes) / 10 * 16
                                    + Integer.parseInt(threes) % 10;
                        socket_car.digital(main, one, two, three);
                    }
                });
        dg_Builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();
                    }
                });
        dg_Builder.create().show();
    }
    //语音播报
    private boolean flag_voice;
    public void voiceController()
    {
        View view = LayoutInflater.from(functionactivity).inflate(R.layout.item_car, null);
        voiceText = (EditText) view.findViewById(R.id.voiceText);
        AlertDialog.Builder voiceBuilder = new AlertDialog.Builder(functionactivity);
        voiceBuilder.setTitle("语音播报");
        voiceBuilder.setView(view);
        voiceBuilder.setPositiveButton("播报",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String src = voiceText.getText().toString();
                        if (src.equals(""))
                        {
                            src = "请输入你要播报的内容";
                        }
                        try
                        {
                            flag_voice=true;
                            byte[] sbyte = bytesend(src.getBytes("GBK"));
                            socket_car.send_voice(sbyte);
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        dialog.cancel();
                    }
                });
        voiceBuilder.setNegativeButton("取消", null);
        voiceBuilder.create().show();
    }
    private byte[] bytesend(byte[] sbyte)
    {
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

    // 报警器
    public void policeController()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(functionactivity);
        builder.setTitle("报警器");
        String[] item2 = { "开", "关" };
        builder.setSingleChoiceItems(item2,-1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which == 0)
                        {
                            socket_car.infrared((byte) 0x03, (byte) 0x05,
                                    (byte) 0x14, (byte) 0x45, (byte) 0xDE,
                                    (byte) 0x92);
                        }
                        else if (which == 1)
                        {
                            socket_car.infrared((byte) 0x67, (byte) 0x34,
                                    (byte) 0x78, (byte) 0xA2, (byte) 0xFD,
                                    (byte) 0x27);
                        }
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
    //灯光档位器
    public void gearController()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(functionactivity);
        builder.setTitle("档位遥控器");
        String[] gr_item = { "光强加1档", "光强加2档", "光强加3档" };
        builder.setSingleChoiceItems(gr_item, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which == 0)
                        {	// 加一档
                            socket_car.gear(1);
                        }
                        else if (which == 1)
                        {	// 加二档
                            socket_car.gear(2);
                        }
                        else if (which == 2)
                        {	// 加三档
                            socket_car.gear(3);
                        }
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
    //立体显示
    private short[] data = { 0x00, 0x00, 0x00, 0x00, 0x00 };
    public void threeDisplay()
    {
        AlertDialog.Builder Builder = new AlertDialog.Builder(functionactivity);
        Builder.setTitle("立体显示");
        String[] three_item = { "颜色信息", "图形信息", "距离信息", "车牌信息", "路况信息", "默认信息" };
        Builder.setSingleChoiceItems(three_item, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which)
                        {
                            case 0:
                                color();
                                break;
                            case 1:
                                shape();
                                break;
                            case 2:
                                dis();
                                break;
                            case 3:
                                lic();
                                break;
                            case 4:
                                road();
                                break;
                            case 5:
                                data[0] = 0x15;
                                data[1] = 0x01;
                                socket_car.infrared_stereo(data);
                                break;
                            default:
                                break;
                        }
                        dialog.cancel();
                    }
                });
        Builder.create().show();
    }
    //向立体显示发送颜色信息
    private void color()
    {
        AlertDialog.Builder colorBuilder = new AlertDialog.Builder(functionactivity);
        colorBuilder.setTitle("颜色信息");
        String[] lg_item = { "红色", "绿色", "蓝色", "黄色", "紫色", "青色", "黑色", "白色" };
        colorBuilder.setSingleChoiceItems(lg_item, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        data[0] = 0x13;
                        data[1] = (short) (which + 0x01);
                        socket_car.infrared_stereo(data);
                        dialog.cancel();
                    }
                });
        colorBuilder.create().show();
    }
    //向立体显示发送形状信息
    private void shape()
    {
        AlertDialog.Builder shapeBuilder = new AlertDialog.Builder(functionactivity);
        shapeBuilder.setTitle("图形信息");
        String[] shape_item = { "矩形", "圆形", "三角形", "菱形", "梯形", "饼图", "靶图","条形图" };
        shapeBuilder.setSingleChoiceItems(shape_item, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        data[0] = 0x12;
                        data[1] = (short) (which + 0x01);
                        socket_car.infrared_stereo(data);
                        dialog.cancel();
                    }
                });
        shapeBuilder.create().show();
    }
    //向立体显示发送路况信息
    private void road()
    {
        AlertDialog.Builder roadBuilder = new AlertDialog.Builder(functionactivity);
        roadBuilder.setTitle("路况信息");
        String[] road_item = { "隧道有事故，请绕行", "前方施工，请绕行" };
        roadBuilder.setSingleChoiceItems(road_item, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        data[0] = 0x14;
                        data[1] = (short) (which + 0x01);
                        socket_car.infrared_stereo(data);
                        dialog.cancel();
                    }
                });
        roadBuilder.create().show();
    }
    //向立体显示发送距离信息
    private void dis()
    {
        AlertDialog.Builder disBuilder = new AlertDialog.Builder(functionactivity);
        disBuilder.setTitle("距离信息");
        final String[] road_item = { "10cm", "15cm", "20cm", "28cm", "39cm" };
        disBuilder.setSingleChoiceItems(road_item, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        int disNum = Integer.parseInt(road_item[which].substring(0, 2));
                        data[0] = 0x11;
                        data[1] = (short) (disNum / 10 + 0x30);
                        data[2] = (short) (disNum % 10 + 0x30);
                        socket_car.infrared_stereo(data);
                        dialog.cancel();
                    }
                });
        disBuilder.create().show();
    }
    //从string中得到short数据数组
    private short[] StringToBytes(String licString)
    {
        if (licString == null || licString.equals(""))
        {
            return null;
        }
        licString = licString.toUpperCase();
        int length = licString.length();
        char[] hexChars = licString.toCharArray();
        short[] d = new short[length];
        for (int i = 0; i < length; i++)
        {
            d[i] = (short) hexChars[i];
        }
        return d;
    }
    private Handler licHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            short [] li=StringToBytes(lic_item[msg.what]);
            data[0] = 0x20;
            data[1] = (short) (li[0]);
            data[2]=(short) (li[1]);
            data[3]=(short) (li[2]);
            data[4]=(short) (li[3]);
            socket_car.infrared_stereo(data);
            data[0] = 0x10;
            data[1] = (short) (li[4]);
            data[2]=(short) (li[5]);
            data[3]=(short) (li[6]);
            data[4]=(short) (li[7]);
            socket_car.infrared_stereo(data);
        };
    };
    private int lic = -1;
    private String[] lic_item = { "N300Y7A4", "N600H5B4", "N400Y6G6", "J888B8C8" };
    private void lic()
    {
        AlertDialog.Builder licBuilder = new AlertDialog.Builder(functionactivity);
        licBuilder.setTitle("车牌信息");
        licBuilder.setSingleChoiceItems(lic_item, lic,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        lic = which;
                        licHandler.sendEmptyMessage(which);
                        dialog.cancel();
                    }
                });
        licBuilder.create().show();
    }
    //图片信息
    public void pictureController()
    {
        AlertDialog.Builder pt_builder = new AlertDialog.Builder(functionactivity);
        pt_builder.setTitle("图片遥控器");
        String[] pt_item = { "上翻", "下翻" };
        pt_builder.setSingleChoiceItems(pt_item, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which == 0)
                        {	// 图片上翻
                            socket_car.picture(1);
                        }
                        else if (which == 1)
                        {	// 图片下翻
                            socket_car.picture(0);
                        }
                        dialog.dismiss();// 取消对话框
                    }
                });
        pt_builder.create().show();// 创建对话框和显示
    }
    //磁悬浮
    public void magnetic_suspension()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(functionactivity);
        builder.setTitle("磁悬浮");
        String[] item2 = { "开", "关" };
        builder.setSingleChoiceItems(item2,-1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which == 0)
                        {
                            socket_car.magnetic_suspension(0x01,0x01,0x00,0x00);
                        }
                        else if (which == 1)
                        {
                            socket_car.magnetic_suspension(0x01,0x02,0x00,0x00);
                        }
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
    private Timer timer;
    public static String result_qr;
    public int msg = 10;
    // 二维码
    public void QrCode() //Handler qrHandler = new Handler()
    {
        timer = new Timer();
        timer.schedule(new TimerTask()
        {
            public void run()
            {
                Result result = null;
                RgbLuminanceSource rSource = new RgbLuminanceSource(MainActivity_two.bitmap);
                try
                {
                    BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rSource));
                    Map<DecodeHintType, String> hint = new HashMap<DecodeHintType, String>();
                    hint.put(DecodeHintType.CHARACTER_SET, "utf-8");
                    QRCodeReader reader = new QRCodeReader();
                    result = reader.decode(binaryBitmap, hint);
                    Log.e("二维码的内容", result.toString());
                    result_qr = result.toString();
                    System.out.println("开始保存二维码图片");
                    new FileService().savePhoto(MainActivity_two.bitmap ,R.string.QrCode_Name + ".png");
                    System.out.println("识别完毕");
                    timer.cancel();
                    return;
                }
                catch (Exception e)
                {
                    Log.e("出错了出错了", "出错了二维码又出错了");
                    e.printStackTrace();
                }
            }
        }, 0, 200);
        System.out.println("哈哈哈哈"+result_qr);
        return;
    };
    //TFT显示屏
    /************************************************************************************************************/
    public void TFT_LCD()
    {
        AlertDialog.Builder TFTbuilder = new AlertDialog.Builder(functionactivity);
        TFTbuilder.setTitle("TFT显示器");
        String[] TFTitem = { "图片显示模式", "车牌显示","计时模式","距离显示","HEX显示模式" };
        TFTbuilder.setSingleChoiceItems(TFTitem,-1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch(which)
                        {
                            case 0:
                                TFT_Image();
                                break;
                            case 1:
                                TFT_plate_number();
                                break;
                            case 2:
                                TFT_Timer();
                                break;
                            case 3:
                                Distance();
                                break;
                            case 4:
                                Hex_show();
                                break;
                        }
                        dialog.dismiss();
                    }
                });
        TFTbuilder.create().show();
    }

    private void TFT_Image()
    {
        AlertDialog.Builder TFT_Image_builder = new AlertDialog.Builder(functionactivity);
        TFT_Image_builder.setTitle("图片显示模式");
        String[] TFT_Image_item = {"指定显示","上翻一页","下翻一页","自动翻页"};
        TFT_Image_builder.setSingleChoiceItems(TFT_Image_item, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO 自动生成的方法存根
                switch(which)
                {
                    case 0:
                        LCD_vo_show();
                        break;
                    case 1:
                        socket_car.TFT_LCD(0x10, 0x01, 0x00, 0x00);
                        break;
                    case 2:
                        socket_car.TFT_LCD(0x10, 0x02, 0x00, 0x00);
                        break;
                    case 3:
                        socket_car.TFT_LCD(0x10, 0x03, 0x00, 0x00);
                        break;
                }
                dialog.cancel();
            }
        });
        TFT_Image_builder.create().show();
    }

    private void LCD_vo_show()
    {
        AlertDialog.Builder TFT_Image_builder = new AlertDialog.Builder(functionactivity);
        TFT_Image_builder.setTitle("指定图片显示");
        String[] TFT_Image_item = {"1","2","3","4","5"};
        TFT_Image_builder.setSingleChoiceItems(TFT_Image_item, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO 自动生成的方法存根
                switch(which)
                {
                    case 0:
                        socket_car.TFT_LCD(0x00, 0x01, 0x00, 0x00);
                        break;
                    case 1:
                        socket_car.TFT_LCD(0x00, 0x02, 0x00, 0x00);
                        break;
                    case 2:
                        socket_car.TFT_LCD(0x00, 0x03, 0x00, 0x00);
                        break;
                    case 3:
                        socket_car.TFT_LCD(0x00, 0x04, 0x00, 0x00);
                        break;
                    case 4:
                        socket_car.TFT_LCD(0x00, 0x05, 0x00, 0x00);
                        break;
                }
                dialog.cancel();
            }
        });
        TFT_Image_builder.create().show();
    }


    int Car_one, Car_two, Car_three, Car_four, Car_five, Car_six;
    private void TFT_plate_number()
    {
        AlertDialog.Builder TFT_plate_builder = new AlertDialog.Builder(functionactivity);
        TFT_plate_builder.setTitle("车牌显示模式");
        final String[] TFT_Image_item = {"A123B4","B567C8","D910E1"};
        TFT_plate_builder.setSingleChoiceItems(TFT_Image_item, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO 自动生成的方法存根
                switch(which)
                {
                    case 0:
                        socket_car.TFT_LCD(0x20, 'A', '1', '2');
                        socket_car.yanchi(500);
                        socket_car.TFT_LCD(0x21, '3', 'B', '4');
                        break;
                    case 1:
                        socket_car.TFT_LCD(0x20, 'B', '5', '6');
                        socket_car.yanchi(500);
                        socket_car.TFT_LCD(0x21, '7', 'C', '8');
                        break;
                    case 2:
                        socket_car.TFT_LCD(0x20, 'D', '9', '1');
                        socket_car.yanchi(500);
                        socket_car.TFT_LCD(0x21, '0', 'E', '1');
                        break;
                }
                dialog.cancel();
            }
        });
        TFT_plate_builder.create().show();
    }

    private void TFT_Timer()
    {
        AlertDialog.Builder TFT_Iimer_builder = new AlertDialog.Builder(functionactivity);
        TFT_Iimer_builder.setTitle("计时模式");
        String[] TFT_Image_item = {"开始","关闭","停止"};
        TFT_Iimer_builder.setSingleChoiceItems(TFT_Image_item, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO 自动生成的方法存根
                switch(which)
                {
                    case 0:
                        socket_car.TFT_LCD(0x30, 0x01, 0x00, 0x00);
                        break;
                    case 1:
                        socket_car.TFT_LCD(0x30, 0x02, 0x00, 0x00);
                        break;
                    case 2:
                        socket_car.TFT_LCD(0x30, 0x00, 0x00, 0x00);
                        break;
                }
                dialog.cancel();
            }
        });
        TFT_Iimer_builder.create().show();
    }

    private void Distance()
    {
        AlertDialog.Builder TFT_Distance_builder = new AlertDialog.Builder(functionactivity);
        TFT_Distance_builder.setTitle("距离显示模式");
        String[] TFT_Image_item = {"10cm","20cm","30cm"};
        TFT_Distance_builder.setSingleChoiceItems(TFT_Image_item, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO 自动生成的方法存根
                if(which == 0)
                {
                    socket_car.TFT_LCD(0x50, 0x00, 0x01, 0x00);
                }
                if(which == 1)
                {
                    socket_car.TFT_LCD(0x50, 0x00, 0x02, 0x00);
                }
                if(which == 2)
                {
                    socket_car.TFT_LCD(0x50, 0x00, 0x03, 0x00);
                }
                dialog.cancel();
            }
        });
        TFT_Distance_builder.create().show();
    }

    private void Hex_show()
    {
        AlertDialog.Builder TFT_Hex_builder = new AlertDialog.Builder(functionactivity);
        TFT_Hex_builder.setTitle("HEX显示模式");
        String[] TFT_Image_item = {"0xAA、0x01、0xBB","0xAA、0x02、0x77","0x55、0x03、0x33"};
        TFT_Hex_builder.setSingleChoiceItems(TFT_Image_item, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO 自动生成的方法存根
                if(which == 0)
                {
                    socket_car.TFT_LCD(0x40, 0xAA, 0x01, 0xBB);
                }
                if(which == 1)
                {
                    socket_car.TFT_LCD(0x40, 0xAA, 0x02, 0x77);
                }
                if(which == 2)
                {
                    socket_car.TFT_LCD(0x40, 0x55, 0x03, 0x33);
                }
                dialog.cancel();
            }
        });
        TFT_Hex_builder.create().show();
    }
    //车牌
    /**********************************************************************************************************/
    private Bitmap licbitmap = null;
    private Bitmap licbit = null;
    public static String textResult;
    public void carLicence()
    {
        ImageBack imageBack = null;
        Bitmap bitma = null;
        licbitmap  = Bitmap.createBitmap(MainActivity_two.bitmap);
        imageBack = new ImageBack(licbitmap);
        //imageBack.ImageBackRun();
        if(currectMat == null)
            imageBack.ImageBackRun();
        else
            imageBack.ImageBackRun(currectMat);
        bitma = imageBack.getBitmap();

        //获取车牌图片
        licbit = lic_id.findcontourdraw(bitma);
        if(licbit == null)
        {
            textResult = null;
            return;
        }
        //对比度
        licbitmap = lic_id.Contrast(licbit);
        //二值化
        try {
            licbitmap = ImgPretreatment.doPretreatment(licbitmap);
        } catch (ArrayIndexOutOfBoundsException e) {
            licbitmap = ImgPretreatment.doPretreatment(licbitmap);
        }catch(Exception e){
            Log.e("还是出错", "二值化失败");
        }
        licbitmap = lic_id.LicenseShow(licbitmap);

        if(licbitmap != null)
        {
            new FileService().savePhoto(licbitmap ,R.string.Licence_Name + ".png");
            textResult = lic_id.doOcr(licbitmap , "chi_sim");
            Log.e("车牌的识别结果为", textResult);
//			Toast.makeText(functionactivity, textResult, 1).show();
        }
        else
            Toast.makeText(functionactivity, "识别错误...", Toast.LENGTH_SHORT).show();
    }

    /************************************************************************************************************/
    //交通灯
    private Bitmap bitmap = null;
    private Bitmap trabitmap;
    public static String traffic_result;
    //	private int picflag = 0;
    public void checkTraffic()
    {
        trabitmap = MainActivity_two.bitmap;
        new FileService().savePhoto(MainActivity_two.bitmap ,R.string.Traffic_Name + ".png");
        if(trabitmap == null)
            return;
        List<Integer> trafficList = null;
        Bitmap bitma = null;
        TrafficImage trafficImage = null;
        //bitmap  = Bitmap.createBitmap(newbitmap);
        bitmap  = Bitmap.createBitmap(trabitmap);
        trafficImage = new TrafficImage(bitmap);
        //for(int i = 0; i< 5;i++)
        {
            trafficImage.TrafficImageRun();
            bitma = trafficImage.getBitmap();
            trafficList =  trafficImage.getEmitConturList();

            //if(trafficList != null)
            //	break;
        }

        //ImageBack imageBack = new ImageBack(bitmap);

        if(trafficList == null ||trafficList.size() == 0)
        {
            Log.e("识别错误", "车牌识别错误");
//			Toast.makeText(functionactivity, "识别错误", 1).show();
//			if(bitma!= null)
//			{
//				picflag = 1;
//				showView.setImageBitmap(bitma);
//			}
//			result.setText("error");
            return;
        }
        int direction = trafficList.get(0);
        int color = trafficList.get(1);
        String str = "";
        if(direction == -1)
        {
            str +="error";
        }else
        {
            if(direction== 0&&color ==0xFF0000)
            {
                str +="禁止左转";
            }else if(direction== 0&&color ==0x00FF00)
            {
                str +="左转";
            }else if(direction== 1&&color ==0xFF0000)
            {
                str +="禁止右转";
            }else if(direction== 1&&color ==0x00FF00)
            {
                str +="右转";
            }else if(direction== 2&&color ==0xFF0000)
            {
                str +="禁止掉头";
            }else if(direction== 2&&color ==0x00FF00)
            {
                str +="掉头";
            }else
            {
                str +="error";
            }
        }
        traffic_result = str;
//		Toast.makeText(functionactivity, str, 1).show();
//		result.setText(str);
//		showView.setImageBitmap(bitma);
//		picflag = 1;
    }
    ///********************************************************************************************************
///////////////////////////////////图形图像识别
    public static Bitmap shapebitmap = null;
    private Bitmap shapebit = MainActivity_two.bitmap;
    public static String shaperesult = "";
    //颜色
    public String[] color = new String[4];
    //形状
    public String[] shape = new String[5];
    //个数
    public int num[][] = new int[8][5];//第一个数代表颜色，第二个代表形状
    public void checkShecp()
    {
        shaperesult = "";
        shapebitmap = MainActivity_two.bitmap;
        new FileService().savePhoto(shapebitmap ,R.string.Picture_Name + ".png");
        if(shapebitmap == null)
            return;
        ImageShapeBack imageShapeBack = null;
        Bitmap bitma = null;
        //bitmap  = Bitmap.createBitmap(newbitmap);
        //从摄像头那里获得图片
        shapebit  = Bitmap.createBitmap(shapebitmap);
        imageShapeBack = new ImageShapeBack(shapebit);
        //图片处理
        if(currectMat == null)
            imageShapeBack.ImageBackRun();
        else
            imageShapeBack.ImageBackRun(currectMat);
        //获取处理后的图片
        shapebitmap = imageShapeBack.getBitmap();
        List<ImageShapeBack.ImageInfo> emitList = imageShapeBack.getImageInfoList();
        if(emitList == null ||emitList.size() == 0)
        {
//			if(bitma!= null)
//			{	picflag = 1;
//				showView.setImageBitmap(bitma);
//			}
//			result.setText("error");
            return;
        }
        //颜色
        String[] colorString = {"红色","绿色","蓝色","黄色","品色","青色","黑色","白色"};
        //形状
        String[] shapString = {"三角形","矩形","圆形","五角星","菱形"};
        //个数
        int emitArry[][]  = new int[8][5];

        for(int i = 0;i<emitList.size();i++)
        {
            ImageShapeBack.ImageInfo imageInfo = emitList.get(i);
            if(imageInfo == null)
                continue;
            int type = imageInfo.type;
            int color = imageInfo.color;
            for(int j = 0; j < 5;j++)
            {
                int ty = type-1;
                if(ty==j && color ==0xFF0000)//红色
                    emitArry[0][ty]++;
                else if(ty==j && color ==0x00FF00)//绿色
                    emitArry[1][ty]++;
                else if(ty==j && color ==0x0000FF)//蓝色
                    emitArry[2][ty]++;
                else if(ty==j && color ==0xFFFF00)//黄色
                    emitArry[3][ty]++;
                else if(ty==j && color ==0xFF00FF)//品色
                    emitArry[4][ty]++;
                else if(ty==j && color ==0x00FFFF)//青色
                    emitArry[5][ty]++;
                else if(ty==j && color ==0x000000)//黑色
                    emitArry[6][ty]++;
                else if(ty==j && color ==0xFFFFFF)//白色
                    emitArry[7][ty]++;
            }
        }
        for(int i = 0;i<8;i++)
        {
            for(int j = 0;j<5;j++)
            {
                shaperesult += colorString[i] + shapString[j] + emitArry[i][j] + "个;";

                try{
//					color[i] = colorString[i];
//	//				Log.e("识别到的颜色：" + colorString[i], "要操作的颜色：" + color[i]);
//	//
//					shape[j] = shapString[j];
//	//				Log.e("识别到的形状：" + shapString[j], "要操作的形状：" + shape[j]);
//	//
                    num[i][j] = emitArry[i][j];
                    //				Log.e("识别到的个数：" + emitArry[i][j], "要操作的个数：" + num[i][j]);
                }
                catch(Exception e)
                {
                    return;
                }
//				Log.e("**********************", "********************************");
            }
            shaperesult +="\n";
//            /*自己加的*/
            result_tuxing = shaperesult;
//            mainactivity_handle.sendEmptyMessage(100);
//            /*自己加的*/

        }
    }

    ///********************************************************************************************************
///////////////////////////////////图形图像识别
    public static Bitmap backbitmap = null;
    private Bitmap backbit = MainActivity_two.bitmap;

    public int checkBack()//检测背景颜色
    {
        shaperesult = "";
        backbitmap = MainActivity_two.bitmap;
        new FileService().savePhoto(backbitmap ,R.string.Picture_Name + ".png");
        if(backbitmap == null)
            return -1;
        ImageBackCheck imageBackCheck = null;
        Bitmap bitma = null;
        //bitmap  = Bitmap.createBitmap(newbitmap);
        //从摄像头那里获得图片
        backbit  = Bitmap.createBitmap(backbitmap);
        imageBackCheck = new ImageBackCheck(backbit);
        //图片处理
        imageBackCheck.ImageBackRun();
        //shapebitmap = imageBackCheck.getBitmap();
        //获取处理后的图片
        backbit = imageBackCheck.getBitmap();
        int color = imageBackCheck.getBackColor();

        currectMat = imageBackCheck.getImageMat();

        return 	color;
    }
}

