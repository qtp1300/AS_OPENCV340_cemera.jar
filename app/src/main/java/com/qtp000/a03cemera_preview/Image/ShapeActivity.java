package com.qtp000.a03cemera_preview.Image;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qtp000.a03cemera_preview.R;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ShapeActivity extends AppCompatActivity {
    public static Bitmap input_bitmap, temp_bitmap;
    Button grey_btn, Binarization_btn, canny_btn, geting_btn, canny_dilate_btn, contours_btn,dilate_contours_btn,dilate_contours_rectandle_btn;
    ImageView shape1, shape2;
    TextView canny_th1, canny_th2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shape_use);
        Init();
    }

    private void Init() {
        canny_th1 = findViewById(R.id.canny_th1);
        canny_th2 = findViewById(R.id.canny_th2);
        shape1 = findViewById(R.id.shape1);
        shape2 = findViewById(R.id.shape2);
        grey_btn = findViewById(R.id.btn_gray);
        Binarization_btn = findViewById(R.id.btn_Binarization);
        canny_btn = findViewById(R.id.btn_canny);
        geting_btn = findViewById(R.id.btn_getimg);
        canny_dilate_btn = findViewById(R.id.btn_canny_dilate);
        contours_btn = findViewById(R.id.btn_contours);
        dilate_contours_btn = findViewById(R.id.btn_dilate_contours);
        dilate_contours_rectandle_btn = findViewById(R.id.btn_dilate_contours_rectandle);
        grey_btn.setOnClickListener(new btnListener());
        Binarization_btn.setOnClickListener(new btnListener());
        canny_btn.setOnClickListener(new btnListener());
        geting_btn.setOnClickListener(new btnListener());
        canny_dilate_btn.setOnClickListener(new btnListener());
        contours_btn.setOnClickListener(new btnListener());
        dilate_contours_btn.setOnClickListener(new btnListener());
        dilate_contours_rectandle_btn.setOnClickListener(new btnListener());

    }

    class btnListener implements View.OnClickListener {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_getimg:
                    temp_bitmap = Bitmap.createBitmap(input_bitmap);
                    shape1.setImageBitmap(temp_bitmap);
                    break;
                case R.id.btn_gray:
                    Mat pre_gray = Bitmap2Mat(temp_bitmap);

                    Mat after_grey = gray(pre_gray);

                    Bitmap grayed_bitmap = Mat2Bitmap(after_grey);
                    shape2.setImageBitmap(grayed_bitmap);
                    break;
                case R.id.btn_Binarization:
                    break;
                case R.id.btn_canny:
                    Mat pre_canny = Bitmap2Mat(temp_bitmap);

                    Mat after_canny = canny(pre_canny);

                    Bitmap return_bitmap = Mat2Bitmap(after_canny);
                    shape2.setImageBitmap(return_bitmap);
                    break;
                case R.id.btn_canny_dilate:
                    Mat pre_canny_dilate = Bitmap2Mat(temp_bitmap);

                    Mat after_canny_dilate = canny_dilate(pre_canny_dilate);

                    Bitmap canny_dilate_bitmap = Mat2Bitmap(after_canny_dilate);
                    shape2.setImageBitmap(canny_dilate_bitmap);
                    break;
                case R.id.btn_dilate_contours:
                    Mat pre_dilate_contours = Bitmap2Mat(temp_bitmap);

                    Mat after_dilate_contours = dilate_Contours(pre_dilate_contours);

                    Bitmap dilate_contours_bitmap = Mat2Bitmap(after_dilate_contours);
                    shape2.setImageBitmap(dilate_contours_bitmap);
                    break;
                case R.id.btn_contours:
                    Mat pre_contours = Bitmap2Mat(temp_bitmap);

                    Mat after_contours = Contours(pre_contours);

                    Bitmap contours_bitmap = Mat2Bitmap(after_contours);
                    shape2.setImageBitmap(contours_bitmap);
                    break;
                case R.id.btn_dilate_contours_rectandle:
                    Mat pre_dilate_contours_rectandle = Bitmap2Mat(temp_bitmap);

                    Mat after_dilate_contours_rectandle = dilate_Contours_rectandle(pre_dilate_contours_rectandle);

                    Bitmap dilate_contours_rectandle_bitmap = Mat2Bitmap(after_dilate_contours_rectandle);
                    shape2.setImageBitmap(dilate_contours_rectandle_bitmap);
                    break;

            }
        }
    }

    public Mat gray(Mat input_mat) {
        Mat pre_process_mat = input_mat;
        Mat processing_mat = new Mat();
        Toast.makeText(getApplication(), "进入灰化", Toast.LENGTH_SHORT).show();
        /*处理流程*/
        Imgproc.cvtColor(pre_process_mat, processing_mat, Imgproc.COLOR_BGR2GRAY);
//        processing_mat = getCannyMat(pre_process_mat);
        return processing_mat;
    }

    public Mat canny(Mat input_mat) {
        int th1 = Integer.parseInt(canny_th1.getText().toString());
        int th2 = Integer.parseInt(canny_th2.getText().toString());
//        Log.i("th1","  "+th1);
        Mat processing_mat = new Mat();
        Mat processed_mat = new Mat();
        Toast.makeText(getApplication(), "灰化->边缘检测", Toast.LENGTH_SHORT).show();
        Imgproc.cvtColor(input_mat, processing_mat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(processing_mat, processed_mat, th1, th2);
        return processed_mat;
    }

    public Mat canny_dilate(Mat input_mat) {
        int th1 = Integer.parseInt(canny_th1.getText().toString());
        int th2 = Integer.parseInt(canny_th2.getText().toString());
        Mat processing_mat = new Mat();
        Mat processed_mat = new Mat();
        Toast.makeText(getApplication(), "灰化->边缘检测->膨胀", Toast.LENGTH_SHORT).show();

        /*处理流程*/
        Imgproc.cvtColor(input_mat, processing_mat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(processing_mat, processing_mat, th1, th2);
        Imgproc.dilate(processing_mat, processed_mat, new Mat());

        return processed_mat;
    }

    private Mat sample_fun(Mat input_mat) {
        Mat pre_process_mat;
        Mat processing_mat = new Mat();
        Mat processed_mat = new Mat();
        Toast.makeText(getApplication(), "进入处理", Toast.LENGTH_SHORT).show();

        /*处理流程*/


        return processed_mat;
    }

    private Mat Contours(Mat input_mat){
        Mat processing_mat;
        Mat processed_mat = new Mat();
        Toast.makeText(getApplication(),"灰化->边缘检测->边缘",Toast.LENGTH_SHORT).show();

        processing_mat = canny(input_mat);

        double mMinContourArea = 0.1;       //最小轮廓区域
        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();        //ArrayList可以存放Object

//        Mat test = new Mat(input_mat.height(),input_mat.width(),CvType.CV_8UC3);
//        input_mat.convertTo(test,CvType.CV_8UC1);
//        Log.i("需要识别的Mat类型是：",""+test.type());
//        CvType.CV_8UC1

        Imgproc.findContours(processing_mat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        //学长Imgproc.CHAIN_APPROX_SIMPLE只取了拐点
        //hierarchy[i][后一个轮廓，前一个轮廓，父轮廓，内嵌轮廓]   的编号，没有相应内容的会被置-1,i与contours的编号对应。
        double maxAr =320*280*0.95;
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
        Log.i("maxArea:",""+maxArea);
        /*新建一个List列表，遍历得到大于0.1*最大面积且小于最大面积的集合mContours*/
        List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
        each = contours.iterator();
        while (each.hasNext()) {
            MatOfPoint contour = each.next();
            double area = Imgproc.contourArea(contour);
            if ( area > mMinContourArea*maxArea &&area <maxAr) {
                mContours.add(contour);
            }
        }
        Log.i("添加完点的列表到mContours","");
        processed_mat = new Mat(processing_mat.height(),processing_mat.width(),CvType.CV_8UC3);

        /*新建一个List列表，遍历得到大于0.1*最大面积且小于最大面积的集合mContours*/
        Imgproc.drawContours(processed_mat,mContours,-1,new Scalar( 255,0, 0),1);         //自己加的，画不出来；


//        MatOfPoint2f approxCurve = new MatOfPoint2f();
//        List<MatOfPoint> mContour2 = new ArrayList<MatOfPoint>();
//        each = mContours.iterator();
//        while (each.hasNext()) {
//            MatOfPoint contour = each.next();
//            MatOfPoint2f new_mat = new MatOfPoint2f( contour.toArray() );       //把轮廓的点转化为数组并以数组建立新的MatOfPoint对象new_mat
//            Imgproc.approxPolyDP(new_mat, approxCurve, 30, true);
//            //Imgproc.approxPolyDP(输入的轮廓点的点集，输出的多边形点集，输出精度——和另一个轮廓点的最大距离数，输出的多边形是否闭合)
//            long total  = approxCurve.total();      //边的数量？
//            if (total == 4 ) {
//                MatOfPoint contour2 = new MatOfPoint(approxCurve.toArray());       //把边的点集转化为MatOfPoint
//                mContour2.add(contour2);        //把点集的MatOfPoint加入列表mContour2
//            }
//        }

        return processed_mat;
    }

    private Mat dilate_Contours(Mat input_mat){
        Mat processing_mat;
        Mat processed_mat = new Mat();
        Toast.makeText(getApplication(),"灰化->边缘检测->膨胀->边缘",Toast.LENGTH_SHORT).show();

        processing_mat = canny_dilate(input_mat);

        double mMinContourArea = 0.1;       //最小轮廓区域
        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();        //ArrayList可以存放Object

//        Mat test = new Mat(input_mat.height(),input_mat.width(),CvType.CV_8UC3);
//        input_mat.convertTo(test,CvType.CV_8UC1);
//        Log.i("需要识别的Mat类型是：",""+test.type());
//        CvType.CV_8UC1

        Imgproc.findContours(processing_mat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        //学长Imgproc.CHAIN_APPROX_SIMPLE只取了拐点
        //hierarchy[i][后一个轮廓，前一个轮廓，父轮廓，内嵌轮廓]   的编号，没有相应内容的会被置-1,i与contours的编号对应。
        double maxAr =320*280*0.95;
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
        Log.i("maxArea:",""+maxArea);
        /*新建一个List列表，遍历得到大于0.1*最大面积且小于最大面积的集合mContours*/
        List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
        each = contours.iterator();
        while (each.hasNext()) {
            MatOfPoint contour = each.next();
            double area = Imgproc.contourArea(contour);
            if ( area > mMinContourArea*maxArea &&area <maxAr) {
                mContours.add(contour);
            }
        }
        Log.i("添加完点的列表到mContours", "数量是:"+mContours.size()+"  分别是"+mContours.toString());
        processed_mat = new Mat(processing_mat.height(),processing_mat.width(),CvType.CV_8UC3);

        /*新建一个List列表，遍历得到大于0.1*最大面积且小于最大面积的集合mContours*/
        Imgproc.drawContours(processed_mat,mContours,-1,new Scalar( 255,0, 0),1);         //自己加的，画不出来；


//        MatOfPoint2f approxCurve = new MatOfPoint2f();
//        List<MatOfPoint> mContour2 = new ArrayList<MatOfPoint>();
//        each = mContours.iterator();
//        while (each.hasNext()) {
//            MatOfPoint contour = each.next();
//            MatOfPoint2f new_mat = new MatOfPoint2f( contour.toArray() );       //把轮廓的点转化为数组并以数组建立新的MatOfPoint对象new_mat
//            Imgproc.approxPolyDP(new_mat, approxCurve, 30, true);
//            //Imgproc.approxPolyDP(输入的轮廓点的点集，输出的多边形点集，输出精度——和另一个轮廓点的最大距离数，输出的多边形是否闭合)
//            long total  = approxCurve.total();      //边的数量？
//            if (total == 4 ) {
//                MatOfPoint contour2 = new MatOfPoint(approxCurve.toArray());       //把边的点集转化为MatOfPoint
//                mContour2.add(contour2);        //把点集的MatOfPoint加入列表mContour2
//            }
//        }

        return processed_mat;
    }

    private Mat dilate_Contours_rectandle(Mat input_mat){
        Mat processing_mat;
        Mat processed_mat = new Mat();
        Toast.makeText(getApplication(),"灰化->边缘检测->膨胀->边缘",Toast.LENGTH_SHORT).show();

        processing_mat = canny_dilate(input_mat);

        double mMinContourArea = 0.1;       //最小轮廓区域
        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();        //ArrayList可以存放Object

        Imgproc.findContours(processing_mat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        //学长Imgproc.CHAIN_APPROX_SIMPLE只取了拐点
        //hierarchy[i][后一个轮廓，前一个轮廓，父轮廓，内嵌轮廓]   的编号，没有相应内容的会被置-1,i与contours的编号对应。
        double maxAr =320*280*0.95;
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
        Log.i("maxArea:",""+maxArea);
        /*新建一个List列表，遍历得到大于0.1*最大面积且小于最大面积的集合mContours*/
        List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
        each = contours.iterator();
        while (each.hasNext()) {
            MatOfPoint contour = each.next();
            double area = Imgproc.contourArea(contour);
            if ( area > mMinContourArea*maxArea &&area <maxAr) {
                mContours.add(contour);
            }
        }
        Log.i("添加完点的列表到mContours", "数量是:"+mContours.size()+"  分别是"+mContours.toString());
        processed_mat = new Mat(processing_mat.height(),processing_mat.width(),CvType.CV_8UC3);

        /*新建一个List列表，遍历得到大于0.1*最大面积且小于最大面积的集合mContours*/
        Imgproc.drawContours(processed_mat,mContours,-1,new Scalar( 255,0, 0),1);         //自己加的，画不出来；



/*        Iterator<MatOfPoint> each2 = mContour2.iterator();      //把轮廓的集进行迭代
        MatOfPoint mContour3 = null;
        while (each2.hasNext()) {
            MatOfPoint wrapper = each2.next();
            double area = Imgproc.contourArea(wrapper);     //计算轮廓面积
            List<Point> pointList = wrapper.toList();       //把轮廓转化为List
            double width =  Math.abs(pointList.get(2).x - pointList.get(0).x);
            double height =  Math.abs(pointList.get(2).y - pointList.get(0).y);
            float b1 = ((float) width/(float) height);

            if( b1 > 1 &&b1 < 2)
            {
                if (area < mixArea )
                {
                    mixArea = area;
                    mContour3 = wrapper;
                }
            }
        }
        return mContour3;*/
//        MatOfPoint2f approxCurve = new MatOfPoint2f();
//        List<MatOfPoint> mContour2 = new ArrayList<MatOfPoint>();
//        each = mContours.iterator();
//        while (each.hasNext()) {
//            MatOfPoint contour = each.next();
//            MatOfPoint2f new_mat = new MatOfPoint2f( contour.toArray() );       //把轮廓的点转化为数组并以数组建立新的MatOfPoint对象new_mat
//            Imgproc.approxPolyDP(new_mat, approxCurve, 30, true);
//            //Imgproc.approxPolyDP(输入的轮廓点的点集，输出的多边形点集，输出精度——和另一个轮廓点的最大距离数，输出的多边形是否闭合)
//            long total  = approxCurve.total();      //边的数量？
//            if (total == 4 ) {
//                MatOfPoint contour2 = new MatOfPoint(approxCurve.toArray());       //把边的点集转化为MatOfPoint
//                mContour2.add(contour2);        //把点集的MatOfPoint加入列表mContour2
//            }
//        }

        return processed_mat;
    }

/*    private Mat getCannyMat(Mat src)
    {
        double pointD = 1;
        double point2D =2;
        Mat grayMat = new Mat();
        Mat grayMat2 = new Mat();
        Mat cannyEdges = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        List<Point> pointList = null;
        MatOfPoint matPoint = new MatOfPoint(new Point(pointD,pointD),new Point(pointD,src.size().height-pointD),new Point(src.size().width-point2D,src.size().height-point2D),new Point(src.size().width-point2D,point2D));
        pointList = matPoint.toList();
        contours.add(matPoint);
        Imgproc.cvtColor(src, grayMat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(grayMat, cannyEdges, 100, 20);
        Imgproc.dilate(cannyEdges, grayMat2, new Mat());

        return cannyEdges;
    }*/

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
