package com.qtp000.a03cemera_preview.Image;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.qtp000.a03cemera_preview.ValuesApplication;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.abs;

public class Get_Shape {
    Get_Contours get_contours = new Get_Contours();

    public int[][] shape_result = new int[9][5];
    /*0 红色  1 绿色    2 蓝色    3 黄色    4 品色    5 青色    6 黑色    7 白色    8 不区分颜色
     * 0 三角形 1 圆形    2 矩形    3 菱形    4 五角星*/
//    public void get_shape_without_input(){
//        get_all_shape_contours(Bitmap2Mat(ValuesApplication.sourcebitmap));
//
//    }

    public Mat get_all_shape_contours(Mat input) {
        Mat pre_process_mat;
        Mat processing_mat = new Mat();
        Mat processed_mat = new Mat();
//        Toast.makeText(getApplication(), "取出所有图形轮廓", Toast.LENGTH_SHORT).show();


        pre_process_mat = get_contours.Contours_rectandle_get_point_FullScreen(input);      //再次运算得到截取后的图形
//        if (pre_process_mat == null){
//            Log.i("图像处理失败保护","pre_process_mat = get_contours.Contours_rectandle_get_point_FullScreen(input)得到空null了，需要再获取一次图片");
//        }
//        else {
        processed_mat = this.Contours(pre_process_mat);     //得到contour边缘
//        }

        /*处理流程*/


        return processed_mat;
    }

