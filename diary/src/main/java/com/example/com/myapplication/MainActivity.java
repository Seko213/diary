package com.example.com.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends Activity {

    DatePicker dP;
    EditText eD;
    Button bW;
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("석현 일기장");

        dP = (DatePicker) findViewById(R.id.dP);
        eD = (EditText) findViewById(R.id.eD);
        bW = (Button) findViewById(R.id.bW);

        Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);
        
        fileName = Integer.toString(cYear) + "-" + Integer.toString(cMonth+1) + "-" + Integer.toString(cDay);
        String str = readDiary(fileName);
        eD.setText(str);
        bW.setEnabled(true);

        dP.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                fileName = Integer.toString(year) + "_" + Integer.toString(monthOfYear +1) + "_" + Integer.toString(dayOfMonth) + ".txt";
                String str = readDiary(fileName);
                eD.setText(str);
                bW.setEnabled(true);
            }
        });

        bW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream outFs = openFileOutput(fileName, Context.MODE_WORLD_WRITEABLE);
                    String str = eD.getText().toString();
                    outFs.write(str.getBytes());
                    outFs.close();
                    Toast.makeText(getApplicationContext(), fileName + " 이 저장됨", Toast.LENGTH_SHORT).show();
                }
                catch (IOException e) {
                }
            }
        });
    }

    String readDiary(String fName) {
        String diaryStr = null;
        FileInputStream inFs;

        try {
            inFs = openFileInput(fName);
            byte[] txt = new byte[500];
            inFs.read(txt);
            inFs.close();
            diaryStr = (new String(txt)).trim();
            bW.setText("수정하기");
        }
        catch (IOException e) {
            eD.setHint("일기 없음");
            bW.setText("새로 저장");
        }
        return diaryStr;
    }
}
