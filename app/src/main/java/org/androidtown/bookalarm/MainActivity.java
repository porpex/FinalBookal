package org.androidtown.bookalarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.icu.lang.UCharacterEnums;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.TextView.OnEditorActionListener;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class    MainActivity extends AppCompatActivity {

    private String[] navItems = {"관심있는 작가 목록", "알람 내역", "알람 설정", "로그아웃",
            };
    private ListView lvNavList;
    private FrameLayout flContainer;

    EditText editText;

    String str2;
    TextView textview;
    SharedPreferences setting;
    Toolbar toolbar;
    DrawerLayout dlDrawer;
    ActionBarDrawerToggle dtToggle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);

        dtToggle = new ActionBarDrawerToggle(this, dlDrawer, R.string.app_name, R.string.app_name);
        dlDrawer.setDrawerListener(dtToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("새책냄새");

        lvNavList = (ListView)findViewById(R.id.lv_activity_main_nav_list);
        flContainer = (FrameLayout)findViewById(R.id.fl_activity_main_container);

        lvNavList.setAdapter(
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
        lvNavList.setOnItemClickListener(new DrawerItemClickListener());

        final SharedPreferences.Editor editor;
        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();

        Intent intent = getIntent();
        final String userID = LoginActivity.AlluserID;



        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.spList, android.R.layout.simple_spinner_item);


        editText = (EditText) findViewById(R.id.editText);
        Button button = (Button) findViewById(R.id.button);
        final RadioButton RadioTitle = (RadioButton) findViewById(R.id.radioButton);
        final RadioButton RadioAuthor = (RadioButton) findViewById(R.id.radioButton2);

        Button itemNewAll_button = (Button) findViewById(R.id.itemNewAll_button);
        Button itemNewSpecial_button = (Button) findViewById(R.id.itemNewSpecial_button);
        Button bestseller_button = (Button) findViewById(R.id.bestseller_button);
        Button blogBest_button = (Button) findViewById(R.id.blogBest_button);
        ImageButton im_Best_button = (ImageButton)findViewById(R.id.im_Best_Button);
        ImageButton im_blog_Button = (ImageButton)findViewById(R.id.im_blog_Button);
        ImageButton im_New_Button = (ImageButton)findViewById(R.id.im_New_Button);
        ImageButton im_eye_Button = (ImageButton)findViewById(R.id.im_eye_Button);
        editText.requestFocus();

        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);


        // textview.setText(userID);

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String content = "setTitle";
                    String setTitle ;
                    String searchData = editText.getText().toString();
                    if( !(searchData.replace(" ", "").equals(""))){
                        Intent intent = new Intent(MainActivity.this, AlarmSearch.class);


                         if (RadioTitle.isChecked())
                        {
                            setTitle ="title";
                        }
                        else
                      {
                          setTitle ="au";
                     }
                        intent.putExtra("setTitle", setTitle);
                        intent.putExtra("setData", content);
                        startActivityForResult(intent, 101);

                    }
                    else
                        Toast.makeText(MainActivity.this , "검색할 글자를 입력하세요", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        editText.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        String searchData = editText.getText().toString();
                        if( !(searchData.replace(" ", "").equals(""))) {
                            String setTitle;
                            if (RadioTitle.isChecked()) {
                                setTitle = "title";
                            } else {
                                setTitle = "au";
                            }
                            Intent intent = new Intent(MainActivity.this, AlarmSearch.class);
                            intent.putExtra("setData", searchData);
                            intent.putExtra("setTitle", setTitle);
                            startActivityForResult(intent, 101);
                            break;
                        }
                        else
                            Toast.makeText(MainActivity.this , "검색할 글자를 입력하세요", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        itemNewAll_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new newBookListTask().execute();

            }
        });

        itemNewSpecial_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ItemNewSpecial.class);
                startActivityForResult(intent,120);

            }
        });

        bestseller_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ItemBestseller.class);
                startActivityForResult(intent,130);

            }
        });

        blogBest_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ItemBlogBest.class);
                startActivityForResult(intent,140);

            }
        });
        im_Best_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ItemBestseller.class);
                startActivityForResult(intent,130);

            }
        });
        im_blog_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ItemBlogBest.class);
                startActivityForResult(intent,140);

            }
        });
        im_New_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new newBookListTask().execute();

            }
        });
        im_eye_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ItemNewSpecial.class);
                startActivityForResult(intent,120);

            }
        });

    }
