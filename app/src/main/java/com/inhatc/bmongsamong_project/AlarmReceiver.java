package com.inhatc.bmongsamong_project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 알람 시간이 되었을 때 실행될 코드 작성
        // 다른 액티비티를 띄우기 위한 Intent 생성
        Intent activityIntent = new Intent(context, RecDreamActivity.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 새로운 태스크로 액티비티 실행
        context.startActivity(activityIntent);
    }

}
