package com.theguardian.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.HashMap;

public class DefaultTextLabelRenderer implements TextLabelRenderer {
    private PaintHolder paints;

    private RectF tempRectF = new RectF();
    private Rect tempRect = new Rect();

    private float spacing;
    private float paddingHorz;
    private float paddingVert;
    private float radius;

    private HashMap<ActionButtonDataHolder, LabelRenderHolder> renderData = new HashMap<>();

    public DefaultTextLabelRenderer() {

    }

    @Override
    public void init(Context context, MetricsHolder metrics, PaintHolder paints) {
        this.paints = paints;

        this.spacing = (context.getResources().getDisplayMetrics().density * 30) * 1.5f;
        paddingHorz = 16 * context.getResources().getDisplayMetrics().density;
        paddingVert = 12 * context.getResources().getDisplayMetrics().density;
        radius = 16 * context.getResources().getDisplayMetrics().density;
    }

    public void draw(ActionButtonDataHolder dataHolder, Canvas canvas, boolean isSelected, float[] position) {
        LabelRenderHolder holder = renderData.get(dataHolder);
        if(holder == null) {
            holder = new LabelRenderHolder();
            renderData.put(dataHolder, holder);
        }

        holder.label = dataHolder.getAction().description;
        if (holder.selected != isSelected) {
            if (isSelected) {
                holder.animation.start();
            } else {
                holder.animation.reverse();
            }
            holder.selected = isSelected;
        }
        if (holder.animation.getAnimatedFraction() > 0) {
            float x = position[0];
            float y = position[1];
            canvas.save();

            paints.descriptionPaint.getTextBounds(holder.label, 0, holder.label.length(), tempRect);
            float pos = y - (spacing * holder.animation.getAnimatedFraction());
            float textHeight = tempRect.height();

            float textWidth = paints.descriptionPaint.measureText(holder.label);
            tempRectF.left = x - (textWidth / 2) - paddingHorz;
            tempRectF.top = pos - textHeight - paddingVert;
            tempRectF.right = x + (textWidth / 2) + paddingHorz;
            tempRectF.bottom = pos + paddingVert;

            canvas.scale(holder.animation.getAnimatedFraction(), holder.animation.getAnimatedFraction(), tempRectF.centerX(), tempRectF.centerY());

            canvas.drawRoundRect(tempRectF, radius, radius, paints.textBackgroundPaint);
            canvas.drawText(holder.label, x - (textWidth / 2), tempRectF.centerY() + (textHeight / 2), paints.descriptionPaint);
            canvas.restore();
        }
    }

    private static class LabelRenderHolder {
        public ValueAnimator animation = ValueAnimator.ofFloat(0, 1);
        public boolean selected = false;
        public String label;

        public LabelRenderHolder() {

        }
    }
}
