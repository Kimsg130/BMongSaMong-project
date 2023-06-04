package com.inhatc.bmongsamong_project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    CustomAdapter customAdapter;
    ArrayList<Item> aryItemList;
    ListView lstView;
    SQLiteHelper sqLiteHelper;
    ImageButton btn;

    String userId;
    String UserUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        sqLiteHelper = new SQLiteHelper(getApplicationContext(), 1);

        //로그인되어있는 정보 불러오기
        if(user != null) {
            userId = user.getEmail();
            UserUid = user.getUid();
        }

        //툴바 불러오기
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //툴바의 뒤로가기 버튼 활성화

        //DB에서 아이디로 셀렉트
        Cursor allRCD = sqLiteHelper.getListItem(userId);

        //리스트에 저장
        aryItemList = new ArrayList<>();
        if(allRCD != null) {
            if(allRCD.moveToFirst()) {
                do{
                    Item item = new Item(allRCD.getString(0), allRCD.getString(1), allRCD.getInt(2));
                    aryItemList.add(item);
                }while(allRCD.moveToNext());
            }
        }
        allRCD.close();

        customAdapter = new CustomAdapter(this, R.layout.item_layout, aryItemList);
        //리스트뷰 만들기
        lstView = (ListView) findViewById(R.id.list);
        lstView.setAdapter(customAdapter);

        //버튼
        btn = findViewById(R.id.addAlarmButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
                startActivity(intent);
            }
        });

        lstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                customShowDialog(position); //경고창 띄우기
                return true;
            }
        });

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item target = (Item) customAdapter.getItem(position);
                int d_id = target.getD_id();

                Intent intent = new Intent(getApplicationContext(), ShowAndEditDream.class);
                intent.putExtra("d_id", d_id);
                startActivity(intent);
            }
        });
    }

    public void customShowDialog(int position) {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(this)
                .setTitle("꿈 기록 삭제")
                .setMessage("정말 "+(position+1)+"번째 꿈을 삭제 하시겠습니까?")
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Item target = (Item) customAdapter.getItem(position);
                        int d_id = target.getD_id();

                        try {
                            sqLiteHelper.delete(d_id);
                            aryItemList.remove(position);
                            customAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), (position+1)+"번째 아이템이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        }catch (Exception e) {
                            System.out.println(e);
                            Toast.makeText(getApplicationContext(), (position+1)+"번째 아이템이 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        closeContextMenu();
                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // toolbar_menu.xml 파일을 인플레이트합니다.
        // 이 메뉴를 툴바에 추가합니다.
        getMenuInflater().inflate(R.menu.list_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut(); //로그아웃
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}