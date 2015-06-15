package com.theguardian.ui;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;

public class DefaultPositionCalculator implements PositionCalculator {

    public void calcUsableRadius(RadialActionMenuView view, ActionButtonDataHolder[] activeAreas, PointF downPosition, MetricsHolder metrics) {
        int l = 0;
        int r = view.getMeasuredWidth();
        int t = view.getTop() + view.getPaddingTop();
        int b = t + view.getMeasuredHeight();

        // We scale the positioning radius to account for the animation bounce we apply
        float scaledPositioningRadius = metrics.positioningRadius * metrics.nudgeFactor;
        scaledPositioningRadius += metrics.circleRadius;

        // Calculate the maximum bounds of a circle around the down position
        int cl = (int) (downPosition.x - scaledPositioningRadius);
        int cr = (int) (downPosition.x + scaledPositioningRadius);
        int ct = (int) (downPosition.y - scaledPositioningRadius);
        int cb = (int) (downPosition.y + scaledPositioningRadius);

        // Work out if we overlap any edges
        boolean leftEdgeIntersected = l > cl && l < cr;
        boolean rightEdgeIntersected = r > cl && r < cr;
        boolean topEdgeIntersected = t < cb && t > ct;
        boolean bottomEdgeIntersected = b < cb && b > ct;

        // Make a count of the number of edges overlapped
        int edgesOverlapped = 0;
        if (leftEdgeIntersected) edgesOverlapped++;
        if (rightEdgeIntersected) edgesOverlapped++;
        if (topEdgeIntersected) edgesOverlapped++;
        if (bottomEdgeIntersected) edgesOverlapped++;

        // roughly calculate the ideal sweep width of a single action button
        float itemWidth = 2 * metrics.selectedActionButtonRadius;
        float idealWidth = (float) Math.toDegrees(Math.atan(itemWidth / metrics.positioningRadius));

        // Depending on the number of edges overlapped, we set a maximum sweep distance (to prevent
        // action buttons being positioned off -screen)
        float totalAngle;
        if (edgesOverlapped == 0) {
            totalAngle = Math.min(140f, idealWidth * activeAreas.length);
        } else if (edgesOverlapped == 1) {
            totalAngle = Math.min(140f, idealWidth * activeAreas.length);
        } else {
            int diff = 0;
            if (topEdgeIntersected) {
                diff = (int) Math.abs(downPosition.y - t);
            } else {
                diff = (int) Math.abs(downPosition.y - b);
            }
            float maxAngle = 85f;
            if (diff > metrics.selectedActionButtonRadius) {
                maxAngle = 90f;
            }
            totalAngle = Math.min(maxAngle, idealWidth * activeAreas.length);
        }

        float offsetAngle = 180;
        if (edgesOverlapped == 1) {
            if (leftEdgeIntersected) {
                offsetAngle = 270;
            }
            if (rightEdgeIntersected) {
                offsetAngle = 90;
            }
            if (topEdgeIntersected) {
                offsetAngle = 0;
            }
            if (bottomEdgeIntersected) {
                offsetAngle = 180;
            }
        } else if (edgesOverlapped == 2) {
            if (rightEdgeIntersected && bottomEdgeIntersected) {
                offsetAngle = 180;
            }
            if (leftEdgeIntersected && bottomEdgeIntersected) {
                offsetAngle = 270;
            }
            if (leftEdgeIntersected && topEdgeIntersected) {
                offsetAngle = 0;
            }
            if (rightEdgeIntersected && topEdgeIntersected) {
                offsetAngle = 90;
            }
        } else {
            offsetAngle = 180;
        }

        if (edgesOverlapped == 0 || (edgesOverlapped == 1 && (topEdgeIntersected || bottomEdgeIntersected))) {
            offsetAngle += (180 - totalAngle) / 2;
        } else if (edgesOverlapped == 1 && rightEdgeIntersected) {
            offsetAngle += (180 - totalAngle);
        } else if (edgesOverlapped == 2) {
            offsetAngle += (90 - totalAngle) / 2;
        }

        final PointF center = downPosition;
        float radius = edgesOverlapped == 2 ? metrics.positioningRadius + (metrics.positioningRadius * 0.2f) : metrics.positioningRadius;
        float startAngle = offsetAngle;
        float endAngle = totalAngle + offsetAngle;
        RectF area = new RectF(center.x - radius, center.y - radius, center.x + radius, center.y + radius);

        Path orbit = new Path();
        orbit.addArc(area, startAngle, endAngle - startAngle);

        PathMeasure measure = new PathMeasure(orbit, false);

        // Prevent overlapping when it is a full circle
        int divisor;
        if (Math.abs(endAngle - startAngle) >= 360 || activeAreas.length <= 1) {
            divisor = activeAreas.length;
        } else {
            divisor = activeAreas.length - 1;
        }

        // Measure this path, in order to find points that have the same distance between each other
        for (int i = 0; i < activeAreas.length; i++) {
            float[] coords = new float[]{0f, 0f};
            measure.getPosTan((i) * measure.getLength() / divisor, coords, null);
            // get the x and y values of these points and set them to each of sub action items.
            activeAreas[i].setPosition(coords[0], coords[1]);
        }
    }
}
