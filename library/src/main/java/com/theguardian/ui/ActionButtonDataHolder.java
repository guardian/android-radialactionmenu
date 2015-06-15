package com.theguardian.ui;

import android.graphics.PointF;

public class ActionButtonDataHolder{
    private RadialActionMenuAction action;
    private PointF position;
    private PointF downPosition;

    public ActionButtonDataHolder(RadialActionMenuAction action, PointF position, PointF downPosition) {
        this.action = action;
        this.position = position;
        this.downPosition = downPosition;
    }

    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
    }

    public RadialActionMenuAction getAction() {
        return action;
    }

    public PointF getPosition() {
        return position;
    }

    public PointF getDownPosition() {
        return downPosition;
    }

}