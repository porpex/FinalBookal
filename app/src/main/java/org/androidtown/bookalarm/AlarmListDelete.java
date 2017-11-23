package org.androidtown.bookalarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wlals on 2017-11-20.
 */

public class AlarmListDelete extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_author);

        Intent intent = getIntent();
        String userData = intent.getStringExtra("select_Author"); //선택한 작가명 받아옴
        String userID = LoginActivity.AlluserID;//전역변수 설정

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        finish();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AlarmListDelete.this);
                        builder.setMessage("작가 삭제에 실패했습니다.")
                                .setNegativeButton("다시 시도", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } ;
        };
        AlarmListDeleteRequest alarmlistdeleterequest = new AlarmListDeleteRequest(userData, userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(AlarmListDelete.this);
        queue.add(alarmlistdeleterequest);
    }
}
