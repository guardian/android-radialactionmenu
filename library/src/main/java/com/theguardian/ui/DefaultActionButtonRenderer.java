package com.theguardian.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.HashMap;

public class DefaultActionButtonRenderer extends BaseActionButtonRenderer {

    private ActionHolderRenderer renderer;
    private TextLabelRenderer labelRenderer;

    private HashMap<ActionButtonDataHolder, MeasureHolder> measureHolder = new HashMap<>();

    public DefaultActionButtonRenderer() {

    }

    @Override
    public void init(Context context, MetricsHolder metrics, PaintHolder paints) {
        super.init(context, metrics, paints);
        renderer = new ActionHolderRenderer(context, metrics, paints);
        labelRenderer = new DefaultTextLabelRenderer();
        labelRenderer.init(context, metrics, paints);
    }

    @Override
    public void draw(ActionButtonDataHolder dataHolder, Canvas canvas, boolean isSelected) {
        MeasureHolder holder = measureHolder.get(dataHolder);
        if (holder == null) {
            Matrix scaleMatrix = new Matrix();
            RectF rectF = new RectF();
            Path line = new Path();
            line.moveTo(dataHolder.getDownPosition().x, dataHolder.getDownPosition().y);
            line.lineTo(dataHolder.getPosition().x, dataHolder.getPosition().y);
            line.computeBounds(rectF, true);
            scaleMatrix.setScale(getMetrics().nudgeFactor, getMetrics().nudgeFactor, rectF.centerX(), rectF.centerY());
            line.transform(scaleMatrix);
            PathMeasure measure = new PathMeasure(line, false);
            float[] labelPosition = new float[2];
            holder = new MeasureHolder(measure, labelPosition);
            measure.getPosTan(measure.getLength(), labelPosition, null);
            measureHolder.put(dataHolder, holder);
        }
        renderer.draw(holder, dataHolder, canvas, isSelected, holder.path);
        labelRenderer.draw(dataHolder, canvas, isSelected, holder.labelPosition);
    }

    private static class ActionHolderRenderer {
        private Resources res;
        private String character;

        private MetricsHolder metrics;
        private PaintHolder paints;

        private Rect tempRect = new Rect();
        private float[] tempFloat = new float[2];

        private float textWidth;
        private float textHeight;

        public ActionHolderRenderer(Context context, MetricsHolder metrics, PaintHolder paints) {
            this.metrics = metrics;
            this.paints = paints;
            res = context.getResources();
        }

        public void draw(MeasureHolder measureHolder, ActionButtonDataHolder holder, Canvas canvas, boolean isSelected, PathMeasure measure) {
            RadialActionMenuAction action = holder.getAction();
            RadialActionMenuIcon icon = null;
            if(action instanceof IconFontRadialActionMenuAction) {
                character = String.valueOf(Character.toChars(res.getInteger(((IconFontRadialActionMenuAction)action).data.icon)));
                icon = (RadialActionMenuIcon) holder.getAction().data;
            } else {
                throw new ExpectedFontIconDataTypeException(action);
            }

            paints.iconPaint.setTypeface(icon.iconFont);
            paints.iconSelectedPaint.setTypeface(icon.iconFont);

            textWidth = paints.iconPaint.measureText(character);
            paints.iconPaint.getTextBounds(character, 0, character.length(), tempRect);
            textHeight = tempRect.height();


            if (measureHolder.selected != isSelected) {
                if (isSelected) {
                    measureHolder.nudgeAnimation.reverse();
                } else {
                    measureHolder.nudgeAnimation.start();
                }
                measureHolder.selected = isSelected;
            }

            canvas.save();

            float frac = (float) measureHolder.nudgeAnimation.getAnimatedValue();

            measureHolder.path.getPosTan((measureHolder.placementAnimation.getAnimatedFraction() + frac) * measure.getLength(), tempFloat, null);
            float posX = tempFloat[0];
            float posY = tempFloat[1];

            canvas.scale(measureHolder.placementAnimation.getAnimatedFraction(), measureHolder.placementAnimation.getAnimatedFraction(), posX, posY);
            if (!isSelected) {
                canvas.drawCircle(posX, posY, metrics.actionButtonRadius, paints.inactiveCirclePaintBg);
            }
            canvas.drawCircle(posX, posY, isSelected ? metrics.selectedActionButtonRadius : metrics.actionButtonRadius, isSelected ? paints.activeCirclePaint : paints.inactiveCirclePaint);
            canvas.drawText(character, posX - (textWidth / 2), posY + (textHeight / 2), isSelected ? paints.iconSelectedPaint : paints.iconPaint);

            canvas.restore();
        }
    }

    private static class MeasureHolder {
        public final PathMeasure path;
        public final float[] labelPosition;

        public ValueAnimator placementAnimation = ValueAnimator.ofFloat(0, 1f);
        public ValueAnimator nudgeAnimation = ValueAnimator.ofFloat(0f, -0.2f).setDuration(200);
        public boolean selected = false;

        private MeasureHolder(PathMeasure path, float[] labelPosition) {
            this.path = path;
            this.labelPosition = labelPosition;
            placementAnimation.start();
            nudgeAnimation.start();
        }
    }

}
