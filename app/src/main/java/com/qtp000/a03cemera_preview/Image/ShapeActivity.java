package com.qtp000.a03cemera_preview.Image;

import android.content.Intent;
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
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class ShapeActivity extends AppCompatActivity {
    public static Bitmap input_bitmap,temp_bitmap;
    Button grey_btn,Binarization_btn,canny_btn,geting_btn,canny_dilate_btn;
    ImageView shape1,shape2;
    TextView canny_th1,canny_th2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shape_use);
        Init();
    }

    private void Init(){
        canny_th1 = findViewById(R.id.canny_th1);
        canny_th2 = findViewById(R.id.canny_th2);
        shape1 = findViewById(R.id.shape1);
        shape2 = findViewById(R.id.shape2);
        grey_btn = findViewById(R.id.btn_grey);
        Binarization_btn = findViewById(R.id.btn_Binarization);
        canny_btn = findViewById(R.id.btn_canny);
        geting_btn = findViewById(R.id.btn_getimg);
        canny_dilate_btn = findViewById(R.id.btn_canny_dilate);
        grey_btn.setOnClickListener(new btnListener());
        Binarization_btn.setOnClickListener(new btnListener());
        canny_btn.setOnClickListener(new btnListener());
        geting_btn.setOnClickListener(new btnListener());
        canny_dilate_btn.setOnClickListener(new btnListener());

    }

    class btnListener implements View.OnClickListener{
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_grey:
                    Bitmap after_grey = gray(temp_bitmap);
                    shape2.setImageBitmap(after_grey);
                    break;
                case R.id.btn_Binarization:
                    break;
                case R.id.btn_canny:
                    Bitmap after_canny = canny(temp_bitmap);
                    shape2.setImageBitmap(after_canny);
                    break;
                case R.id.btn_getimg:
                    temp_bitmap = Bitmap.createBitmap(input_bitmap);
                    shape1.setImageBitmap(temp_bitmap);
                    break;
                case R.id.btn_canny_dilate:
                    Bitmap after_canny_dilate = canny_dilate(temp_bitmap);
                    shape2.setImageBitmap(after_canny_dilate);
                    break;
            }
        }
    }

    public Bitmap gray(Bitmap input){
        Bitmap return_bitmap;
        Mat processing_mat = new Mat();
        Mat pre_process_mat = Bitmap2Mat(input);
        Toast.makeText(getApplication(),"进入灰化",Toast.LENGTH_SHORT).show();
        /*处理流程*/
        Imgproc.cvtColor(pre_process_mat, processing_mat, Imgproc.COLOR_BGR2GRAY);
//        processing_mat = getCannyMat(pre_process_mat);
        return_bitmap = Mat2Bitmap(processing_mat);
        return return_bitmap;
    }
    public Bitmap canny(Bitmap input){
        int th1 = Integer.parseInt(canny_th1.getText().toString());
        int th2 = Integer.parseInt(canny_th2.getText().toString());
//        Log.i("th1","  "+th1);
        Bitmap return_bitmap;
        Mat processing_mat = new Mat();
        Mat processed_mat = new Mat();
        Mat pre_process_mat = Bitmap2Mat(input);
        Toast.makeText(getApplication(),"灰化->边缘检测",Toast.LENGTH_SHORT).show();
        Imgproc.cvtColor(pre_process_mat,processing_mat,Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(processing_mat,processed_mat,th1,th2);
        return_bitmap = Mat2Bitmap(processed_mat);
        return return_bitmap;
    }
    public Bitmap canny_dilate(Bitmap input){
        int th1 = Integer.parseInt(canny_th1.getText().toString());
        int th2 = Integer.parseInt(canny_th2.getText().toString());
        Bitmap return_bitmap;
        Mat pre_process_mat = Bitmap2Mat(input);
        Mat processing_mat = new Mat();
        Mat processed_mat = new Mat();
        Toast.makeText(getApplication(),"灰化->边缘检测->膨胀",Toast.LENGTH_SHORT).show();

        /*处理流程*/
        Imgproc.cvtColor(pre_process_mat,processing_mat,Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(processing_mat,processing_mat,th1,th2);
        Imgproc.dilate(processing_mat,processed_mat,new Mat());

        return_bitmap = Mat2Bitmap(processed_mat);
        return return_bitmap;
    }

    private Bitmap sample_fun(Bitmap input){
        Bitmap return_bitmap;
        Mat pre_process_mat = Bitmap2Mat(input);
        Mat processing_mat = new Mat();
        Mat processed_mat = new Mat();
        Toast.makeText(getApplication(),"进入处理",Toast.LENGTH_SHORT).show();

        /*处理流程*/

        return_bitmap = Mat2Bitmap(processed_mat);
        return return_bitmap;
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

    private Mat Bitmap2Mat(Bitmap in_bitmap){
        int width = in_bitmap.getWidth();
        int height = in_bitmap.getHeight();
        Mat return_mat = new Mat(height,width, CvType.CV_8UC3);
        Utils.bitmapToMat(in_bitmap,return_mat);
        return return_mat;
    }
    private Bitmap Mat2Bitmap(Mat in_mat){
        int width = in_mat.width();
        int height = in_mat.height();
        Bitmap return_bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(in_mat,return_bitmap);
        return return_bitmap;

    }
}
