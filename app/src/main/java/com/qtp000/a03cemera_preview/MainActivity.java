package com.qtp000.a03cemera_preview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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

        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());


        Button btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new btnListener());

    }

    class btnListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
//            int id = v.getId();
            Toast toast = Toast.makeText(getApplicationContext(),"点击了",Toast.LENGTH_SHORT);
            toast.show();

            Intent intent = new Intent(MainActivity.this , test.class);
            startActivity(intent);


        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();



}
