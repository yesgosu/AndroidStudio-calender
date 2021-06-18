package com.example.calender;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class membership extends AppCompatActivity {
    Button btninsert;
    EditText idet, pwet, pwdetch1;
    String name,password,pwdcheck;
    ImageView setimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);
        Button delete = (Button) findViewById(R.id.delete);
        setTitle("ORACLE");
        setimage = (ImageView)findViewById(R.id.setimage);

        btninsert = (Button) findViewById(R.id.btninsert);
        idet = (EditText) findViewById(R.id.memberid);
        pwet = (EditText) findViewById(R.id.memberpwd);
        pwdetch1 = (EditText) findViewById(R.id.memberpwdcheck);

        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = idet.getText().toString();
                password = pwet.getText().toString();
                pwdcheck = pwdetch1.getText().toString();

                dataInsert(name, password, pwdcheck);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(membership.this, MainActivity.class);
                startActivity(intent);
            }
        });
        pwdetch1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(pwet.getText().toString().equals(pwdetch1.getText().toString())) {
                    setimage.setImageResource(R.drawable.whlie);
                }else {
                    setimage.setImageResource(android.R.drawable.ic_delete);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


        void dataInsert(final String id,final String password,final String pwdcheck) {
            new Thread() {
                public void run() {
                    try {
                        URL setURL = new URL("Http://114.204.113.61/insert.php/");
                        HttpURLConnection http;
                        http = (HttpURLConnection) setURL.openConnection();
                        http.setDefaultUseCaches(false);
                        http.setDoInput(true);
                        http.setRequestMethod("POST");
                        http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                        StringBuffer buffer = new StringBuffer();
                        buffer.append("id").append("=").append(id).append("/").append(password).append("/");
                        OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                        outStream.write(buffer.toString());
                        outStream.flush();
                        InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                        final BufferedReader reader = new BufferedReader(tmp);
                        Intent intent = new Intent(membership.this,MainActivity.class);
                        startActivity(intent);

                        while (reader.readLine() != null) {
                            System.out.println(reader.readLine());
                        }
                    } catch (Exception e) {
                        Log.e("dataInsert()", "지정 에러 발생", e);
                    }
                }
            }.start();
        }

    }

