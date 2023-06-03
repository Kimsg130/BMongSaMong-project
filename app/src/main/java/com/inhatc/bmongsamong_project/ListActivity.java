package com.inhatc.bmongsamong_project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    final String userId = "test1"; //테스트를 위한 유저아이디, TODO: 향후 로그인 기능이 완성되었을 때 변경

    SQLiteDatabase myDB;
    CustomAdapter customAdapter;
    ArrayList<Item> aryItemList;
    ListView lstView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        
        //툴바 불러오기
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //툴바의 뒤로가기 버튼 활성화

        //DB열기
        myDB = this.openOrCreateDatabase("BDMS_Project", MODE_PRIVATE, null);

        //DB에서 아이디로 셀렉트 TODO: 드림즈 테이블이 없다는데 한번 확인
        Cursor allRCD = myDB.rawQuery("select memo, dreamdate from dreams where userId='"+ userId +"'", null);

        //리스트에 저장
        aryItemList = new ArrayList<>();
        if(allRCD != null) {
            if(allRCD.moveToFirst()) {
                do{
                    Item item = new Item(allRCD.getString(0), allRCD.getString(1));
                    aryItemList.add(item);
                }while(allRCD.moveToNext());
            }
        }

        customAdapter = new CustomAdapter(this, R.layout.item_layout, aryItemList);
        //리스트뷰 만들기
        lstView = (ListView) findViewById(R.id.list);
        lstView.setAdapter(customAdapter);

        if (myDB != null) myDB.close();

    }
}