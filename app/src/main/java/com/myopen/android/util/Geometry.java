package com.myopen.android.util;

public class Geometry {
    public static class Point {
        public final float x, y, z;
        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point translateY(float distance) {
            return new Point(x, y + distance, z);
        }

        public Point translateZ(float distance) {
            return new Point(x, y, z + distance);
        }
    }

    public static class Circle {
        public final Point center;
        public final float radius;

        public Circle(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }

        public Circle scale(float scale) {
            return new Circle(center, radius * scale);
        }
    }

    public static class Cylinder {
        public final Point center;
        public final float radius;
        public final float height;

        public Cylinder(Point center, float radius, float height) {
            this.center = center;
            this.height = height;
            this.radius = radius;
        }
    }

    public static class CollapsedRectangle {
        public final Point center;
        public final float length;
        public final float width;
        public final float depth;

        public CollapsedRectangle(Point center, float length, float width, float depth) {
            this.center = center;
            this.length = length;
            this.width = width;
            this.depth = depth;
        }
    }

    public static class CollapsedCylinder {
        public final Point center;
        public final float length;
        public final float width;
        public final float depth;

        public CollapsedCylinder(Point center, float length, float width, float depth) {
            this.center = center;
            this.length  =length;
            this.width = width;
            this.depth = depth;
        }
    }

    public static class TiltedCylinder {
        public final Point center;
        public final float height;
        public final float width;
        public final double angle;

        public TiltedCylinder(Point center, float height, float width, double angle) {
            this.center = center;
            this.height = height;
            this.width = width;
            this.angle = angle;
        }
    }

    public static class CurvingCylinder {
        public final Point center;
        public final float length;
        public final float width;
        public final float angle;//端截面转角
        public final float maxDeflection;

        public CurvingCylinder
                (Point center, float length, float width, float angle, float maxDeflection) {
            this.center = center;
            this.length = length;
            this.width = width;
            this.angle = angle;
            this.maxDeflection = maxDeflection;
        }
    }

    public static class CurvingRectangle {
        public final Point center;
        public final float length;
        public final float width;
        public final float angle;
        public final float maxDeflection;

        public CurvingRectangle(Point center, float length, float width, float angle, float maxDeflection) {
            this.center = center;
            this.length = length;
            this.width = width;
            this.angle = angle;
            this.maxDeflection = maxDeflection;
        }
    }
}
