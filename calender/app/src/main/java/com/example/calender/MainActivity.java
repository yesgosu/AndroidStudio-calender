package com.example.calender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
     EditText dittextid,dittextpwd;
     Button btnlogin,btnone;
     static String userid1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dittextid = (EditText)findViewById(R.id.dittextid);
        dittextpwd = (EditText)findViewById(R.id.dittextpwd);
        btnlogin = findViewById(R.id.btnlogin); // 로그인버튼
        btnone = findViewById(R.id.btnone); // 회원가입버튼

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userid1 = dittextid.getText().toString();
                String userpassword = dittextpwd.getText().toString();


                Response.Listener<String> responserLister = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // TODO : 인코딩 문제때문에 한글 DB인 경우 로그인 불가
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다",Toast.LENGTH_SHORT).show();
                                String userid = jsonObject.getString("id");
                                String userpassword = jsonObject.getString("password");
                                Intent intentpass = new Intent(MainActivity.this,ModifyInformation.class);
                                intentpass.putExtra("id",userid1);
                                //startActivity(intentpass);
                                Intent intent = new Intent(MainActivity.this, calender.class);
                                intent.putExtra("id",userid);
                                intent.putExtra("password",userpassword);
                                startActivity(intent);

                            }else {
                                Toast.makeText(getApplicationContext(),"로그인에 실패하였습니다",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userid1,userpassword,responserLister);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(loginRequest);
            }
        });
        btnone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,membership.class);
                startActivity(intent);
            }
        });

    }
}