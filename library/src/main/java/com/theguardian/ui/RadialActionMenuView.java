package com.theguardian.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import java.util.List;

public class RadialActionMenuView extends FrameLayout {
    private static final String TAG = RadialActionMenuView.class.getSimpleName();

    private MetricsHolder metrics;
    private PaintHolder paints;

    private PointF downPosition = new PointF();
    private PointF currentPosition = new PointF();
    private int[] tempPositionArray = new int[2];

    private OnActionSelectedListener onActionSelectedListener;

    private boolean activated = false;

    private ActionButtonDataHolder[] activeAreas;
    private ActionButtonDataHolder selectedPoint;

    private PositionCalculator positionCalculator = new DefaultPositionCalculator();
    private ActionButtonRenderer renderer = new DefaultActionButtonRenderer();

    public RadialActionMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public RadialActionMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RadialActionMenuView(Context context) {
        super(context);
        init(null);
    }

    public void init(AttributeSet attrs) {
        metrics = new MetricsHolder(getContext(), attrs);
        paints = new PaintHolder(metrics);

        renderer.init(getContext(), metrics, paints);
    }

    public void snoopOnTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            midpoint(event, downPosition);
            midpoint(event, currentPosition);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            midpoint(event, currentPosition);
            if (activated) {
                ActionButtonDataHolder newSelectedPoint = selectedPoint();
                if (selectedPoint != newSelectedPoint) {
                    Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    if(v != null) {
                        try {
                            v.vibrate(60);
                        } catch(SecurityException ex) {
                            Log.e(TAG, "Need vibrate permission to vibrate");
                        }
                    }
                    selectedPoint = newSelectedPoint;
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
            if (activated) {
                actionUp();
            }
        }
    }

    private void actionUp() {
        activated = false;
        if (onActionSelectedListener != null) {
            onActionSelectedListener.onActionSelected(selectedPoint != null ? selectedPoint.getAction() : null);
            onActionSelectedListener = null;
        }
        selectedPoint = null;
        setWillNotDraw(true);
        invalidate();
    }


    private ActionButtonDataHolder selectedPoint() {
        ActionButtonDataHolder closest = null;
        for (int i = 0; i < activeAreas.length; i++) {
            ActionButtonDataHolder aa = activeAreas[i];
            if (closest == null || distanceBetween(currentPosition, closest.getPosition()) > distanceBetween(currentPosition, aa.getPosition())) {
                closest = aa;
            }
        }
        if (closest != null && !isPointOutsideCircle(currentPosition, closest.getPosition(), metrics.activeAreaSelectedRadius)) {
            return closest;
        }
        return null;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (activated) {
            snoopOnTouchEvent(ev);
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (activated) {
            snoopOnTouchEvent(event);
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (activated) {
            canvas.save();
            getLocationInWindow(tempPositionArray);
            canvas.translate(0, -tempPositionArray[1]);

            canvas.drawCircle(downPosition.x, downPosition.y, metrics.circleRadius, paints.circlePaint);

            if (activeAreas != null) {
                for (ActionButtonDataHolder area : activeAreas) {
                    if (selectedPoint != area) {
                        renderer.draw(area, canvas, false);
                    }
                }
            }

            if (selectedPoint != null) {
                renderer.draw(selectedPoint, canvas, true);
            }

            canvas.restore();
            invalidate();
        }
    }

    public void showMenu(OnActionSelectedListener listener, List<RadialActionMenuAction> items) {
        this.onActionSelectedListener = listener;

        // Send cancel event to view hierarchy
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        float x = 0.0f;
        float y = 0.0f;
        int metaState = 0;
        MotionEvent motionEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_CANCEL,
                x,
                y,
                metaState
        );
        getRootView().dispatchTouchEvent(motionEvent);

        activeAreas = new ActionButtonDataHolder[items.size()];
        for (int i = 0; i < activeAreas.length; i++) {
            PointF point = new PointF();
            activeAreas[i] = new ActionButtonDataHolder(items.get(i), point, downPosition);
        }

        activated = true;
        positionCalculator.calcUsableRadius(this, activeAreas, downPosition, metrics);
        setWillNotDraw(false);
        invalidate();
    }

    private void midpoint(MotionEvent event, PointF point) {
        point.x = event.getX();
        point.y = event.getY();
    }

    public boolean isPointOutsideCircle(PointF point, PointF circleCenter, float radius) {
        return !(Math.pow(point.x - circleCenter.x, 2) + Math.pow(point.y - circleCenter.y, 2) < (Math.pow(radius, 2)));
    }

    public float distanceBetween(PointF p1, PointF p2) {
        return (float) Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    public void setDescriptionTypeface(Typeface tf) {
        paints.descriptionPaint.setTypeface(tf);
    }

    public void setPositionCalculator(PositionCalculator calculator) {
        this.positionCalculator = calculator;
    }

    public void setActionButtonRenderer(ActionButtonRenderer renderer) {
        this.renderer = renderer;
        renderer.init(getContext(), metrics, paints);
    }

    public interface OnActionSelectedListener {
        void onActionSelected(RadialActionMenuAction actionSelected);
    }
}
