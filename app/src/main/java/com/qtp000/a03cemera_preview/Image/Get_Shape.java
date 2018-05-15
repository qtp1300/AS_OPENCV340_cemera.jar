package com.qtp000.a03cemera_preview.Image;

import android.util.Log;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Get_Shape {
    Get_Contours get_contours = new Get_Contours();

    public Mat get_all_shape_contours(Mat input) {
        Mat pre_process_mat;
        Mat processing_mat = new Mat();
        Mat processed_mat = new Mat();
//        Toast.makeText(getApplication(), "取出所有图形轮廓", Toast.LENGTH_SHORT).show();


        pre_process_mat = get_contours.Contours_rectandle_get_point_FullScreen(input);

//        processed_mat = Contours(pre_process_mat);


        /*处理流程*/


        return processed_mat;
    }

    public Mat Contours(Mat input_mat) {        //膨胀或不膨胀  取所有点或者拐点
        Mat processing_mat;
        Mat processed_mat = new Mat();
//        Toast.makeText(getApplication(), "灰化->边缘检测->边缘", Toast.LENGTH_SHORT).show();

        processing_mat = get_contours.canny_dilate(input_mat);

        double mMinContourArea = 0.07;       //最小轮廓区域
        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();        //ArrayList可以存放Object

        Imgproc.findContours(processing_mat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        //学长Imgproc.CHAIN_APPROX_SIMPLE只取了拐点
        //hierarchy[i][后一个轮廓，前一个轮廓，父轮廓，内嵌轮廓]   的编号，没有相应内容的会被置-1,i与contours的编号对应。
        double maxAr = 320 * 280 * 0.95;
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
            if (area > mMinContourArea * maxArea && area < maxAr) {
                mContours.add(contour);
            }
        }
        Log.i("添加完点的列表到mContours", "");
        processed_mat = new Mat(processing_mat.height(), processing_mat.width(), CvType.CV_8UC3);

        /*新建一个List列表，遍历得到大于0.1*最大面积且小于最大面积的集合mContours*/
        Imgproc.drawContours(processed_mat, /*contours*/mContours, -1, new Scalar(255, 0, 0), 1);         //自己加的，画不出来；画出来了
        Log.i("剪切完的图形共有轮廓", mContours.size() + "个");
        if (mContours.size() > 5){
            Log.i("剪切完的图形认为是", "图形");
        }
        else {
            Log.i("剪切完的图形认为是", "车牌");
        }
        return processed_mat;
    }

}
