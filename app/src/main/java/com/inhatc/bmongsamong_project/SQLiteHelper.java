package com.inhatc.bmongsamong_project;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Blob;

public class SQLiteHelper extends SQLiteOpenHelper { //sqlite관련한 crud를 도와줄 클래스
    static final String DATABASE_NAME = "BDMS_Project";

    // DBHelper 생성자
    public SQLiteHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    // Person Table 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS dreams (" +
                "d_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId TEXT not null," +
                "rec_dream BLOB,"+
                "memo TEXT," +
                "dreamdate TEXT not null DEFAULT(datetime('now','localtime')));");

    }

    // Table Upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS dreams");
        onCreate(db);
    }

    // Table 데이터 입력
    public void insert(String userId, Blob blob, String memo) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO dreams" +
                " (userId, rec_dream, memo) VALUES (" +
                "'"+userId+"', '"+blob+"', '"+memo+"');");
        db.close();
    }

    // Table 데이터 수정
    public void update(int d_id, String newMemo) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE dreams SET memo = '" + newMemo + "'" + " WHERE d_id = " + d_id);
        db.close();
    }

    // Table 데이터 삭제
    public void delete(int d_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM dreams WHERE d_id = " + d_id);
        db.close();
    }

    public Cursor getListItem(String userId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select memo, dreamdate, d_id from dreams where userId='"+ userId +"'", null);
//        db.close();

        return cursor;
    }

    // Table 조회
    public String getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM dreams", null);
        while (cursor.moveToNext()) {
            result += " d_id : " + cursor.getString(0)
                    + ", userId : "
                    + cursor.getInt(1)
                    + ", memo : "
                    + cursor.getString(2)
                    + ", dreamdate : "
                    + cursor.getString(3)
                    + "\n";
        }

        return result;
    }
}
