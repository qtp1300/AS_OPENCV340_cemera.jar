package com.qtp000.a03cemera_preview.Init_page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.qtp000.a03cemera_preview.MainActivity_two;
import com.qtp000.a03cemera_preview.R;
import com.qtp000.a03cemera_preview.Serial.SerialAcyivity_two;
import com.qtp000.a03cemera_preview.ValuesApplication;

/**
 * Created by 祁天培 on 2018/5/8.
 */

public class Init_page_activity_one extends AppCompatActivity {
    private Button wire_btn,wireless_btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zero_init_select_page);
        init_btn();
    }
    private void init_btn(){
        wire_btn = findViewById(R.id.btn_wire);
        wireless_btn = findViewById(R.id.btn_wireless);
        wire_btn.setOnClickListener(new btnListener());
        wireless_btn.setOnClickListener(new btnListener());
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
                case R.id.btn_wire:
                    ValuesApplication.isserial = true;
                    Toast.makeText(Init_page_activity_one.this,"有线",Toast.LENGTH_SHORT).show();
//                    Intent toSerial = new Intent();
//                    toSerial.setClass(Init_page_activity_one.this, SerialAcyivity_two.class);
                    Intent toSerial = new Intent(Init_page_activity_one.this,SerialAcyivity_two.class);
                    startActivity(toSerial);
                    break;
                case R.id.btn_wireless:
                    ValuesApplication.isserial = false;
                    Toast.makeText(Init_page_activity_one.this,"无线",Toast.LENGTH_SHORT).show();
                    Intent toMainactivity = new Intent();
                    toMainactivity.setClass(Init_page_activity_one.this, MainActivity_two.class);
                    startActivity(toMainactivity);
                    break;
            }

        }
    }

}
//class bbb implements View.OnClickListener{
//    /**
//     * Called when a view has been clicked.
//     *
//     * @param v The view that was clicked.
//     */
//    @Override
//    public void onClick(View v) {
//        Log.i("bbb:","收到");
//    }
//}

