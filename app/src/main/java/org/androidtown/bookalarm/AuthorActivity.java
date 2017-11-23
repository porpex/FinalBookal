package org.androidtown.bookalarm;

/**
 * Created by wlals on 2017-10-26.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AuthorActivity  extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_send_author);
        final String Url = intent.getStringExtra("Url");
        final String ImageUrl = intent.getStringExtra("ImageUrl");
        final String BookName = intent.getStringExtra("BookName");


        String Author = intent.getStringExtra("author"); //작가명 받아옴
        String userID = LoginActivity.AlluserID;//전역변수 설정
        String url = Url.toString();
        String imageurl = ImageUrl.toString();
        String bookname = BookName.toString();
        String nowdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));

        /*
        int length = AlarmStateListAdapter.authorDataLength;
        for(int i=0;i<AlarmStateListAdapter.author_AppendData.size(); i++ ) {
           // if (Author.equals(AlarmStateListAdapter.author_AppendData.get(i).toString()))
                Log.d("S1", "i =" + i + " 데이터 : " +AlarmStateListAdapter.author_AppendData.get(i).toString() );
        }*/
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        finish();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AuthorActivity.this);
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
        AuthorRequest authorRequest = new AuthorRequest(userID, Author,nowdate, imageurl,url,bookname, responseListener);
        RequestQueue queue = Volley.newRequestQueue(AuthorActivity.this);
        queue.add(authorRequest);
    }

}






