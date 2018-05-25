package com.qtp000.a03cemera_preview.Image;

import android.graphics.Bitmap;
import android.graphics.Color;
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

public class ShapeActivity extends AppCompatActivity {
    Get_Contours get_contours = new Get_Contours();
    Get_Shape get_shape = new Get_Shape();
    public static Bitmap input_bitmap, temp_bitmap;
    Button grey_btn, Binarization_btn, canny_btn, geting_btn, canny_dilate_btn,
            contours_btn, dilate_contours_btn, dilate_contours_rectandle_btn,
            full_screen_btn, canny_equalizeHise_btn,
            all_shape_contours_btn, grey_btn2, canny_btn2,
            license_plate_btn;
    ImageView shape1, shape2, shape3;
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
        shape3 = findViewById(R.id.shape3);
        grey_btn = findViewById(R.id.btn_gray);
        Binarization_btn = findViewById(R.id.btn_Binarization);
        canny_btn = findViewById(R.id.btn_canny);
        canny_btn2 = findViewById(R.id.btn_canny2);
        grey_btn2 = findViewById(R.id.btn_gray2);
        geting_btn = findViewById(R.id.btn_getimg);
        canny_dilate_btn = findViewById(R.id.btn_canny_dilate);
        contours_btn = findViewById(R.id.btn_contours);
        dilate_contours_btn = findViewById(R.id.btn_dilate_contours);
        dilate_contours_rectandle_btn = findViewById(R.id.btn_dilate_contours_rectandle);
        full_screen_btn = findViewById(R.id.btn_full_screen);
        all_shape_contours_btn = findViewById(R.id.btn_get_allshape_contours);
        canny_equalizeHise_btn = findViewById(R.id.btn_canny_equalizeHise);
        license_plate_btn = findViewById(R.id.btn_license_plate);

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
        canny_equalizeHise_btn.setOnClickListener(new btnListener());
        license_plate_btn.setOnClickListener(new btnListener());

        canny_btn2.setOnClickListener(new btnListener());
        grey_btn2.setOnClickListener(new btnListener());

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

                    shape3.setDrawingCacheEnabled(true);
                    Bitmap getted_CheckImageBack = Bitmap.createBitmap(shape3.getDrawingCache());
                    shape3.setDrawingCacheEnabled(false);
                    Mat pre_CheckImageBack = Bitmap2Mat(getted_CheckImageBack);

//                    Mat pre_CheckImageBack = Bitmap2Mat(temp_bitmap);
                    int coooolor;
//                    Mat after_grey = get_contours.gray(pre_gray);

                    coooolor = get_shape.CheckImageBack(pre_CheckImageBack);
                    Log.i("颜色", "" + coooolor);
                    Toast.makeText(getApplication(),"颜色" + coooolor,Toast.LENGTH_SHORT).show();
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
                    Log.i("拿到的图形分辨率", pre_full_screen.size().toString());

                    Mat after_full_screen = get_contours.Contours_rectandle_get_point_FullScreen(pre_full_screen);

                    Bitmap full_screen_bitmap = Mat2Bitmap(after_full_screen);
                    shape2.setImageBitmap(full_screen_bitmap);
                    shape3.setImageBitmap(full_screen_bitmap);
                    break;
                case R.id.btn_get_allshape_contours:
                    Mat pre_get_allshape_contours = Bitmap2Mat(temp_bitmap);

                    Mat after_get_allshape_contours = get_shape.get_all_shape_contours(pre_get_allshape_contours);

                    Bitmap get_allshape_contours_bitmap = Mat2Bitmap(after_get_allshape_contours);
                    shape2.setImageBitmap(get_allshape_contours_bitmap);
                    break;
                case R.id.btn_canny2:
                    shape3.setDrawingCacheEnabled(true);
                    Bitmap getted_shape3 = Bitmap.createBitmap(shape3.getDrawingCache());
                    shape3.setDrawingCacheEnabled(false);
                    Mat pre_canny2 = Bitmap2Mat(getted_shape3);

                    Mat after_canny2 = get_shape.canny_dilate(pre_canny2);

                    Bitmap return_bitmap2 = Mat2Bitmap(after_canny2);
                    shape2.setImageBitmap(return_bitmap2);
                    break;
                case R.id.btn_gray2:
                    shape3.setDrawingCacheEnabled(true);
                    Bitmap getted_shape3_2 = Bitmap.createBitmap(shape3.getDrawingCache());
                    shape3.setDrawingCacheEnabled(false);
                    Mat pre_gray2 = Bitmap2Mat(getted_shape3_2);

                    Mat after_grey2 = get_contours.gray(pre_gray2);

                    Bitmap grayed_bitmap2 = Mat2Bitmap(after_grey2);
                    shape2.setImageBitmap(grayed_bitmap2);
                    break;
                case R.id.btn_canny_equalizeHise:
                    shape3.setDrawingCacheEnabled(true);
                    Bitmap getted_shape3_3 = Bitmap.createBitmap(shape3.getDrawingCache());
                    shape3.setDrawingCacheEnabled(false);
                    Mat pre_canny_equalizeHise = Bitmap2Mat(getted_shape3_3);

                    Mat after_canny_equalizeHise = get_shape.canny_equalizeHist(pre_canny_equalizeHise);

                    Bitmap canny_equalizeHise_bitmap = Mat2Bitmap(after_canny_equalizeHise);
                    shape2.setImageBitmap(canny_equalizeHise_bitmap);
                    break;
                case R.id.btn_license_plate:
                    shape3.setDrawingCacheEnabled(true);
                    Bitmap getted_license_plate = Bitmap.createBitmap(shape3.getDrawingCache());
                    shape3.setDrawingCacheEnabled(false);

//                    Mat pre_license_plate = Bitmap2Mat(getted_shape3_4);

                    License_Plate license_plate = new License_Plate();
                    license_plate.get_license_plate(getted_license_plate, "chi_sim");
                    Log.i("车牌", license_plate.license_plate_string);

//                    shape2.setImageBitmap(license_plate_bitmap);


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
