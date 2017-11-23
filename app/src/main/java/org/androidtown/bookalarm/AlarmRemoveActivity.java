package org.androidtown.bookalarm;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AlarmRemoveActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_author);

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
                        AlertDialog.Builder builder = new AlertDialog.Builder(AlarmRemoveActivity.this);
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
        AlArmRemoveRequest alarmRemoveRequest = new AlArmRemoveRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(AlarmRemoveActivity.this);
        queue.add(alarmRemoveRequest);
    }
}
