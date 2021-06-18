package com.example.calender;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class detailsCalender extends AppCompatActivity {
    Button btnplus1;
    TextView tvTitle;
    public static final String PARAM_YEAR = "year";
    public static final String PARAM_MONTH = "month";
    public static final String PARAM_DATE = "date";
    private final List<Schedule> data = new ArrayList<>();

    private ScheduleAdapter adapter;

    private int year;
    private int month;
    private int date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_calender);
        tvTitle = findViewById(R.id.tvTitle);
        btnplus1 = (Button) findViewById(R.id.btnplus1);
        setSupportActionBar(findViewById(R.id.toolbar));
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // 선택한 날짜를 받아오기
        year = getIntent().getIntExtra(PARAM_YEAR, 0);
        month = getIntent().getIntExtra(PARAM_MONTH, 0);
        date = getIntent().getIntExtra(PARAM_DATE, 0);

        tvTitle.setText(toTitleString(month, date));

        RecyclerView rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScheduleAdapter(data, s -> {
            Intent intent = new Intent(detailsCalender.this, scheduleForm.class);
            // schedulePlus 액티비티에 선택된 날짜 전달
            intent.putExtra(scheduleForm.PARAM_TITLE, s.title);
            intent.putExtra(scheduleForm.PARAM_POINT, s.point);
            intent.putExtra(scheduleForm.PARAM_YEAR, s.year);
            intent.putExtra(scheduleForm.PARAM_MONTH, s.month);
            intent.putExtra(scheduleForm.PARAM_DATE, s.date);
            intent.putExtra(scheduleForm.PARAM_HOUR, s.hour);
            intent.putExtra(scheduleForm.PARAM_MINUTE, s.minute);
            intent.putExtra(scheduleForm.PARAM_IS_ADD, false);
            startActivityForResult(intent, 0);
        });
        rv.setAdapter(adapter);

        loadItems(year, month, date);

        btnplus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(detailsCalender.this, scheduleForm.class);
                // schedulePlus 액티비티에 선택된 날짜 전달
                intent.putExtra(scheduleForm.PARAM_YEAR, year);
                intent.putExtra(scheduleForm.PARAM_MONTH, month);
                intent.putExtra(scheduleForm.PARAM_DATE, date);
                intent.putExtra(scheduleForm.PARAM_IS_ADD, true);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            loadItems(year, month, date);
        }
    }

    private void loadItems(int year, int month, int date) {
        loadItems(year, month, date, data -> {
            this.data.clear();
            this.data.addAll(data);
            this.adapter.notifyDataSetChanged();
        });
    }

    private void loadItems(int year, int month, int date, OnItemLoadedListener onItemLoaded) {
        new Thread(() -> {
            try {
                long start = dateToEpoch(year, month, date);
                long end = dateToEpoch(year, month, date + 1);

                URL url = new URL("http://114.204.113.61/select.php?start=" + start + "&end=" + end);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setDefaultUseCaches(false);
                http.setRequestMethod("GET");
                InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);

                List<Schedule> schedules = new ArrayList<>();
                String str;
                while ((str = reader.readLine()) != null) {
                    if (str.isEmpty()) continue;
                    String[] data = str.split("/");
                    Schedule schedule = dataToSchedule(data);
                    schedules.add(schedule);
                }

                runOnUiThread(() -> onItemLoaded.onItemLoaded(schedules));
            } catch (Exception e) {
                Log.e("", "Error", e);
            }
        }).start();
    }

    private static Schedule dataToSchedule(String[] data) {
        String title = data[0];
        String point = data[1];
        long time = Long.parseLong(data[2]);

        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), TimeZone.getDefault().toZoneId());
        int year = localDateTime.getYear();
        int month = localDateTime.getMonthValue();
        int date = localDateTime.getDayOfMonth();
        int hour = localDateTime.getHour();
        int minute = localDateTime.getMinute();

        return new Schedule(title, year, month, date, hour, minute, point);
    }

    private static long dateToEpoch(int year, int month, int date) {
        return LocalDateTime.of(year, month, date, 0, 0)
                .atZone(TimeZone.getDefault().toZoneId())
                .toInstant()
                .toEpochMilli();
    }

    private static String toTitleString(int month, int date) {
        return month + "월 " + date + "일";
    }

    interface OnItemLoadedListener {
        void onItemLoaded(List<Schedule> data);
    }
}