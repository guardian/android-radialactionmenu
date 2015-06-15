package com.theguardian.ui;

import android.graphics.Paint;
import android.text.TextPaint;

public class PaintHolder {
    public final Paint textBackgroundPaint = new Paint();
    public final TextPaint descriptionPaint = new TextPaint();
    public final TextPaint iconPaint = new TextPaint();
    public final TextPaint iconSelectedPaint = new TextPaint();

    public final Paint activeCirclePaint = new Paint();
    public final Paint inactiveCirclePaint = new Paint();
    public final Paint inactiveCirclePaintBg = new Paint();

    public final Paint circlePaint = new Paint();

    public PaintHolder(MetricsHolder metrics) {
        iconPaint.setColor(metrics.primaryColor);
        iconPaint.setTextSize(metrics.iconTextSize);
        iconPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        iconSelectedPaint.setColor(metrics.secondaryColor);
        iconSelectedPaint.setTextSize(metrics.iconTextSize);
        iconSelectedPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        activeCirclePaint.setStyle(Paint.Style.FILL);
        activeCirclePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        inactiveCirclePaint.setStrokeWidth(metrics.unselectedStrokeWidth);
        inactiveCirclePaint.setStyle(Paint.Style.STROKE);
        inactiveCirclePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        inactiveCirclePaintBg.setStyle(Paint.Style.FILL);
        inactiveCirclePaintBg.setFlags(Paint.ANTI_ALIAS_FLAG);

        activeCirclePaint.setColor(metrics.primaryColor);
        inactiveCirclePaint.setColor(metrics.primaryColor);
        inactiveCirclePaintBg.setColor(metrics.circleBackgroundInactive);

        descriptionPaint.setColor(metrics.secondaryColor);
        descriptionPaint.setTextSize(metrics.descriptionTextSize);
        descriptionPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        textBackgroundPaint.setStyle(Paint.Style.FILL);
        textBackgroundPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textBackgroundPaint.setColor(metrics.primaryColor);

        circlePaint.setStrokeWidth(metrics.circleStrokeWidth);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        circlePaint.setColor(metrics.circleColor);
    }
}