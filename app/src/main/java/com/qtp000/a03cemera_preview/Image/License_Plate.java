package com.qtp000.a03cemera_preview.Image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.qtp000.a03cemera_preview.MainActivity_two;
import com.qtp000.a03cemera_preview.ValuesApplication;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class License_Plate {
    public String license_plate_string,true_ocr_string;
    Get_Contours get_contours = new Get_Contours();
    Get_Shape get_shape = new Get_Shape();

    public void get_license_plate(Bitmap input_bitmap, String language) {
        Bitmap pre_ocr = getCenterPicture(input_bitmap);
        TessBaseAPI tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.init(getSDPath(), language);
        tessBaseAPI.setImage(pre_ocr);
        String ocr_return_text = tessBaseAPI.getUTF8Text();
        true_ocr_string = getLicense_plate_string_true(ocr_return_text);
        license_plate_string = true_ocr_string;
        tessBaseAPI.clear();
        tessBaseAPI.end();
    }

////    pre_process_mat = get_contours.Contours_rectandle_get_point_FullScreen(input);      //再次运算得到截取后的图形
//    public Bitmap get_full_screen_bitmap(Bitmap input_bitmap){
//        Mat pre_full_screen,prossing_mat;
//        Bitmap full_screen_bitmap;
//        pre_full_screen = get_shape.Bitmap2Mat(input_bitmap);
//        prossing_mat = get_contours.Contours_rectandle_get_point_FullScreen(pre_full_screen);
//        full_screen_bitmap = get_shape.Mat2Bitmap(prossing_mat);
//        return full_screen_bitmap;
//    }


    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // �ж�sd���Ƿ����
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// ��ȡ���Ŀ¼
        }
        return sdDir.toString();
    }

    private String getLicense_plate_string_true(String input_string) {      //把非所需字符排除掉
        String true_string = new String();
        String num_word = "Az Bz Cz Dz Ez Fz Gz Hz Iz Jz Kz Lz Mz Nz Oz Pz Qz Rz Sz Tz Uz Vz Wz Xz Yz Zz 1z 2z 3z 4z 5z 6z 7z 8z 9z 0z "; /*国z*/
        for (int i = 0; i < input_string.length(); i++) {
            char c = input_string.charAt(i);
            if (c == 'ó'){
                c = '6';
            }
            if (c == '」'){
                c = 'J';
            }

            Log.i("车牌处理", "第" + i + "位是" + c);
            if (num_word.contains(c + "z")) {
                Log.i("车牌处理", "该字符为正常字符" + "第" + i + "位是" + c);
                true_string = true_string + c;
            }
        }
        Log.i("车牌处理", "最终结果" + true_string);
        ValuesApplication.license_plate_result_byte = true_string.getBytes();
        Log.i("车牌处理", "最终结果长度" + ValuesApplication.license_plate_result_byte.length);


        for (Byte b :
                ValuesApplication.license_plate_result_byte) {
            Log.i("车牌处理", "转为Byte  " + "0x"+Integer.toHexString(b));

        }
        return true_string;
    }

    public Bitmap getCenterPicture(Bitmap bitmap) {
        Mat bmpMat = new Mat();//为传入的创建Mat
        Mat gausMat = new Mat();//高斯模糊后的Mat图片
        Mat grayMat = new Mat();
        Mat hierarchy = new Mat();
        Mat bwmMat = new Mat();
        Mat dilateMat = new Mat();
        Mat erodeMat = new Mat();

        bmpMat = Bitmap2Mat(bitmap);
        Imgproc.GaussianBlur(bmpMat, gausMat, new Size(7, 7), 0);
        Imgproc.cvtColor(gausMat, grayMat, Imgproc.COLOR_RGB2GRAY);//灰化

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();//建立坐標集合
        Imgproc.findContours(grayMat, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        for (int idx = 0; idx < contours.size(); idx++) {
            Scalar color = new Scalar(255, 0, 0);
            //这个方法的功能是按比例去中间图片  最后的数字是比例值
            Imgproc.drawContours(grayMat, contours, idx, new Scalar(255, 0, 0), 75);
            Log.e("运行到这里", "已经执行");
        }
        //0 <= _colRange.start && _colRange.start <= _colRange.end && _colRange.end <= m.cols
        Mat pic = grayMat.submat(100, 250, 0, 640);

//        int width = pic.width();
//        int height = pic.height();
//        System.out.println("宽："+width + ",高：" + height);

        Bitmap bmp = Mat2Bitmap(pic);

        //当获取到图片亮度过暗，黑白处理后可能全变成黑色，
        // 所以在处理前需要调整亮度，修改下面方法亮度参数即可
        bmp = ContrastPicture(bmp);//提高对比度

        Bitmap bwbmp = ImgPretreatment.doPretreatment(bmp);//将图片黑白处理

        bwmMat = Bitmap2Mat(bwbmp);

        Imgproc.erode(bwmMat, erodeMat, new Mat());//图片的腐蚀处理
        Imgproc.dilate(erodeMat, dilateMat, new Mat());//图片膨胀处理
        Imgproc.GaussianBlur(dilateMat, gausMat, new Size(3, 3), 0);//高斯模糊
        Imgproc.dilate(gausMat, dilateMat, new Mat());//图片膨胀处理
        bmp = Mat2Bitmap(dilateMat);


        return bmp;
    }

    //提高对比度
    private Bitmap ContrastPicture(Bitmap bmp)
    {
        ColorMatrix cm = new ColorMatrix();
        float brightness = 30;  //亮度
        float contrast = 2;        //对比度
        cm.set(new float[] {
                contrast, 0, 0, 0, brightness,
                0, contrast, 0, 0, brightness,
                0, 0, contrast, 0, brightness,
                0, 0, 0, contrast, 0
        });
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        //显示图片
        Matrix matrix = new Matrix();
        Bitmap createBmp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        Canvas canvas = new Canvas(createBmp);
        canvas.drawBitmap(bmp, matrix, paint);
        return createBmp;
    }



    private Mat Bitmap2Mat(Bitmap in_bitmap) {
        int width = in_bitmap.getWidth();
        int height = in_bitmap.getHeight();
        Mat return_mat = new Mat(height, width, CvType.CV_8UC3);
        Utils.bitmapToMat(in_bitmap, return_mat);
        return return_mat;
    }

    private Bitmap Mat2Bitmap(Mat in_mat) {
        int width = in_mat.width();
        int height = in_mat.height();
        Bitmap return_bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(in_mat, return_bitmap);
        return return_bitmap;

    }


}