    public Mat get_all_shape_license(Mat input) {
        int backcolor;
        Mat pre_process_mat;
        Mat processed_mat = new Mat();
        pre_process_mat = get_contours.Contours_rectandle_get_point_FullScreen(input);      //再次运算得到截取后的图形

        try {
            Thread.sleep(500);          //延迟，等待图像处理完毕，否则容易得不到正确颜色
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        backcolor = CheckImageBack(pre_process_mat);
        Log.i("get_all_shape_license","颜色"+backcolor);
//        Log.i("get_all_shape_license","正经颜色黄色"+Color.YELLOW);

        switch (backcolor){
            case Color.RED & 0xFFFFFF:
                Log.i("背景颜色","红色");
                break;
            case Color.GREEN & 0xFFFFFF:
                Log.i("背景颜色","绿色");
                break;
            case Color.BLUE & 0xFFFFFF:
                Log.i("背景颜色","蓝色");
                break;
            case Color.YELLOW & 0xFFFFFF:
                Log.i("背景颜色","黄色");
                ValuesApplication.tft_status = ValuesApplication.TFT_status.SHAPE;
                processed_mat = this.Contours2(pre_process_mat);     //得到图形
                return  processed_mat;
            case Color.MAGENTA & 0xFFFFFF:
                Log.i("背景颜色","品色");
                break;
            case Color.CYAN & 0xFFFFFF:
                Log.i("背景颜色","青色");
                ValuesApplication.tft_status = ValuesApplication.TFT_status.License_Plate;
                License_Plate license_plate_qing = new License_Plate();
                license_plate_qing.get_license_plate(Mat2Bitmap(pre_process_mat), "chi_sim");
                ValuesApplication.license_plate_result = license_plate_qing.license_plate_string;
                break;
            case Color.BLACK & 0xFFFFFF:
                Log.i("背景颜色","黑色");
                break;
            case Color.WHITE & 0xFFFFFF:
                Log.i("背景颜色","白色");
                ValuesApplication.tft_status = ValuesApplication.TFT_status.License_Plate;
                License_Plate license_plate_bai = new License_Plate();
                license_plate_bai.get_license_plate(Mat2Bitmap(pre_process_mat), "chi_sim");
                ValuesApplication.license_plate_result = license_plate_bai.license_plate_string;
                break;
        }
/*      //原来的按识别出来的文字数量判断是否为车牌或图形     隐藏序号 A001
        ValuesApplication.tft_status = ValuesApplication.TFT_status.License_Plate;
        License_Plate license_plate = new License_Plate();
        license_plate.get_license_plate(Mat2Bitmap(pre_process_mat), "chi_sim");
        ValuesApplication.license_plate_result = license_plate.license_plate_string;
        if (ValuesApplication.license_plate_result.length() < 5) {
            ValuesApplication.tft_status = ValuesApplication.TFT_status.SHAPE;
            processed_mat = this.Contours2(pre_process_mat);     //得到图形
        }*/

        return processed_mat;
    }

    public Mat Contours(Mat input_mat) {        //膨胀或不膨胀  取所有点或者拐点
        Mat processing_mat;
        Mat processed_mat = new Mat();
//        Toast.makeText(getApplication(), "灰化->边缘检测->边缘", Toast.LENGTH_SHORT).show();
//        Imgproc.equalizeHist(processing_mat,processing_mat);
        processing_mat = this.canny_equalizeHist(input_mat);
//        processing_mat = this.canny_dilate(input_mat);


        double mMinContourArea = 0.07;       //最小轮廓区域
        Mat hierarchy = new Mat();
//        Log.i("之前hierarchy.toString", hierarchy.toString());
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();        //ArrayList可以存放Object

        Imgproc.findContours(processing_mat, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE/*Imgproc.CHAIN_APPROX_SIMPLE*/);
//        Log.i("之后hierarchy.toString", hierarchy.toString());
        Log.i("原始轮廓个数", contours.size() + "");
//        String sss = hierarchy.dump();
//        Log.i("hierarchy转储", sss);
//        long sha = hierarchy.total();

        //学长Imgproc.CHAIN_APPROX_SIMPLE只取了拐点
        //hierarchy[i][后一个轮廓，前一个轮廓，父轮廓，内嵌轮廓]   的编号，没有相应内容的会被置-1,i与contours的编号对应。
//        double maxAr = 320 * 280 * 0.95;
        double maxAr = 640 * 360 * 0.95;
//        Log.i("截取过的面积",input_mat.size().toString()+"");
        double maxArea = 0;
        double mixArea = 0;
        Iterator<MatOfPoint> each = contours.iterator();        //1迭代器，依次取出contours内的各个轮廓
        while (each.hasNext()) {                                //3遍历轮廓集合，得到最大轮廓面积maxArea
            MatOfPoint wrapper = each.next();
            double area = Imgproc.contourArea(wrapper);         //2轮廓List中点的面积
//            Imgproc.contourArea(Mat类型的contours轮廓，顺时针还是逆时针)
            if (area > maxArea)
                maxArea = area;
            mixArea = maxArea;
        }
        Log.i("maxArea:", "" + maxArea);
        /*新建一个List列表，遍历得到大于0.1*最大面积且小于最大面积的集合mContours*/
        List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
        each = contours.iterator();
        while (each.hasNext()) {
            MatOfPoint contour = each.next();
            double area = Imgproc.contourArea(contour);
            Log.i("分别的面积", area + "");
            if (area > mMinContourArea * maxArea && area < maxAr) {
//                if (contour.isSubmatrix())
                mContours.add(contour);
            }
        }
        Log.i("get_shape添加列表到mContours", mContours.size() + "个");
        processed_mat = new Mat(processing_mat.height(), processing_mat.width(), CvType.CV_8UC3);

        /*新建一个List列表，遍历得到大于0.1*最大面积且小于最大面积的集合mContours*/
        Imgproc.drawContours(processed_mat, /*contours*/mContours, -1, new Scalar(255, 0, 0), 1);         //自己加的，画不出来；画出来了
        Log.i("剪切完的图形共有轮廓", mContours.size() + "个");

/*
        List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        List<MatOfPoint> mContour2 = new ArrayList<MatOfPoint>();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();        //ArrayList可以存放Object
        Iterator<MatOfPoint> each = contours.iterator();        //1迭代器，依次取出contours内的各个轮廓
        each = mContours.iterator();
        while (each.hasNext())
        {
            MatOfPoint contour = each.next();
            MatOfPoint2f new_mat = new MatOfPoint2f(contour.toArray());       //把轮廓的点转化为数组并以数组建立新的MatOfPoint对象new_mat
            Imgproc.approxPolyDP(new_mat, approxCurve, 30, true);

            //Imgproc.approxPolyDP(输入的轮廓点的点集，输出的多边形点集，输出精度——和另一个轮廓点的最大距离数;输出精度可以认为是输出的线段的长度，输出的多边形是否闭合)
            long total = approxCurve.total();      //边的数量？   确定，边的数量
//            Log.i("approxCurve",approxCurve.toString());
//            Log.i("approxCurve.total",total+"");
            if (total == 4) {                                  //找到四边形
                MatOfPoint contour2 = new MatOfPoint(approxCurve.toArray());       //把边的点集转化为MatOfPoint
                mContour2.add(contour2);        //把点集的MatOfPoint加入列表mContour2，四边形的集合
            }
        }
        */
//        MatOfPoint test = mContours.get(0);
//        Log.i("test单独", test.toString());
////        test.isSubmatrix()
//        List<Point> test_point = test.toList();
////        Log.i("test的点", test_point.toString());

        if (mContours.size() > 5) {
            Log.i("剪切完的图形认为是", "图形");
            ValuesApplication.tft_status = ValuesApplication.TFT_status.SHAPE;

            int yuan = 0;
            int ju = 0;
            int sanjiao = 0;
            int wujiaoxing = 0;
            int ling = 0;
            List<MatOfPoint> after_DP_MatOfPoint = new ArrayList<MatOfPoint>();
            Iterator<MatOfPoint> pre_approxPolyDP = mContours.iterator();
            while (pre_approxPolyDP.hasNext()) {
                MatOfPoint pre_approxPolyDP_matofpoint = pre_approxPolyDP.next();
                MatOfPoint2f pre_approxPolyDP_matofpoint2f = new MatOfPoint2f(pre_approxPolyDP_matofpoint.toArray());
                MatOfPoint2f after_approxPolyDP_matofpoint2f = new MatOfPoint2f();
                List<Point> pre_approxPolyDP_matofpoint2List = pre_approxPolyDP_matofpoint.toList();
                Imgproc.approxPolyDP(pre_approxPolyDP_matofpoint2f, after_approxPolyDP_matofpoint2f, 15, true);
                Log.i("迭代", "多边形拟合成了" + after_approxPolyDP_matofpoint2f.total() + "边形");
                Log.i("拟合后的图形坐标", after_approxPolyDP_matofpoint2f.toList().toString());

                MatOfPoint after_approxPolyDP_matofpoint = new MatOfPoint(after_approxPolyDP_matofpoint2f.toArray());
                List<Point> after_approxPolyDP_point = after_approxPolyDP_matofpoint.toList();

                switch ((int) after_approxPolyDP_matofpoint2f.total()) {
                    case 3:
                        sanjiao += 1;
                        break;
                    case 10:
                    case 11:
                    case 12:
                        wujiaoxing += 1;
                        break;
                    case 8:
                    case 7:
                    case 6:
                        yuan += 1;
                        break;
                    case 4:
                        RotatedRect wtrect = Imgproc.minAreaRect(after_approxPolyDP_matofpoint2f);
                        Log.i("外接矩形面积", "" + wtrect.size.area());

                        Log.i("矩形面积", Imgproc.contourArea(after_approxPolyDP_matofpoint2f) + "");
                        if (abs(wtrect.size.area() - Imgproc.contourArea(after_approxPolyDP_matofpoint2f)) > 950) {
                            ling += 1;
                        } else {
                            ju += 1;
                        }

                        break;
                }

                after_DP_MatOfPoint.add(after_approxPolyDP_matofpoint);


//            Log.i("迭代", "pre_approxPolyDP_matofpoint2List" + pre_approxPolyDP_matofpoint2List.toString());
//            Log.i("迭代", "pre_approxPolyDP_matofpoint2List 长度" + pre_approxPolyDP_matofpoint2List.size());

            }
            Log.i("after_DP_MatOfPoint", "共有" + after_DP_MatOfPoint.size() + "个元素");
            shape_result[8][0] = sanjiao % 2 == 0 ? sanjiao / 2 : (sanjiao / 2) + 1;
            shape_result[8][1] = yuan % 2 == 0 ? yuan / 2 : (yuan / 2) + 1;
            shape_result[8][2] = ju % 2 == 0 ? ju / 2 : (ju / 2) + 1;
            shape_result[8][3] = ling % 2 == 0 ? ling / 2 : (ling / 2) + 1;
            shape_result[8][4] = wujiaoxing % 2 == 0 ? wujiaoxing / 2 : (wujiaoxing / 2) + 1;
            /*0 红色  1 绿色    2 蓝色    3 黄色    4 品色    5 青色    6 黑色    7 白色    8 不区分颜色
             * 0 三角形 1 圆形    2 矩形    3 菱形    4 五角星*/
            Log.i("图形个数原始", "圆形" + yuan + "  矩形" + ju + "  五角星" + wujiaoxing + "  三角形" + sanjiao + "  菱形" + ling);
            Log.i("图形个数最终", "圆形" + shape_result[8][1] + "  矩形" + shape_result[8][2] + "  五角星" + shape_result[8][4] + "  三角形" + shape_result[8][0] + "  菱形" + shape_result[8][3]);
            ValuesApplication.shape_result = this.shape_result;
        } else {
            Log.i("剪切完的图形认为是", "车牌");
            ValuesApplication.tft_status = ValuesApplication.TFT_status.License_Plate;
            License_Plate license_plate = new License_Plate();
            license_plate.get_license_plate(Mat2Bitmap(input_mat), "chi_sim");
            ValuesApplication.license_plate_result = license_plate.license_plate_string;


        }
        return processed_mat;
    }

    //上面那个是原版，这个是为了主流程识别而复制的，务必要保证两个方法识别参数相同
    public Mat Contours2(Mat input_mat) {        //膨胀或不膨胀  取所有点或者拐点
        Mat processing_mat;
        Mat processed_mat = new Mat();
//        Toast.makeText(getApplication(), "灰化->边缘检测->边缘", Toast.LENGTH_SHORT).show();
//        Imgproc.equalizeHist(processing_mat,processing_mat);
        processing_mat = this.canny_equalizeHist(input_mat);
//        processing_mat = this.canny_dilate(input_mat);


        double mMinContourArea = 0.07;       //最小轮廓区域
        Mat hierarchy = new Mat();
//        Log.i("之前hierarchy.toString", hierarchy.toString());
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();        //ArrayList可以存放Object

        Imgproc.findContours(processing_mat, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE/*Imgproc.CHAIN_APPROX_SIMPLE*/);
//        Log.i("之后hierarchy.toString", hierarchy.toString());
        Log.i("原始轮廓个数", contours.size() + "");
//        String sss = hierarchy.dump();
//        Log.i("hierarchy转储", sss);
//        long sha = hierarchy.total();

        //学长Imgproc.CHAIN_APPROX_SIMPLE只取了拐点
        //hierarchy[i][后一个轮廓，前一个轮廓，父轮廓，内嵌轮廓]   的编号，没有相应内容的会被置-1,i与contours的编号对应。
//        double maxAr = 320 * 280 * 0.95;
        double maxAr = 640 * 360 * 0.95;
//        Log.i("截取过的面积",input_mat.size().toString()+"");
        double maxArea = 0;
        double mixArea = 0;
        Iterator<MatOfPoint> each = contours.iterator();        //1迭代器，依次取出contours内的各个轮廓
        while (each.hasNext()) {                                //3遍历轮廓集合，得到最大轮廓面积maxArea
            MatOfPoint wrapper = each.next();
            double area = Imgproc.contourArea(wrapper);         //2轮廓List中点的面积
//            Imgproc.contourArea(Mat类型的contours轮廓，顺时针还是逆时针)
            if (area > maxArea)
                maxArea = area;
            mixArea = maxArea;
        }
        Log.i("maxArea:", "" + maxArea);
        /*新建一个List列表，遍历得到大于0.1*最大面积且小于最大面积的集合mContours*/
        List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
        each = contours.iterator();
        while (each.hasNext()) {
            MatOfPoint contour = each.next();
            double area = Imgproc.contourArea(contour);
//            Log.i("分别的面积", area + "");
            if (area > mMinContourArea * maxArea && area < maxAr) {
//                if (contour.isSubmatrix())
                mContours.add(contour);
            }
        }
        Log.i("get_shape添加列表到mContours", mContours.size() + "个");
        processed_mat = new Mat(processing_mat.height(), processing_mat.width(), CvType.CV_8UC3);

        /*新建一个List列表，遍历得到大于0.1*最大面积且小于最大面积的集合mContours*/
        Imgproc.drawContours(processed_mat, /*contours*/mContours, -1, new Scalar(255, 0, 0), 1);         //自己加的，画不出来；画出来了
        Log.i("剪切完的图形共有轮廓", mContours.size() + "个");

        int yuan = 0;
        int ju = 0;
        int sanjiao = 0;
        int wujiaoxing = 0;
        int ling = 0;
        List<MatOfPoint> after_DP_MatOfPoint = new ArrayList<MatOfPoint>();
        Iterator<MatOfPoint> pre_approxPolyDP = mContours.iterator();
        while (pre_approxPolyDP.hasNext()) {
            MatOfPoint pre_approxPolyDP_matofpoint = pre_approxPolyDP.next();
            MatOfPoint2f pre_approxPolyDP_matofpoint2f = new MatOfPoint2f(pre_approxPolyDP_matofpoint.toArray());
            MatOfPoint2f after_approxPolyDP_matofpoint2f = new MatOfPoint2f();
            List<Point> pre_approxPolyDP_matofpoint2List = pre_approxPolyDP_matofpoint.toList();
            Imgproc.approxPolyDP(pre_approxPolyDP_matofpoint2f, after_approxPolyDP_matofpoint2f, 15, true);
//            Log.i("拟合后的图形坐标", after_approxPolyDP_matofpoint2f.toList().toString());

            MatOfPoint after_approxPolyDP_matofpoint = new MatOfPoint(after_approxPolyDP_matofpoint2f.toArray());
            List<Point> after_approxPolyDP_point = after_approxPolyDP_matofpoint.toList();

            switch ((int) after_approxPolyDP_matofpoint2f.total()) {
                case 3:
                    sanjiao += 1;
                    break;
                case 10:
                case 11:
                case 12:
                    wujiaoxing += 1;
                    break;
                case 8:
                case 7:
                case 6:
                    yuan += 1;
                    break;
                case 4:
                    RotatedRect wtrect = Imgproc.minAreaRect(after_approxPolyDP_matofpoint2f);
                    Log.i("外接矩形面积", "" + wtrect.size.area());

                    Log.i("矩形面积", Imgproc.contourArea(after_approxPolyDP_matofpoint2f) + "");
                    if (abs(wtrect.size.area() - Imgproc.contourArea(after_approxPolyDP_matofpoint2f)) > 950) {
                        ling += 1;
                    } else {
                        ju += 1;
                    }

                    break;
            }

            after_DP_MatOfPoint.add(after_approxPolyDP_matofpoint);
            Log.i("图形元素多余二十个", "认为是车牌误识别"+"圆形" + shape_result[8][1] + "  矩形" + shape_result[8][2] + "  五角星" + shape_result[8][4] + "  三角形" + shape_result[8][0] + "  菱形" + shape_result[8][3]);

        }
        Log.i("after_DP_MatOfPoint", "共有" + after_DP_MatOfPoint.size() + "个元素");
        if(after_DP_MatOfPoint.size() >=20){
            return processed_mat;
        }
        shape_result[8][0] = sanjiao % 2 == 0 ? sanjiao / 2 : (sanjiao / 2) + 1;
        shape_result[8][1] = yuan % 2 == 0 ? yuan / 2 : (yuan / 2) + 1;
        shape_result[8][2] = ju % 2 == 0 ? ju / 2 : (ju / 2) + 1;
        shape_result[8][3] = ling % 2 == 0 ? ling / 2 : (ling / 2) + 1;
        shape_result[8][4] = wujiaoxing % 2 == 0 ? wujiaoxing / 2 : (wujiaoxing / 2) + 1;
        /*0 红色  1 绿色    2 蓝色    3 黄色    4 品色    5 青色    6 黑色    7 白色    8 不区分颜色
         * 0 三角形 1 圆形    2 矩形    3 菱形    4 五角星*/
//        Log.i("图形个数原始", "圆形" + yuan + "  矩形" + ju + "  五角星" + wujiaoxing + "  三角形" + sanjiao + "  菱形" + ling);
        Log.i("图形个数最终", "圆形" + shape_result[8][1] + "  矩形" + shape_result[8][2] + "  五角星" + shape_result[8][4] + "  三角形" + shape_result[8][0] + "  菱形" + shape_result[8][3]);
        ValuesApplication.shape_result = this.shape_result;

        return processed_mat;
    }

    public Mat canny_equalizeHist(Mat input_mat) {
        int th1 = 50;
        int th2 = 190;

        if (ShapeActivity.canny_th1 != null) {
            th1 = Integer.parseInt(ShapeActivity.canny_th1.getText().toString());
        }
        if (ShapeActivity.canny_th2 != null) {
            th2 = Integer.parseInt(ShapeActivity.canny_th2.getText().toString());
        }
        Mat processing_mat = new Mat();
//        Mat processing_mat2 = new Mat();
        Mat processed_mat = new Mat();

        /*处理流程*/
        Imgproc.cvtColor(input_mat, processing_mat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(processing_mat, processed_mat);                //提升对比度
        Imgproc.Canny(processing_mat, processing_mat, th1, th2);
        Imgproc.dilate(processing_mat, processed_mat, new Mat());

        return processed_mat;
    }

    public Mat canny_dilate(Mat input_mat) {
        int th1 = Integer.parseInt(ShapeActivity.canny_th1.getText().toString());
        int th2 = Integer.parseInt(ShapeActivity.canny_th2.getText().toString());
        Mat processing_mat = new Mat();
        Mat processed_mat2 = new Mat();
        Mat processed_mat = new Mat();
//        Toast.makeText(getApplication(), "灰化->边缘检测->膨胀", Toast.LENGTH_SHORT).show();

        /*处理流程*/
        Imgproc.cvtColor(input_mat, processing_mat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(processing_mat, processed_mat, th1, th2);
        Imgproc.dilate(processed_mat, processed_mat2, new Mat());

        return processed_mat2;
    }

    public int CheckImageBack(Mat src)
    {
        Scalar scalar = getRectScaleFromeMat(src,new Rect(src.width()/2,10,4,4));		//中心线上部取4*4像素
        Scalar scalar1 = getRectScaleFromeMat(src,new Rect(src.width()/2,src.height()-10,4,4));	//中心线上部取4*4像素
        if(scalar == null)
            return -1;
        // Mat Canny_grayMatE = new Mat();
        int rgbR  = (int) scalar.val[0];
        int rgbG  = (int) scalar.val[1];
        int rgbB = (int) scalar.val[2];
        int rgbSum = (rgbR + rgbG + rgbB)/3;//取出rgb的平均值
        int rgbMax = 0;
        if(rgbR > rgbMax)			//找到截取面积内最大RGB
            rgbMax = rgbR;
        if(rgbG > rgbMax)
            rgbMax = rgbG;
        if(rgbB > rgbMax)
            rgbMax = rgbB;
        double light = 1;
        int alp = 50;
        if(rgbMax < 100)//对比度调高
        {
            light = 1;
            alp = 80;
        }
        else if(rgbMax < 170)
        {
            light = 1;				//当前光照强度
            alp = 50;				//当前对比度
        }
        else//�Աȶȵ���//降低亮度对比度？
        {
            light = 0.9;
            alp = 40;
        }
        if(Math.abs(rgbR - rgbSum)<20 &&Math.abs(rgbG - rgbSum)<20&&Math.abs(rgbG - rgbSum)<20)
        {
            //小于20代表三种颜色接近
            //80是经验值，不需要大的改动
//            Log.i("检测颜色时","R="+rgbR);
            int R  = rgbR > 80?255:0;
            int G  = rgbR > 80?255:0;
            int B =  rgbR > 80?255:0;
            rgbR = R;
            rgbG = G;
            rgbB = B;
        }
        else
        {
            if(rgbR >= rgbSum)
                rgbR = 255;
            else
                rgbR = 0;
            if(rgbG >= rgbSum)
                rgbG = 255;
            else
                rgbG = 0;
            if(rgbB >= rgbSum)
                rgbB = 255;
            else
                rgbB = 0;
        }
        int color = Color.argb(0, rgbR, rgbG, rgbB);
        Drawcenter(src,new Point(src.width()/2-2,10-2),new Scalar(255,0,0));		//在中心点向右上方扩展4个像素并画出方框
        if(scalar1 == null)
            return -1;
        // Mat Canny_grayMatE = new Mat();
        int rgbR1  = (int) scalar1.val[0];
        int rgbG1  = (int) scalar1.val[1];
        int rgbB1 = (int) scalar1.val[2];
        int rgbSum1 = (rgbR1 + rgbG1 + rgbB1)/3;//取出rgb的平均值
        int rgbMax1 = 0;
        if(rgbR1 > rgbMax1)
            rgbMax1 = rgbR1;
        if(rgbG1 > rgbMax1)
            rgbMax1 = rgbG1;
        if(rgbB1 > rgbMax1)
            rgbMax1 = rgbB1;
        double light1 = 1;
        int alp1 = 50;
        if(rgbMax1 < 100)//对比度调高
        {
            light1 = 1;
            alp1 = 80;
        }
        else if(rgbMax1 < 170)
        {
            light1 = 1;				//当前光照强度
            alp1 = 50;				//当前对比度
        }
        else//对比度调低
        {
            light1 = 0.9;
            alp1 = 40;
        }
        if(Math.abs(rgbR1 - rgbSum1)<20 &&Math.abs(rgbG1 - rgbSum1)<20&&Math.abs(rgbG1 - rgbSum1)<20)
        {
            //小于20代表三种颜色接近
            //80是经验值，不需要大的改动
            int R  = rgbR1 > 80?255:0;
            int G  = rgbR1 > 80?255:0;
            int B =  rgbR1 > 80?255:0;
            rgbR1 = R;
            rgbG1 = G;
            rgbB1 = B;
        }
        else
        {
            if(rgbR1 >= rgbSum1)
                rgbR1 = 255;
            else
                rgbR1 = 0;
            if(rgbG1 >= rgbSum1)
                rgbG1 = 255;
            else
                rgbG1 = 0;
            if(rgbB1 >= rgbSum1)
                rgbB1 = 255;
            else
                rgbB1 = 0;
        }
        int color1 = Color.argb(0, rgbR1, rgbG1, rgbB1);
        Drawcenter(src,new Point(src.width()/2-2,src.height()-10+2),new Scalar(255,0,0));
//        displayMat(src);
        if(color1 != color)		//上下方取值如果不相同
            return -1;
        return color;		//返回取得的颜色值
    }

    private Scalar getRectScaleFromeMat(Mat src, Rect rect) {
        Scalar mBlobColorHsv = new Scalar(255);

        Mat touchedRegionRgba;
        try {
            touchedRegionRgba = src.submat(rect);
        } catch (CvException e) {
            return null;
        }

        Mat touchedRegionHsv = new Mat();
        //Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

        // Calculate average color of touched region
        mBlobColorHsv = Core.sumElems(touchedRegionRgba);           //取得特征值？？一般用于判断两图片是否相同，翻译名：计算容器值
        int pointCount = rect.width * rect.height;
        for (int i = 0; i < mBlobColorHsv.val.length; i++)
            mBlobColorHsv.val[i] /= pointCount;
        return mBlobColorHsv;
    }

    void Drawcenter(Mat srcMat, Point center, Scalar scalar) {
        int centerX = (int) (center.x - 2);
        int centerY = (int) (center.y - 2);
        List<Point> pointList = new ArrayList<Point>();
        pointList.add(new Point(centerX, centerY));
        pointList.add(new Point(centerX, centerY + 4));
        pointList.add(new Point(centerX + 4, centerY + 4));
        pointList.add(new Point(centerX + 4, centerY));
        List<MatOfPoint> matList = new ArrayList<MatOfPoint>();
        MatOfPoint maoint = new MatOfPoint();
        maoint.fromList(pointList);
        matList.add(maoint);
        Imgproc.drawContours(srcMat, matList, -1, scalar);
    }

    public Mat canny(Mat input_mat) {
        int th1 = Integer.parseInt(ShapeActivity.canny_th1.getText().toString());
        int th2 = Integer.parseInt(ShapeActivity.canny_th2.getText().toString());
//        Log.i("th1","  "+th1);
        Mat processing_mat = new Mat();
        Mat processed_mat = new Mat();
//        Toast.makeText(getApplication(), "灰化->边缘检测", Toast.LENGTH_SHORT).show();
        Imgproc.cvtColor(input_mat, processing_mat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(processing_mat, processed_mat, th1, th2);
//        Imgproc.Canny(processing_mat, processed_mat, 20, 100);
        return processed_mat;
    }

    public Mat Bitmap2Mat(Bitmap in_bitmap) {
        int width = in_bitmap.getWidth();
        int height = in_bitmap.getHeight();
        Mat return_mat = new Mat(height, width, CvType.CV_8UC3);
        Utils.bitmapToMat(in_bitmap, return_mat);
        return return_mat;
    }

    public Bitmap Mat2Bitmap(Mat in_mat) {
        int width = in_mat.width();
        int height = in_mat.height();
        Bitmap return_bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(in_mat, return_bitmap);
        return return_bitmap;

    }

}
