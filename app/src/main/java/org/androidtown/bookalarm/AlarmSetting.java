package org.androidtown.bookalarm;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class AlarmSetting extends AppCompatActivity{
    private static int ONE_MINUTE = 5626;
    String viCheck,soCheck;

    private CheckBox main = null;
    private CheckBox vi = null;
    private CheckBox so = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);
        vi = (CheckBox) findViewById(R.id.checkBox1);
        so = (CheckBox) findViewById(R.id.checkBox2);
        main = (CheckBox) findViewById(R.id.checkBox3);
        final Button button = (Button) findViewById(R.id.button);
        setTitle("알림 설정");
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        viCheck =  pref.getString("viCheck","");
        soCheck =  pref.getString("soCheck","");
        if(viCheck.equals("false"))
        {
            vi.setChecked(false);
        }else
        {
            vi.setChecked(true);
        }

        if(soCheck.equals("false"))
        {
            so.setChecked(false);
        }else
        {
            so.setChecked(true);
        }
        if(soCheck.equals("false")&&viCheck.equals("false"))
        {
            main.setChecked(false);
            so.setChecked(false);
            vi.setChecked(false);
            vi.setEnabled(false);
            so.setEnabled(false);
        }





        main.setOnClickListener(new CheckBox.OnClickListener() {
            @Override

            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                if(main.isChecked()==true) {
                    vi.setChecked(true);
                    so.setChecked(true);
                    vi.setEnabled(true);
                    so.setEnabled(true);
                }else
                {
                    vi.setChecked(false);
                    so.setChecked(false);
                    vi.setEnabled(false);
                    so.setEnabled(false);
                }
            }
        }) ;
        so.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(so.isChecked()==false && vi.isChecked()==false) {
                    main.setChecked(false);
                    vi.setEnabled(false);
                    so.setEnabled(false);
                }
            }
        }) ;

        vi.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(so.isChecked()==false && vi.isChecked()==false) {
                    main.setChecked(false);
                    vi.setEnabled(false);
                    so.setEnabled(false);
                }
            }
        }) ;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                if(vi.isChecked())
                    viCheck = "true";
                else
                    viCheck="false";
                if(so.isChecked())
                    soCheck = "true";
                else
                    soCheck="false";
                editor.putString("viCheck",viCheck);
                editor.putString("soCheck",soCheck);
                editor.commit();
                //   Toast.makeText(AlarmSetting.this,soCheck+viCheck+"테스트", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, 102);
            }
        });
    }
    //뒤로가기
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(intent, 102);
    }
}
