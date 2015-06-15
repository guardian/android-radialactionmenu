package com.theguardian.ui;

import android.content.Context;

public abstract class BaseActionButtonRenderer implements ActionButtonRenderer {
    private MetricsHolder metrics;
    private PaintHolder paints;

    public BaseActionButtonRenderer() {

    }

    @Override
    public void init(Context context, MetricsHolder metrics, PaintHolder paints) {
        this.metrics = metrics;
        this.paints = paints;
    }

    public MetricsHolder getMetrics() {
        return metrics;
    }

    public PaintHolder getPaints() {
        return paints;
    }
}