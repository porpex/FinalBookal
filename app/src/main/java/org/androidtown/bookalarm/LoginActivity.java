package org.androidtown.bookalarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.androidtown.bookalarm.LoginRequest;
import org.androidtown.bookalarm.MainActivity;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    static String AlluserID;
    SharedPreferences setting;
    long pressTime;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)


    @Override
    public void onBackPressed() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView image2 = (ImageView)findViewById(R.id.imageView2);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.sarajim);
        image2.startAnimation(animation);


        final EditText idText = (EditText) findViewById(R.id.idText);
        idText.requestFocus();
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final Button loginButton = (Button) findViewById(R.id.loginButton);
        final TextView registerButton = (TextView) findViewById(R.id.registerButton);
        final CheckBox AutoLog = (CheckBox) findViewById(R.id.AutoLog);
        final SharedPreferences.Editor editor;



        passwordText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    loginButton.performClick();
                    return true;
                }
                return false;
            }
        });


        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();
        if (setting.getBoolean("chk_auto", false)) {
            idText.setText(setting.getString("userID", ""));
            passwordText.setText(setting.getString("userPassword", ""));
            AutoLog.setChecked(true);
        }


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userID = idText.getText().toString();
                final String userPassword = passwordText.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                if (AutoLog.isChecked()) {
                                    editor.putString("userID", userID);
                                    editor.putString("userPassword", userPassword);
                                    editor.putBoolean("chk_auto", true);
                                    editor.commit();
                                    String userID = jsonResponse.getString("userID");
                                    String userPassword = jsonResponse.getString("userPassword");
                                    Toast.makeText(LoginActivity.this, userID + "님 자동로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    String userID = jsonResponse.getString("userID");
                                    String userPassword = jsonResponse.getString("userPassword");
                                    Toast.makeText(LoginActivity.this, userID + "님 환영합니다.", Toast.LENGTH_SHORT).show();
                                    editor.clear();
                                    editor.commit();
                                }
                                Toast.makeText(getApplicationContext(),"Service 시작",Toast.LENGTH_SHORT).show();
                                Intent intents = new Intent(LoginActivity.this,MyService.class);
                                startService(intents);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("userID", userID);
                                intent.putExtra("userPassword", userPassword);
                                LoginActivity.this.startActivity(intent);
                                finish();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("로그인에 실패했습니다.")
                                        .setNegativeButton("다시 시도", null)
                                        .create()
                                        .show();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
                AlluserID = userID;


                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("ID", userID);
                editor.commit();




            }
        });
        if(AutoLog.isChecked())
        {
            loginButton.performClick();

        }
    }

}
