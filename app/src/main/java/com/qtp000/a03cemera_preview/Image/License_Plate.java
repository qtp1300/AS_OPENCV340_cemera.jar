package com.qtp000.a03cemera_preview.Image;

import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.os.Environment;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;

public class License_Plate {
    public String license_plate_string;
    public void get_license_plate(Bitmap input_bitmap,String language){
        TessBaseAPI tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.init(getSDPath(),language);
        tessBaseAPI.setImage(input_bitmap);
        String return_text = tessBaseAPI.getUTF8Text();
        license_plate_string = return_text;
        tessBaseAPI.clear();
        tessBaseAPI.end();
    }

    public static String getSDPath()
    {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // �ж�sd���Ƿ����
        if (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();// ��ȡ���Ŀ¼
        }
        return sdDir.toString();
    }
}
