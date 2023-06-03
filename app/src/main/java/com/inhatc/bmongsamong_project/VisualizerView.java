package com.inhatc.bmongsamong_project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class VisualizerView extends View {
    private Paint waveformPaint;
    private float amplitude;

    public VisualizerView(Context context) {
        super(context);
        init();
    }

    public VisualizerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 그래픽 속성 초기화
        waveformPaint = new Paint();
        waveformPaint.setColor(Color.BLUE);
        waveformPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 진폭에 맞춰 그래픽을 그립니다.
        float centerY = getHeight() / 2f;
        float height = getHeight() * amplitude;
        canvas.drawRect(0, centerY - height / 2, getWidth(), centerY + height / 2, waveformPaint);
    }

    public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
        invalidate(); // 뷰를 다시 그리도록 요청합니다.
    }
}
