package com.example.it2.test2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

import static com.example.it2.test2.R.id.editText;

public class MainActivity extends Activity {

    ShowBarCode SHBC=new ShowBarCode();
    Bitmap bitmap=null;
    SharedPreferences sPref;
    String CardCode="BonusNumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        // barcode image

        final ImageView iv=(ImageView) findViewById(R.id.imageView2);
        final EditText tv =(EditText) findViewById(editText);
        String barcode_data=loadPref();


        tv.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                        (i == KeyEvent.KEYCODE_ENTER))
                {
                    // сохраняем текст, введенный до нажатия Enter в переменную
                    String strCatName = tv.getText().toString();
                    try {

                        bitmap = SHBC.encodeAsBitmap(strCatName,BarcodeFormat.CODE_128,600,300);
                        iv.setImageBitmap(bitmap);


                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                    savePerf(strCatName);



                    return true;
                }
                return false;
            }
        });




        try {

            bitmap = SHBC.encodeAsBitmap(barcode_data,BarcodeFormat.CODE_128,600,300);
            iv.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }



        //barcode text

        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setText(barcode_data);



    }


    String loadPref()
    {
        sPref=getPreferences(MODE_PRIVATE); // модификатор
        String barcode_data = sPref.getString(CardCode,""); // получить поле, по значению
        return barcode_data;
    }


    void savePerf(String strCatName)
    {
        sPref=getPreferences(MODE_PRIVATE); // модификатор
        SharedPreferences.Editor editor=sPref.edit(); //
        editor.putString(CardCode,strCatName); // Изменить пол и значение
        editor.commit(); // записать
    }






}

