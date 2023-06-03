package com.inhatc.bmongsamong_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    private Button btnSetAlarm;
    private Button btnCancel;
    private Button btnDel;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        btnSetAlarm = findViewById(R.id.btn_set);
        btnCancel = findViewById(R.id.btn_cancel);
        btnDel = findViewById(R.id.btn_del);
        timePicker = findViewById(R.id.time_picker);

        // 알람 설정 버튼 클릭 이벤트 처리
        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hourOfDay = timePicker.getHour();
                int minute = timePicker.getMinute();

                setAlarm(hourOfDay, minute);
            }
        });

        // 알람 취소 버튼 클릭 이벤트 처리
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });
    }

    private void setAlarm(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        Intent alarmIntent = new Intent(AlarmActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent); //앱이 백그라운드에 있어도 실행
            Toast.makeText(AlarmActivity.this, "알람이 설정되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelAlarm() {
        if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(AlarmActivity.this, "알람이 취소되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}