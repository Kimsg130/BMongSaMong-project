package com.inhatc.bmongsamong_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class SQLiteHandler extends AppCompatActivity {
    SQLiteDatabase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_handler);

        //Create DB
        myDB = this.openOrCreateDatabase("BDMS_Project", MODE_PRIVATE, null);

        //Create 꿈을 저장할 Table
        //d_id: int, pk -> 꿈의 고유식별번호
        //userId: string nn -> 유저 아이디
        //memo: string -> 꿈에 대한 메모
        //rec_dream: BLOB -> 꿈 녹음파일
        //dreamdate: string -> 꿈을 기록한 날짜시간
        myDB.execSQL("CREATE TABLE IF NOT EXISTS dreams (" +
                "d_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId TEXT not null," +
                "rec_dream BLOB not null,"+
                "memo TEXT," +
                "dreamdate TEXT not null DEFAULT(datetime('now','localtime')));");

        //Insert testing data
        myDB.execSQL("INSERT INTO dreams" +
                " (userId, rec_dream, memo) VALUES (" +
                "'test1', 'wrongData', 'memotest2');");

        if(myDB != null) myDB.close();
    }



}

