package com.theguardian.ui;

import android.graphics.PointF;

public interface PositionCalculator {

    void calcUsableRadius(RadialActionMenuView view, ActionButtonDataHolder[] activeAreas, PointF downPosition, MetricsHolder metrics);
}