////////// 여기부터 메뉴



    long pressTime;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override // 뒤 버튼 누르면 메뉴 닫기
    public void onBackPressed() {

        if (dlDrawer.isDrawerOpen(lvNavList)) {
            dlDrawer.closeDrawer(lvNavList);
        } else {
            long currentTime = System.currentTimeMillis();
            long intervalTime = currentTime - pressTime;
            if (intervalTime < 2000) {
                super.onBackPressed();
                finishAffinity();
            } else {
                pressTime = currentTime;
                Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        dtToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dtToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (dtToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position,
                                long id) {

            final SharedPreferences.Editor editor;
            setting = getSharedPreferences("setting", 0);
            editor = setting.edit();
            final String userID = LoginActivity.AlluserID;

            switch(position){
                case 0:
                    new StateListTask().execute(userID);
                    break;
                case 1:
                    new AlarmListTask().execute(userID);
                    break;
                case 2:
                    Intent intentAlarmSetting = new Intent(getApplicationContext(), AlarmSetting.class);
                    startActivityForResult(intentAlarmSetting,102); //알람 설정
                    break;
                case 3:
                            Toast.makeText(getApplicationContext(),"Service 끝",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this,MyService.class);
                            stopService(intent);
                    Intent intentLogOut = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(intentLogOut,102);
                    editor.clear();
                    editor.commit(); //로그아웃
                    break;

            }
            dlDrawer.closeDrawer(lvNavList); // 추가됨
        }

    }

    //////////////////////////// 메뉴의끝
  public class StateListTask extends AsyncTask<String,Void,String> // 등록현황 조회
    {
        StringBuilder jsonHtml;
        String target;
        int resCode;
        String jsondata;
        @Override
        protected String doInBackground(String... progress) {
            try{
                target = "http://bk66.cafe24.com/data_List2.php?id="+progress[0];
                URL url = new URL(target);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                if ( conn != null ) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);


                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        jsondata = br.readLine();
                        while (true) {
                            String temp = br.readLine();
                            if (temp == null)
                                break;
                           // jsonHtml.append(temp + "\n");
                            jsondata = jsondata + temp + "\n" ; //jsondata에 한 줄 씩 읽어서 저장
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
                //return  stringBuilder.toString().trim() ;
            }catch (Exception e){
                Log.e("SampleHTTP", "Exception in processing response.", e);
                e.printStackTrace();
            }
            return  jsondata.trim();
        }
        @Override
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }
        @Override
        public void onPostExecute(String result){
            Intent intent = new Intent(MainActivity.this,AlarmState.class);
            intent.putExtra("dataList",result);
            MainActivity.this.startActivity(intent);
            //textview.setText(result);
        }
    }

    public class AlarmListTask extends AsyncTask<String,Void,String> // 알람내역 조회
    {
        StringBuilder jsonHtml;
        String target;
        int resCode;
        String jsondata;
        @Override
        protected String doInBackground(String... progress) {
            try{
                target = "http://bk66.cafe24.com/alarm_List.php?id="+progress[0];
                URL url = new URL(target);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                if ( conn != null ) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);


                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        jsondata = br.readLine();
                        while (true) {
                            String temp = br.readLine();
                            if (temp == null)
                                break;
                            // jsonHtml.append(temp + "\n");
                            jsondata = jsondata + temp + "\n" ; //jsondata에 한 줄 씩 읽어서 저장
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
                //return  stringBuilder.toString().trim() ;
            }catch (Exception e){
                Log.e("SampleHTTP", "Exception in processing response.", e);
                e.printStackTrace();
            }
            return  jsondata.trim();
        }
        @Override
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }
        @Override
        public void onPostExecute(String result){
            Intent intent = new Intent(MainActivity.this,AlarmList.class);
            intent.putExtra("dataList",result);
            Log.d("D1",result);
            MainActivity.this.startActivity(intent);
            //textview.setText(result);
        }
    }
    public class newBookListTask extends AsyncTask<Void,Void,String> // 신간테이블 조회
    {
        StringBuilder jsonHtml;
        String target;
        int resCode;
        String jsondata;
        @Override
        protected String doInBackground(Void... values) {
            try{
                target = "http://bk66.cafe24.com/newbook_List.php";
                URL url = new URL(target);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                if ( conn != null ) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);


                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        jsondata = br.readLine();
                        while (true) {
                            String temp = br.readLine();
                            if (temp == null)
                                break;
                            // jsonHtml.append(temp + "\n");
                            jsondata = jsondata + temp + "\n" ; //jsondata에 한 줄 씩 읽어서 저장
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
                //return  stringBuilder.toString().trim() ;
            }catch (Exception e){
                Log.e("SampleHTTP", "Exception in processing response.", e);
                e.printStackTrace();
            }
            return  jsondata.trim();
        }
        @Override
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }
        @Override
        public void onPostExecute(String result){
            Intent intent = new Intent(MainActivity.this,ItemNewList.class);
            Log.d("R1",result+" a");
            intent.putExtra("dataList",result);
            MainActivity.this.startActivity(intent);
            //textview.setText(result);
        }
    }
}