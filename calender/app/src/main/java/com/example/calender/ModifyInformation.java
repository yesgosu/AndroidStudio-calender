package com.example.calender;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class ModifyInformation extends AppCompatActivity {
   //public static final String id = "id";
    Handler handler1 =  new Handler();
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_information);
        Button btnlogout = findViewById(R.id.btnlogout);
        Button btnbye = findViewById(R.id.btnbye);
        Button btnstep1 = findViewById(R.id.btnstep1);
        Intent intent = getIntent(); // 데이터 수신
        userid = intent.getStringExtra("id");
        String data = intent.getStringExtra("id");

        btnstep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnbye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(ModifyInformation.this)
                        .setTitle("회원탈퇴").setMessage("정말로하시겠습니까?")
                        .setPositiveButton("회원탈퇴", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dataDelete(data);
                                Intent j = new Intent(ModifyInformation.this, MainActivity.class);

                                j.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(j);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnlogout1();
            }
        });
    }
    public void btnlogout1() {
        new AlertDialog.Builder(this)
                .setTitle("로그아웃").setMessage("로그아웃하시겠습니까?")
                .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(ModifyInformation.this,MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void dataDelete(String id){
        new Thread(() -> {
            try{
                URL setURL = new URL("Http://114.204.113.61/delete1.php/");
                HttpURLConnection http;
                http = (HttpURLConnection) setURL.openConnection();
                http.setDefaultUseCaches(false);
                http.setDoInput(true);
                http.setRequestMethod("POST");
                http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                StringBuffer buffer = new StringBuffer();
                buffer.append("id").append("=").append(id);
                OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(), "utf-8");
                osw.write(buffer.toString());
                osw.flush();
                InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "utf-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();
                String resultData = builder.toString();
                String[] sResult = resultData.split("/");
                handler1.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ModifyInformation.this, userid + "님이 회원탈퇴되었습니다.", Toast.LENGTH_LONG).show();
                    }
                });
            }catch(Exception e){
                Log.e("", "Error", e);
            }
        }).start();

    }

}