package com.example.calender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class scheduleForm extends AppCompatActivity {
    public static final String PARAM_TITLE = "title";
    public static final String PARAM_POINT = "position";
    public static final String PARAM_YEAR = "year";
    public static final String PARAM_MONTH = "month";
    public static final String PARAM_DATE = "date";
    public static final String PARAM_HOUR = "hour";
    public static final String PARAM_MINUTE = "minute";
    public static final String PARAM_IS_ADD = "isAdd";

    int mYear, mMonth, mDay, mHour, mMinute;
    boolean isAdd;

    Button buttonConfirm, buttonCancel;
    EditText edtitle, edpoint, edtime, edits;

    String title, point, time, days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_form);

        buttonCancel = (Button) findViewById(R.id.buttondelete);
        buttonConfirm = (Button) findViewById(R.id.buttonplus);
        edtitle = (EditText) findViewById(R.id.edtitle);
        edpoint = (EditText) findViewById(R.id.edpoint);
        edtime = (EditText) findViewById(R.id.edtime);
        edits = (EditText) findViewById(R.id.edits);

        isAdd = getIntent().getBooleanExtra(PARAM_IS_ADD, true);

        if (!isAdd) {
            String title = getIntent().getStringExtra(PARAM_TITLE);
            String point = getIntent().getStringExtra(PARAM_POINT);

            edtitle.setText(title);
            edpoint.setText(point);
        }

        //현재 날짜와 시간을 가져오기위한 Calendar 인스턴스 선언
        Calendar cal = new GregorianCalendar();
        mYear = getIntent().getIntExtra(PARAM_YEAR, cal.get(Calendar.YEAR));
        mMonth = getIntent().getIntExtra(PARAM_MONTH, cal.get(Calendar.MONTH) + 1) - 1;
        mDay = getIntent().getIntExtra(PARAM_DATE, cal.get(Calendar.DAY_OF_MONTH));
        mHour = getIntent().getIntExtra(PARAM_HOUR, cal.get(Calendar.HOUR_OF_DAY));
        mMinute = getIntent().getIntExtra(PARAM_MINUTE, cal.get(Calendar.MINUTE));

        UpdateNow(); // 화면에 텍스트뷰에 업데이트


        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = edtitle.getText().toString();
                point = edpoint.getText().toString();
                time = edtime.getText().toString();
                days = edits.getText().toString();

                if (isAdd) {
                    dataplus1(title, point, time, days);
                } else {
                    update(title, point, time, days);
                }
            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isAdd) {
            getMenuInflater().inflate(R.menu.menu_schedule_edit, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        new AlertDialog.Builder(this)
                .setTitle("일정 삭제")
                .setMessage("정말로 일정을 삭제하시겠습니까?")
                .setPositiveButton("삭제", (dialog, which) -> delete())
                .setNegativeButton("취소", (dialog, which) -> dialog.dismiss())
                .show();
        return true;
    }

    void dataplus1(final String title, final String point, final String time, final String days) {
        long epochTime = timeToEpoch(days, time);

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("title", title);
        bodyMap.put("point", point);
        bodyMap.put("time", epochTime);

        postRequest("Http://114.204.113.61/insert1.php/", bodyMap);
    }

    private void update(final String targetTitle, final String targetPoint, final String time, final String days) {
        long originalTime = getOriginalTime();
        long targetTime = timeToEpoch(days, time);

        String originalTitle = getIntent().getStringExtra(PARAM_TITLE);
        String originalPoint = getIntent().getStringExtra(PARAM_POINT);

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("original_title", originalTitle);
        bodyMap.put("original_point", originalPoint);
        bodyMap.put("original_time", originalTime);
        bodyMap.put("target_title", targetTitle);
        bodyMap.put("target_point", targetPoint);
        bodyMap.put("target_time", targetTime);

        postRequest("Http://114.204.113.61/update.php/", bodyMap);
    }

    private void delete() {
        String title = getIntent().getStringExtra(PARAM_TITLE);
        String point = getIntent().getStringExtra(PARAM_POINT);
        long time = getOriginalTime();

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("title", title);
        paramsMap.put("point", point);
        paramsMap.put("time", time);

        postRequest("Http://114.204.113.61/delete.php/", paramsMap);
    }

    private long getOriginalTime() {
        int originalYear = getIntent().getIntExtra(PARAM_YEAR, 0);
        int originalMonth = getIntent().getIntExtra(PARAM_MONTH, 0);
        int originalDate = getIntent().getIntExtra(PARAM_DATE,0);
        int originalHour = getIntent().getIntExtra(PARAM_HOUR, 0);
        int originalMinute = getIntent().getIntExtra(PARAM_MINUTE, 0);

        return timeToEpoch(originalYear, originalMonth, originalDate, originalHour, originalMinute);
    }

    private void postRequest(String url, Map<String, Object> bodyData) {
        new Thread(() -> {
            try {
                URL setURL = new URL(url);
                HttpURLConnection http;
                http = (HttpURLConnection) setURL.openConnection();
                http.setDefaultUseCaches(false);
                http.setDoInput(true);
                http.setRequestMethod("POST");
                http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                StringBuilder bodyBuilder = new StringBuilder();
                for (String key : bodyData.keySet()) {
                    bodyBuilder.append(key).append("=").append(bodyData.get(key)).append("&");
                }
                if (bodyBuilder.length() > 0) {
                    bodyBuilder.deleteCharAt(bodyBuilder.length() - 1);
                }
                outStream.write(bodyBuilder.toString());
                outStream.flush();
                InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                final BufferedReader reader = new BufferedReader(tmp);

                String str;
                while ((str = reader.readLine()) != null) {
                    System.out.println(str);
                }

                finish();
            } catch (Exception e) {
                Log.e("dataInsert()", "지정 에러 발생", e);
            }
        }).start();
    }

    public void mOnclick(View v) {
        switch (v.getId()) {
            case R.id.btndate2:
                new DatePickerDialog(scheduleForm.this, mDateSetListener, mYear, mMonth, mDay).show();
                break;

            case R.id.btndate1:
                new TimePickerDialog(scheduleForm.this, mTimeSetLister, mHour, mMinute, false).show();
                break;

        }
    }

    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year1, int month1, int dayOfMonth) {
                    mYear = year1;
                    mMonth = month1;
                    mDay = dayOfMonth;
                    UpdateNow();
                }
            };
    TimePickerDialog.OnTimeSetListener mTimeSetLister =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute1) {
                    mHour = hourOfDay;
                    mMinute = minute1;
                    UpdateNow();
                }
            };

    void UpdateNow() {
        edtime.setText(String.format("%d:%d", mHour, mMinute));
        edits.setText(String.format("%d/%d/%d", mYear, mMonth + 1, mDay));
    }

    private static long timeToEpoch(String days, String time) {
        // 시간 데이터 파싱
        String[] timeSplit = time.split(":");
        int hour = Integer.parseInt(timeSplit[0]);
        int minute = Integer.parseInt(timeSplit[1]);

        // 날짜 데이터 파싱
        String[] daysSplit = days.split("/");
        int year = Integer.parseInt(daysSplit[0]);
        int month = Integer.parseInt(daysSplit[1]);
        int date = Integer.parseInt(daysSplit[2]);

        return timeToEpoch(year, month, date, hour, minute);
    }

    private static long timeToEpoch(int year, int month, int date, int hour, int minute) {
        return LocalDateTime.of(year, month, date, hour, minute)
                .atZone(TimeZone.getDefault().toZoneId())
                .toInstant()
                .toEpochMilli();
    }
}



