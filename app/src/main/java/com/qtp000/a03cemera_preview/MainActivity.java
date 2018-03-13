package com.qtp000.a03cemera_preview;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView imageView_test,imageView_test2;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("opencv_java");
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_view();

    }


    private void init_view(){
        imageView_test = findViewById(R.id.test_imageView);
        imageView_test2 = findViewById(R.id.test_imageView2);
        Button btn_copy = findViewById(R.id.btn_getbitmap);
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap temp;
                temp = getImageview_test(imageView_test);
                setImageview_test(temp,imageView_test2);

            }
        });


        imageView_test.setImageResource(R.drawable.test);

    }

    private Bitmap getImageview_test(ImageView imageview){
        Bitmap getted_image;
        imageview.setDrawingCacheEnabled(true);    //开启缓存生成Cache
        getted_image = Bitmap.createBitmap(imageview.getDrawingCache());    //从缓存Cache中获得bitmap
        imageview.setDrawingCacheEnabled(false);       //关闭缓存，减少内存占用
        return getted_image;
    }
    private void setImageview_test(Bitmap input,ImageView imageview){
        imageview.setImageBitmap(input);

    }

}
