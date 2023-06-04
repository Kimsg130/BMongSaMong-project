package com.inhatc.bmongsamong_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth mFirebaseAuth;             //firebase 인증
    private DatabaseReference mDatabaseRef;         //실시간 데이터 베이스
    private EditText mEtEmail, mEtName, mEtPwd1,mEtPwd2;
    private Button mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mEtEmail = findViewById(R.id.et_email);
        mEtName = findViewById(R.id.et_Name);
        mEtPwd1 = findViewById(R.id.et_pwd1);
        mEtPwd2 = findViewById(R.id.et_pwd2);
        mBtnRegister = findViewById(R.id.btn_Register);


        // 텍스트뷰 클릭시 이전화면으로 이동
        TextView textView = findViewById(R.id.goBackToLogin);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 처리시작
                String strEmail = mEtEmail.getText().toString();
                String strName = mEtName.getText().toString();
                String strPwd1 = mEtPwd1.getText().toString();
                String strPwd2 = mEtPwd2.getText().toString();


                if (strEmail.isEmpty() || strName.isEmpty() || strPwd1.isEmpty() || strPwd2.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "빈칸없이 모두 입력해주세요.", Toast.LENGTH_SHORT).show();

                } else {
                    //비밀번호와 비밀번호확인 문자열 확인
                    if (strPwd1.equals(strPwd2)) {
                        if (strPwd1.length() >= 6) {
                            //firebaseAuth진행
                            mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd1).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                        UserAccount account = new UserAccount();
                                        account.setIdToken(firebaseUser.getUid());
                                        account.setEmailId(firebaseUser.getEmail());
                                        account.setUserName(strName);
                                        account.setPassword(strPwd1);

                                        mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                                        Toast.makeText(RegisterActivity.this, "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(RegisterActivity.this, ListActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Toast.makeText(RegisterActivity.this, "회원가입을 실패했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "비밀번호는 6자 이상으로 설정해주세요!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(RegisterActivity.this, "비밀번호를 다시확인해주세요!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //액션바 숨기기
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

    }


}