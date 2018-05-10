package com.qtp000.a03cemera_preview.Image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.qtp000.a03cemera_preview.R;

public class ShapeActivity extends AppCompatActivity {
    public static Bitmap input_bitmap;
    Button grey_btn,Binarization_btn,canny_btn,geting_btn;
    ImageView shape1,shape2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shape_use);
        Init();
    }

    private void Init(){
        shape1 = findViewById(R.id.shape1);
        shape2 = findViewById(R.id.shape2);
        grey_btn = findViewById(R.id.btn_grey);
        Binarization_btn = findViewById(R.id.btn_Binarization);
        canny_btn = findViewById(R.id.btn_canny);
        geting_btn = findViewById(R.id.btn_getimg);
        grey_btn.setOnClickListener(new btnListener());
        Binarization_btn.setOnClickListener(new btnListener());
        canny_btn.setOnClickListener(new btnListener());
        geting_btn.setOnClickListener(new btnListener());

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
                    break;
                case R.id.btn_Binarization:
                    break;
                case R.id.btn_canny:
                    break;
                case R.id.btn_getimg:
                    shape1.setImageBitmap(input_bitmap);
                    break;
            }
        }
    }

    public void gray(Bitmap input){

    }
}
