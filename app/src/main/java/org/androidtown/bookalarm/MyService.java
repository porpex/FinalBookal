package org.androidtown.bookalarm;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.*;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.O)
@RequiresApi(api = Build.VERSION_CODES.O)
public class MyService extends Service {
    private RequestQueue queue;
    NotificationManager Notifi_M;
    ServiceThread thread;
    Notification Notifi ;
    String jsondata;
    String ik = "my_channel_01";
    static String AlluserID2;
    String soCheck;
    String viCheck;

    String userAuthor,userAlarmcheck,UserAuthorPlus;
    private List<list_author> List_ArrayAuthor;
    list_author list_arrayauthor;
    SharedPreferences setting;
    SharedPreferences.Editor editor;





// 알림 채널에 사용할 설정을 구성한다.





    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//*****************************************************************************************오레오 8.0.0 API26 부터 울리는 코드 채널 생성 시작
/*
       NotificationManager mNotificationManager =
               (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// 채널 Id
       String id = "my_channel_01";
// 채널 이름
       CharSequence name = "test";
       int importance = NotificationManager.IMPORTANCE_LOW;
       NotificationChannel mChannel = new NotificationChannel(id, name, importance);
       mChannel.enableLights(true);
       mChannel.setLightColor(Color.RED);
       mChannel.enableVibration(true);
       mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
       mNotificationManager.createNotificationChannel(mChannel);*/

//*****************************************************************************************오레오 8.0.0 API26 부터 울리는 코드 채널 생성 종료





        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        AlluserID2=pref.getString("ID", "");

        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
        return START_STICKY;

    }

    //서비스가 종료될 때 할 작업

    public void onDestroy() {
        thread.stopForever();
        thread = null;//쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌.
    }

    class myServiceHandler extends Handler {

        @Override
        public void handleMessage(android.os.Message msg) {

            new MyService.AlarmListTask().execute(AlluserID2);

            Intent intent = new Intent(MyService.this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
            new AlarmListTask() ;
            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
            soCheck=pref.getString("soCheck", "");
            viCheck=pref.getString("viCheck", "");
            //Toast.makeText(MyService.this, "돌공있당"+soCheck+"소리 진동"+viCheck, Toast.LENGTH_LONG).show();

            if (userAuthor == null || userAuthor.equals("")){

            }
            else{
                //*****************************************************************************************오레오 8.0.0 API26 부터 울리는 코드 시작
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                // 알림 ID
                int notifyID = 1;
// 1번에서 생성한 채널 ID 이름
                String CHANNEL_ID = "my_channel_01";
// 알림을 만들고 알림 채널을 설정합니다.
                Notification notification = new Notification.Builder(MyService.this)
                        .setContentTitle("신간알림")
                        .setContentText(UserAuthorPlus+"작가분의 신간이 나왔습니다.")
                        .setSmallIcon(R.drawable.newbookmark)
                        .setTicker("신간알림")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                       // .setChannelId(CHANNEL_ID)
                        .build();
                Vibrator via =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                long[] pattern = {100,300,100,300};
                final SoundPool sp = new SoundPool(1,AudioManager.STREAM_MUSIC, 0);
                final int soundID = sp.load(MyService.this, R.raw.booksori, 1);
                userAuthor = "";
                if(soCheck.equals("true")) {
                    sp.play(soundID, 1.0f, 1.0f, 0, 0, 1.0f);
                    int waitLimit = 1000;
                    int waitCounter = 0;
                    int throttle = 10;
                    while (sp.play(soundID, 1.f, 1.f, 1, 0, 1.f) == 0 && waitCounter < waitLimit) {
                        waitCounter++;
                        SystemClock.sleep(throttle);
                    }
                }
                if(viCheck.equals("true")) {
                    via.vibrate(pattern, -1);
                }
                mNotificationManager.notify(notifyID, notification);

                //*****************************************************************************************오레오 8.0.0 API26 부터 울리는 코드 종료
            }
        }
    };

    public class AlarmListTask extends AsyncTask<String,Void,String> // 알람내역 조회
    {
        StringBuilder jsonHtml;
        String target;
        int resCode;

        @Override
        protected String doInBackground(String... progress) {
            int count=0;
            try{
                target = "http://bk66.cafe24.com/alarm_List.php?id="+AlluserID2;
                URL url = new URL(target);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if ( conn != null ) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        jsondata = br.readLine();

                        List_ArrayAuthor = new ArrayList<list_author>();
                        JSONObject jsonObject = new JSONObject(jsondata.toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("response"); //배열형태로 받아옴
                        while (count < jsonArray.length()) {
                            JSONObject object = jsonArray.getJSONObject(count);
                            if (userAuthor == null || userAuthor.equals("")){
                                userAuthor = "";
                            }
                            if (UserAuthorPlus == null || UserAuthorPlus.equals("")){
                                UserAuthorPlus = "";
                            }

                            userAlarmcheck = object.getString("Alarmcheck");
                            // if(userAlarmcheck=="XXX")
                            if(userAlarmcheck.contains("X"))
                            {
                                userAuthor = object.getString("Author");
                                UserAuthorPlus = UserAuthorPlus +" "+ object.getString("Author");
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if (success) {

                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(MyService.this);
                                                builder.setMessage("작가 등록에 실패했습니다.")
                                                        .setNegativeButton("다시 시도", null)
                                                        .create()
                                                        .show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } ;
                                };
                                MyserviceRequest myserviceRequest = new MyserviceRequest(AlluserID2, userAuthor, responseListener);
                                queue = getRequestQueue();
                                queue.add(myserviceRequest);
                            }
                            count++;
                        }
                        while (true) {
                            String temp = br.readLine();
                            if (temp == null)
                                break;
                            // jsonHtml.append(temp + "\n");
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

    }
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (queue == null) {
            queue = Volley.newRequestQueue(getApplicationContext());
        }

        return queue;
    }


}


