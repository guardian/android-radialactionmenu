package com.theguardian.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.theguardian.R;

public class MetricsHolder {
    public final float nudgeFactor;
    public final int actionButtonRadius;
    public final int selectedActionButtonRadius;

    public final int iconTextSize;
    public final int unselectedStrokeWidth;

    public final int circleRadius;
    public final int circleStrokeWidth;
    public final float positioningRadius;
    public final float activeAreaSelectedRadius;

    public final int primaryColor;
    public final int secondaryColor;
    public final int circleColor;
    public final int circleBackgroundInactive;

    public final int descriptionTextSize;

    public MetricsHolder(Context context, AttributeSet attrs) {
        Resources res = context.getResources();

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.RadialActionMenuView,
                0, 0);

        try {
            nudgeFactor = a.getFloat(R.styleable.RadialActionMenuView_nudgeFactor, 1.2f);
            actionButtonRadius = a.getDimensionPixelSize(R.styleable.RadialActionMenuView_actionButtonRadius, (int) (res.getDisplayMetrics().density * 25));
            selectedActionButtonRadius = a.getDimensionPixelSize(R.styleable.RadialActionMenuView_selectedActionButtonRadius, (int) (res.getDisplayMetrics().density * 30));
            iconTextSize = a.getDimensionPixelSize(R.styleable.RadialActionMenuView_iconTextSize, (int) (res.getDisplayMetrics().density * 22));
            unselectedStrokeWidth = a.getDimensionPixelSize(R.styleable.RadialActionMenuView_unselectedStrokeWidth, (int) (res.getDisplayMetrics().density * 1));
            circleRadius = a.getDimensionPixelSize(R.styleable.RadialActionMenuView_circleRadius, (int) (res.getDisplayMetrics().density * 35));
            positioningRadius = a.getDimensionPixelSize(R.styleable.RadialActionMenuView_positioningRadius, (int) (res.getDisplayMetrics().density * 100));
            activeAreaSelectedRadius = a.getDimensionPixelSize(R.styleable.RadialActionMenuView_actionButtonSelectedRadius, (int) (res.getDisplayMetrics().density * 45));
            primaryColor = a.getColor(R.styleable.RadialActionMenuView_primaryColor, 0xFF0000FF);
            secondaryColor = a.getColor(R.styleable.RadialActionMenuView_secondaryColor, 0xFFFFFFFF);
            circleColor = a.getColor(R.styleable.RadialActionMenuView_circleColor, 0xFFFFFFFF);
            circleBackgroundInactive = a.getColor(R.styleable.RadialActionMenuView_circleBackgroundInactive, 0x77333333);
            descriptionTextSize = a.getDimensionPixelSize(R.styleable.RadialActionMenuView_descriptionTextSize, (int) (res.getDisplayMetrics().density * 14));
            circleStrokeWidth = a.getDimensionPixelSize(R.styleable.RadialActionMenuView_circleStrokeWidth, (int) (res.getDisplayMetrics().density * 6));
        } finally {
            a.recycle();
        }
    }
}