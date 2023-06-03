package com.inhatc.bmongsamong_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class ShowAlarmAtivity extends AppCompatActivity {
    private TextView textDate;
    private Button btnREC;
    private Button btnNo;
    private Ringtone alarmRingtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alarm_ativity);

        btnREC = findViewById(R.id.btnRec);
        btnNo = findViewById(R.id.btnNo);
        textDate = findViewById(R.id.textTime);

        //시간 가져오기
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        textDate.setText(hour+":"+minute);

        btnREC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecDreamActivity.class);
                startActivity(intent);
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 알람 소리 URI 가져오기
        Uri alarmUri = Settings.System.DEFAULT_ALARM_ALERT_URI;

        // Ringtone 객체 생성
        alarmRingtone = RingtoneManager.getRingtone(this, alarmUri);
    }

    // 액티비티가 화면에 표시될 때 알람 소리 재생
    @Override
    protected void onResume() {
        super.onResume();
        if (alarmRingtone != null) {
            alarmRingtone.play();
        }
    }

    // 액티비티가 일시 중지될 때 알람 소리 중지
    @Override
    protected void onPause() {
        super.onPause();
        if (alarmRingtone != null) {
            alarmRingtone.stop();
        }
    }

    // 액티비티가 종료될 때 알람 소리 해제
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alarmRingtone != null) {
            alarmRingtone.stop();
            alarmRingtone = null;
        }
    }
}