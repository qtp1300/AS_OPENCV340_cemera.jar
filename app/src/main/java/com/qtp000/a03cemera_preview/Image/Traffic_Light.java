package com.qtp000.a03cemera_preview.Image;

import android.graphics.Bitmap;
import android.util.Log;

import com.qtp000.a03cemera_preview.ValuesApplication;

public class Traffic_Light {
    //获取不是黑色的像素点
    private int red = 0;
    private int green = 0;
    private int yellow = 0;
    //设置显色比例
    private int ColorProportion = 0;

    private Bitmap convertToBlack(Bitmap bip) {// 像素处理背景变为黑色，红绿黄不变
        Log.e("---------------", "%%%%%%%%%%%%%%%%%------  " + bip);
        int width = bip.getWidth();
        int height = bip.getHeight();
        int[] pixels = new int[width * height];
        bip.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] pl = new int[bip.getWidth() * bip.getHeight()];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                int pixel = pixels[offset + x];
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
                if (r > 200 && g < 150 && b < 150) {// 红色
                    pl[offset + x] = pixel;
                } else if (r < 150 && g > 170 && b < 150) {// 绿色
                    pl[offset + x] = pixel;
                } else {
                    pl[offset + x] = 0xff000000;// 黑色
                }
            }
        }
        Bitmap result = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        result.setPixels(pl, 0, width, 0, 0, width, height);
        return result;
    }

    public boolean get_traffic_light_mode(Bitmap bit) {
//        System.out.println("开始获取像素点");
        Bitmap bitmap = convertToBlack(bit);
        //获取图片的长和宽
        int width = bit.getWidth();
        int height = bit.getHeight();
        //建立一个能存所有像素点数组
        int[] pixels = new int[width * height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = bitmap.getPixel(x, y);
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
                if (r > 0 && g > 0 && b > 0)
                {
//                    Log.i("红：" + r,"绿:" + g);
//                    Log.i("蓝：" + b,"over...");
                    if (r > 200 && g > 80 && b > 100) {
                        red++;
                    }
                    if (r > 60 && g > 200 && b > 100) {
                        green++;
                    }
                    if (r > 200 && g > 140 && b > 100) {
                        yellow++;
                    }
                }
            }
        }
        ColorProportion = red - green;
        ColorProportion = Math.abs(ColorProportion);
//		datashow.setText("红色" + red + "，绿色" + green + ",黄色" + yellow + ",比例值" + ColorProportion);
        //判断是否为绿灯
        if (/*red>20 && */ green > 0 /*&& yellow < 10*/) {
            ValuesApplication.Traffic_Light_Status = ValuesApplication.Traffic_Light_Mode.GREEN;
        }
        //判断是否为红灯
        else if (red > 100 && green < 5 && yellow < 10) {
            ValuesApplication.Traffic_Light_Status = ValuesApplication.Traffic_Light_Mode.RED;
        }
        //判断是否为黄灯
        else if (red < 60 && green < 5 && yellow < 30) {
            ValuesApplication.Traffic_Light_Status = ValuesApplication.Traffic_Light_Mode.YELLOW;
        } else {
            ValuesApplication.Traffic_Light_Status = ValuesApplication.Traffic_Light_Mode.YELLOW;
        }
//        endTime=System.currentTimeMillis();
//        time = endTime - starTime;
//		datashow.setText("运行时间为：" + time + "ms");
//        datashow.setText("红色" + red + ", 绿色" + green + ",黄色" + yellow + ",比例值" + ColorProportion + ", 运行时间：" + time + "ms");
        red = 0;green = 0;yellow = 0;
        return true;
    }
}
