package com.example.calender;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.appcompat.app.AppCompatActivity;

public class calender extends AppCompatActivity {

    Button btnmemberships;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        Button btnmemberships = (Button) findViewById(R.id.btnmemberships);
        // 달력에서 날짜 선택 시 처리
        ((CalendarView) findViewById(R.id.calendarView)).setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // detailsCalendar에 선택한 날짜 정보를 전달하여 실행
            Intent intent = new Intent(calender.this, detailsCalender.class);
            intent.putExtra(detailsCalender.PARAM_YEAR, year);
            // month는 0부터 시작하므로 1을 증가시켜 전달
            intent.putExtra(detailsCalender.PARAM_MONTH, month + 1);
            intent.putExtra(detailsCalender.PARAM_DATE, dayOfMonth);
            startActivity(intent);
        });
        btnmemberships.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(calender.this, ModifyInformation.class);
                startActivity(intent);
            }
        });
    }

}