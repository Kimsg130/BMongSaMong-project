package com.inhatc.bmongsamong_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowAndEditDream extends AppCompatActivity { //꿈의 기록을 보고 수정할 수 있는 액티비티 TODO:여기 완성

    private SQLiteHelper sqLiteHelper;
    private int d_id; //꿈의 식별번호
    private ImageView imageView;
    private ImageButton btnStart, btnPause;
    private Button btnModify;
    private EditText txtMemo;
    private AudioTrack audioTrack;
    private byte[] audioData;
    private boolean isPlaying;
    private boolean isPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_and_edit_dream);

        Intent intent = getIntent();
        d_id = intent.getIntExtra("d_id", 0);

        if(d_id == 0) finish();

        sqLiteHelper = new SQLiteHelper(getApplicationContext(), 1);

        Cursor cursor = sqLiteHelper.getItemByD_id(d_id);
        cursor.moveToFirst();
        String d_memo = cursor.getString(1);
        String d_date = cursor.getString(2);
        byte[] d_rec = cursor.getBlob(3);

        cursor.close();

        audioData = d_rec;

        //툴바 불러오기
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(d_date+" Dream");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //툴바의 뒤로가기 버튼 활성화

        txtMemo = findViewById(R.id.editMemo2);
        imageView = findViewById(R.id.imageView2);
        btnStart = findViewById(R.id.btn_start2);
        btnPause = findViewById(R.id.btn_start2);
        btnModify = findViewById(R.id.btn_modify);

        txtMemo.setText(d_memo);
        btnPause.setEnabled(false);

        // 버튼 클릭 이벤트 설정
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
                    startAudio();
                } else {
                    restartAudio();
                }
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying && !isPaused) {
                    pauseAudio();
                } else {
                    resumeAudio();
                }
            }
        });

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newMemo = txtMemo.getText().toString();
                sqLiteHelper.update(d_id, newMemo);
                finish();
            }
        });
    }

    private void startAudio() {
        // 오디오 트랙 초기화
        int bufferSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);

        // 오디오 재생 시작
        audioTrack.play();
        audioTrack.write(audioData, 0, audioData.length);

        // 상태 변수 업데이트
        isPlaying = true;
        isPaused = false;

        // 버튼 상태 변경
        btnStart.setImageResource(R.drawable.re_audio_red);
        btnPause.setEnabled(true);
        imageView.setImageResource(R.drawable.recimage);
    }

    private void restartAudio() {
        // 오디오 처음으로 돌아감
        audioTrack.stop();
        audioTrack.reloadStaticData();
        audioTrack.play();

        // 상태 변수 업데이트
        isPaused = false;

        btnPause.setImageResource(R.drawable.ic_audio_pause);
    }

    private void pauseAudio() {
        // 오디오 일시중지
        audioTrack.pause();

        // 상태 변수 업데이트
        isPaused = true;

        // 버튼 상태 변경
        btnPause.setImageResource(R.drawable.ic_audio_play);
        imageView.setImageResource(R.drawable.pulse_1_3s_217px);
    }

    private void resumeAudio() {
        // 오디오 재개
        audioTrack.play();

        // 상태 변수 업데이트
        isPaused = false;

        // 버튼 상태 변경
        imageView.setImageResource(R.drawable.recimage);
        btnPause.setImageResource(R.drawable.ic_audio_pause);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 액티비티 종료 시 오디오 트랙 해제
        if (audioTrack != null) {
            audioTrack.release();
        }
    }
}