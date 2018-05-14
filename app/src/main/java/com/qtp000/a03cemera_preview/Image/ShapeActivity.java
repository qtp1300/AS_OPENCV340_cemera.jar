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
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShapeActivity extends AppCompatActivity {
    Get_Contours get_contours = new Get_Contours();
    public static Bitmap input_bitmap, temp_bitmap;
    Button grey_btn, Binarization_btn, canny_btn, geting_btn, canny_dilate_btn,
            contours_btn, dilate_contours_btn, dilate_contours_rectandle_btn,
            full_screen_btn,
    all_shape_contours_btn;
    ImageView shape1, shape2;
    public static TextView canny_th1, canny_th2;

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
        full_screen_btn = findViewById(R.id.btn_full_screen);
        all_shape_contours_btn = findViewById(R.id.btn_get_allshape_contours);
        grey_btn.setOnClickListener(new btnListener());
        Binarization_btn.setOnClickListener(new btnListener());
        canny_btn.setOnClickListener(new btnListener());
        geting_btn.setOnClickListener(new btnListener());
        canny_dilate_btn.setOnClickListener(new btnListener());
        contours_btn.setOnClickListener(new btnListener());
        dilate_contours_btn.setOnClickListener(new btnListener());
        dilate_contours_rectandle_btn.setOnClickListener(new btnListener());
        full_screen_btn.setOnClickListener(new btnListener());
        all_shape_contours_btn.setOnClickListener(new btnListener());

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

                    Mat after_grey = get_contours.gray(pre_gray);

                    Bitmap grayed_bitmap = Mat2Bitmap(after_grey);
                    shape2.setImageBitmap(grayed_bitmap);
                    break;
                case R.id.btn_Binarization:
                    break;
                case R.id.btn_canny:
                    Mat pre_canny = Bitmap2Mat(temp_bitmap);

                    Mat after_canny = get_contours.canny(pre_canny);

                    Bitmap return_bitmap = Mat2Bitmap(after_canny);
                    shape2.setImageBitmap(return_bitmap);
                    break;
                case R.id.btn_canny_dilate:
                    Mat pre_canny_dilate = Bitmap2Mat(temp_bitmap);

                    Mat after_canny_dilate = get_contours.canny_dilate(pre_canny_dilate);

                    Bitmap canny_dilate_bitmap = Mat2Bitmap(after_canny_dilate);
                    shape2.setImageBitmap(canny_dilate_bitmap);
                    break;
                case R.id.btn_dilate_contours:
                    Mat pre_dilate_contours = Bitmap2Mat(temp_bitmap);

                    Mat after_dilate_contours = get_contours.dilate_Contours(pre_dilate_contours);

                    Bitmap dilate_contours_bitmap = Mat2Bitmap(after_dilate_contours);
                    shape2.setImageBitmap(dilate_contours_bitmap);
                    break;
                case R.id.btn_contours:
                    Mat pre_contours = Bitmap2Mat(temp_bitmap);

                    Mat after_contours = get_contours.Contours(pre_contours);

                    Bitmap contours_bitmap = Mat2Bitmap(after_contours);
                    shape2.setImageBitmap(contours_bitmap);
                    break;
                case R.id.btn_dilate_contours_rectandle:
                    Mat pre_dilate_contours_rectandle = Bitmap2Mat(temp_bitmap);

                    Mat after_dilate_contours_rectandle = get_contours.dilate_Contours_rectandle(pre_dilate_contours_rectandle);

                    Bitmap dilate_contours_rectandle_bitmap = Mat2Bitmap(after_dilate_contours_rectandle);
                    shape2.setImageBitmap(dilate_contours_rectandle_bitmap);
                    break;
                case R.id.btn_full_screen:
                    Mat pre_full_screen = Bitmap2Mat(temp_bitmap);

                    Mat after_full_screen = get_contours.Contours_rectandle_get_point_FullScreen(pre_full_screen);

                    Bitmap full_screen_bitmap = Mat2Bitmap(after_full_screen);
                    shape2.setImageBitmap(full_screen_bitmap);
                    break;
                case R.id.btn_get_allshape_contours:
                    Mat pre_get_allshape_contours = Bitmap2Mat(temp_bitmap);

                    Mat after_get_allshape_contours = get_contours.get_all_shape_contours(pre_get_allshape_contours);

                    Bitmap get_allshape_contours_bitmap = Mat2Bitmap(after_get_allshape_contours);
                    shape2.setImageBitmap(get_allshape_contours_bitmap);
                    break;

            }
        }
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
