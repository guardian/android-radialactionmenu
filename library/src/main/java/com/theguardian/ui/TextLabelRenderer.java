package com.theguardian.ui;

import android.content.Context;
import android.graphics.Canvas;

public interface TextLabelRenderer {

    void draw(ActionButtonDataHolder dataHolder, Canvas canvas, boolean isSelected, float[] position);
    void init(Context context, MetricsHolder metrics, PaintHolder paints);
}
