package com.example.it2.test2;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.example.it2.test2.barcode.BarcodeCaptureActivity;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import static com.example.it2.test2.R.id.editText;

public class MainActivity extends Activity {

    ShowBarCode SHBC=new ShowBarCode();
    Bitmap bitmap=null;
    EditText tv;
    SharedPreferences sPref;
    String CardCode="BonusNumber";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int BARCODE_READER_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button scanBarcodeButton = (Button) findViewById(R.id.scanButton);
        scanBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
            }
        });


        // barcode image

        final ImageView iv=(ImageView) findViewById(R.id.imageView2);
        final EditText tv =(EditText) findViewById(editText);
        String barcode_data=loadPref(); // загружаем номер карты из файла


        tv.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                        (i == KeyEvent.KEYCODE_ENTER))
                {
                    // сохраняем текст, введенный до нажатия Enter в переменную
                    String strCatName = tv.getText().toString();
                    try {

                        bitmap = SHBC.encodeAsBitmap(strCatName,BarcodeFormat.CODE_128,600,300);//код ,формат,размеры штрих кода
                        iv.setImageBitmap(bitmap);// отображаим штрихкод



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

            bitmap = SHBC.encodeAsBitmap(barcode_data,BarcodeFormat.CODE_128,600,300); //код ,формат,размеры штрих кода
            iv.setImageBitmap(bitmap); // отображаем штрихкод

        } catch (WriterException e) {
            e.printStackTrace();
        }



        //barcode text

        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setText(barcode_data);



    }


    String loadPref()
    {
        sPref=getSharedPreferences("Test2",MODE_PRIVATE); // модификатор
        String barcode_data = sPref.getString(CardCode,""); // получить поле, по значению
        return barcode_data;
    }


    void savePerf(String strCatName)
    {
        sPref=getSharedPreferences("Test2",MODE_PRIVATE); // модификатор-Константа MODE_PRIVATE используется для настройки доступа и означает, что после сохранения, данные будут видны только этому приложению.
        SharedPreferences.Editor editor=sPref.edit(); //чтобы редактировать данные, необходим объект Editor – получаем его из sPref
        editor.putString(CardCode,strCatName); // Изменить пол и значение
        editor.commit(); // записать
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] p = barcode.cornerPoints;
                    tv.setText(barcode.displayValue);
                } else tv.setText(R.string.no_barcode_captured);
            } else Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode)));
        } else super.onActivityResult(requestCode, resultCode, data);
    }
}








