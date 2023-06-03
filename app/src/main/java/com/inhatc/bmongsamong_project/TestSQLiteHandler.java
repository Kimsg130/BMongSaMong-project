package com.inhatc.bmongsamong_project;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.sql.Blob;

public class TestSQLiteHandler extends AppCompatActivity { //sqlite관련해서 테스트를 진행하기 위한 액티비티
    SQLiteHelper sqLiteHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_handler);

        //Create 꿈을 저장할 Table: dreams
        //  d_id: int, pk -> 꿈의 고유식별번호
        //  userId: string nn -> 유저 아이디
        //  memo: string -> 꿈에 대한 메모
        //  rec_dream: BLOB -> 꿈 녹음파일
        //  dreamdate: string -> 꿈을 기록한 날짜시간
        sqLiteHelper = new SQLiteHelper(getApplicationContext(), 1);

//        sqLiteHelper.onUpgrade(db, 1, 2);

        for (int i = 10; i < 20; i++) {
            sqLiteHelper.insert("test1", null, "testing memo"+i);
        }


    }



}

