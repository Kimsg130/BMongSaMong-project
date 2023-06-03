package com.inhatc.bmongsamong_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RecDreamActivity extends AppCompatActivity { //TODO: 오류나니까 고쳐야됨 시각화
    private static final int PERMISSION_REQUEST_CODE = 1;

    private Button btnRecord;
    private LinearLayout visualizerLayout;
    private Visualizer visualizer;
    private MediaRecorder mediaRecorder;
    private byte[] audioData;
    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_dream);
        btnRecord = findViewById(R.id.btn_record);
        visualizerLayout = findViewById(R.id.visualizer_layout);

        // 저장소 접근 권한 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CODE);
        }

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording) {
                    startRecording();
                    startVisualizer();
                    btnRecord.setText("녹음 종료");
                } else {
                    stopRecording();
                    stopVisualizer();
                    btnRecord.setText("녹음 시작");
                }
                isRecording = !isRecording;
            }
        });
    }

    private void startRecording() {
        // 녹음 시작 코드 작성
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        // 녹음 종료 코드 작성
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

        // 녹음된 오디오 데이터를 Blob 형식으로 저장
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        audioData = outputStream.toByteArray();

        // Blob 파일에 대한 처리를 추가해주세요
        // 여기서부터 작성해주세요
        // ...
        // 작성해주세요
        // ...
    }

    private void startVisualizer() {
        int audioSessionId = AudioRecord.getMinBufferSize(
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
        );

        visualizer = new Visualizer(audioSessionId);
        visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);

        Visualizer.OnDataCaptureListener captureListener = new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                // 음파 데이터를 이용하여 시각화 처리를 수행합니다.
                // waveform 배열에서 값을 읽어와서 그래프를 그리거나 뷰의 속성을 조정하는 등의 작업을 수행합니다.
                // ...

                // 예시: 음파 데이터의 최대 진폭을 계산하여 정규화합니다.
                int maxAmplitude = 0;
                for (byte value : waveform) {
                    int amplitude = Math.abs(value);
                    if (amplitude > maxAmplitude) {
                        maxAmplitude = amplitude;
                    }
                }
                float normalizedAmplitude = maxAmplitude / 128f;

                // 정규화된 진폭을 이용하여 시각화 뷰를 업데이트합니다.
                updateVisualizer(normalizedAmplitude);
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
                // FFT 데이터를 처리하는 코드 작성 (옵션)
                // ...
            }
        };

        visualizer.setDataCaptureListener(captureListener,
                Visualizer.getMaxCaptureRate() / 2, true, true);
        visualizer.setEnabled(true);

        VisualizerView visualizerView = new VisualizerView(this);

        // 시각화 뷰를 visualizerLayout에 추가
        visualizerLayout.addView(visualizerView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
    }

    private void updateVisualizer(float amplitude) {
        // 레이아웃에서 시각화 뷰를 가져옵니다.
        VisualizerView visualizerView = findViewById(R.id.visualizer_view);

        // 새로운 진폭 값을 시각화 뷰에 적용합니다.
        visualizerView.setAmplitude(amplitude);
    }

    private void stopVisualizer() {
        visualizer.setEnabled(false);
        visualizer.release();

        // 시각화 뷰를 visualizerLayout에서 제거
        visualizerLayout.removeAllViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRecording) {
            stopRecording();
            stopVisualizer();
        }
    }
}