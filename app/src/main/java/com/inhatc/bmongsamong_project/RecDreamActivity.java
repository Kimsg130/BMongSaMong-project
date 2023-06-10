package com.inhatc.bmongsamong_project;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import android.media.MediaRecorder;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;

public class RecDreamActivity extends AppCompatActivity {
    private MediaRecorder mediaRecorder;
    private ImageButton btnRecord, btnPause;
    private Button btnSubmit, btnCancel;
    private EditText txtMemo;
    private ImageView imageView;
    private boolean isRecording = false;
    private boolean isPaused = false;

    private SQLiteHelper sqLiteHelper;

    byte[] blobData; // 녹음파일을 저장할 BLOB파일

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_dream);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        sqLiteHelper = new SQLiteHelper(getApplicationContext(), 1);

        btnRecord = findViewById(R.id.btn_start);
        btnPause = findViewById(R.id.btn_pause);
        btnSubmit = findViewById(R.id.rec_submit);
        btnCancel = findViewById(R.id.rec_cancel);
        imageView = findViewById(R.id.imageView);
        txtMemo = findViewById(R.id.editMemo);

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording) {
                    startRecording();
                } else {
                    stopRecording();
                }
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    if (!isPaused) {
                        pauseRecording();
                    } else {
                        resumeRecording();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user != null && blobData != null) {
                    String memo = txtMemo.getText().toString();
                    sqLiteHelper.insert(user.getEmail(), blobData, memo);
                    Intent listIntent = new Intent(getApplicationContext(), ListActivity.class);
                    startActivity(listIntent);
                }else if(user == null) {
                    //로그인이 안되어있을 떄
                    Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginIntent);
                }else if(blobData == null) {
                    Toast.makeText(getApplicationContext(), "녹음을 해주세요~!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(getFilePath()); // 파일 경로 설정
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            btnRecord.setImageResource(R.drawable.ic_record);
            imageView.setImageResource(R.drawable.recimage);
            btnPause.setEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null && isRecording) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
            btnRecord.setImageResource(R.drawable.ic_recording_red);
            imageView.setImageResource(R.drawable.pulse_1_3s_217px);
            btnPause.setImageResource(R.drawable.ic_audio_pause);
            btnPause.setEnabled(false);
        }

        File recordingFile = new File(getFilePath());
        blobData = convertFileToBlob(recordingFile); //blob파일로 변환
    }

    private void pauseRecording() {
        if (mediaRecorder != null && isRecording) {
            mediaRecorder.pause();
            isPaused = true;
            btnPause.setImageResource(R.drawable.ic_audio_play);
            imageView.setImageResource(R.drawable.pulse_1_3s_217px);
        }
    }

    private void resumeRecording() {
        if (mediaRecorder != null && isRecording && isPaused) {
            mediaRecorder.resume();
            isPaused = false;
            btnPause.setImageResource(R.drawable.ic_audio_pause);
            imageView.setImageResource(R.drawable.recimage);
        }
    }

    private String getFilePath() {
        // 파일 경로를 생성하는 로직을 구현합니다.
        // 예시로 임시 디렉토리에 녹음 파일을 생성합니다.
        File directory = getExternalCacheDir();
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }
        return directory.getAbsolutePath() + "/recording.3gp";
    }

    private byte[] convertFileToBlob(File file) {
        byte[] blobData = null;
        try {
            InputStream inputStream = new FileInputStream(file);
            blobData = new byte[(int) file.length()];
            inputStream.read(blobData);
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return blobData;
    }
}