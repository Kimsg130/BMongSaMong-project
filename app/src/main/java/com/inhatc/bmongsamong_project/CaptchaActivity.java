package com.inhatc.bmongsamong_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class CaptchaActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth mFirebaseAuth;             //firebase 인증
    private DatabaseReference mDatabaseRef;         //실시간 데이터 베이스

    private Button btnCheck;
    private EditText etCaptcha;
    private TextView tvCaptach;
    private String captchaCode;


    public static String getCaptcha() {
        Random rd = new Random(System.currentTimeMillis());
        StringBuilder strBuilder = new StringBuilder("");
        String tmp = new String("123456789abcdefghijklmnopqrstuvwxyz");
        for (int i = 0; i < 8; i++) {
            strBuilder.append(tmp.charAt(rd.nextInt(35)));
        }
        return strBuilder.toString();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captcha);


        //로그인 엑티비티에서 이메일, 비밀번호를 받아옴
        Intent intent = getIntent();
        String strEmail = intent.getStringExtra("EMAIL");
        String strPwd = intent.getStringExtra("PASSWORD");

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        tvCaptach = findViewById(R.id.tv_captcha);
        etCaptcha = findViewById(R.id.et_chatchaCheck);
        btnCheck = findViewById(R.id.btn_check);

        captchaCode = getCaptcha();
        tvCaptach.setText(captchaCode);


        // 확인버튼 클릭시
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(captchaCode.equals(etCaptcha.getText().toString())){
                    mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(CaptchaActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //로그인 성공
                                Toast.makeText(CaptchaActivity.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(CaptchaActivity.this, ListActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(CaptchaActivity.this, "로그인을 실패했습니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }else{
                    Toast.makeText(CaptchaActivity.this, "코드를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        // 텍스트뷰 클릭시 이전화면으로 이동
        TextView textView = findViewById(R.id.goToBack);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //액션바 숨기기
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
}