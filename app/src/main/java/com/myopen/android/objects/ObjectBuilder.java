package com.myopen.android.objects;

import static java.lang.Math.*;
import static android.opengl.GLES20.*;

import com.myopen.android.util.Geometry.*;

import java.util.ArrayList;
import java.util.List;

public class ObjectBuilder {

    static interface DrawCommand {
        void draw();
    }

    static class GeneratedData {
        final float[] vertexData;
        final List<DrawCommand> drawList;

        GeneratedData(float[] vertexData, List<DrawCommand> drawList) {
            this.vertexData = vertexData;
            this.drawList = drawList;
        }
    }

    private static final int FLOAT_PER_VERTEX = 3;
    private final float[] vertexData;
    private int offset = 0;

    private final List<DrawCommand> drawList = new ArrayList<DrawCommand>();

    private ObjectBuilder(int sizeInVertices) {
        vertexData = new float[sizeInVertices * FLOAT_PER_VERTEX];
    }

    //计算圆柱体顶部顶点数量
    private static int sizeOfCircleInVertices(int numPoints) {
        return 1 + (numPoints + 1);
    }

    //计算圆柱体侧面顶点数量
    private static int sizeOfOpenCylinderInVertices(int numPoints) {
        return (numPoints + 1) * 2;
    }

    private static int sizeOfColRectangleInVertices(int numPoints) {
        return numPoints * 2;
    }

    private static int sizeOfColCylinderInVertices(int numPoints) {
        return (numPoints * 2 + 1) * 2;
    }

    private static int sizeOfTilCylinderInVertices() {
        return sizeOfOpenCylinderInVertices(4);
    }

    private static int sizeOfCurCylinderInVertices(int numPoints) {
        return (numPoints * 2 + 1) * 2;
    }

    private static int sizeOfCurRectangleInVertices(int numPoints) {
        return numPoints * 2;
    }

    static GeneratedData createCube(Cylinder puck) {
        int numPoints = 4;
        //所有顶点数
        int size = sizeOfCircleInVertices(numPoints) + sizeOfOpenCylinderInVertices(numPoints);

        ObjectBuilder builder = new ObjectBuilder(size);

        Circle puckTop = new Circle(
                puck.center.translateY(puck.height / 2f),
                puck.radius
        );

        //计算位置并创建
        builder.appendCircle(puckTop, numPoints);
        builder.appendOpenCylinder(puck, numPoints);

        return builder.build();
    }

    static GeneratedData createColCube(CollapsedCylinder colCylinder, int numPoints) {
        int size = sizeOfColCylinderInVertices(numPoints) +
                sizeOfColRectangleInVertices(numPoints) * 2;

        ObjectBuilder builder = new ObjectBuilder(size);

        CollapsedRectangle colCubeTop = new CollapsedRectangle(
                colCylinder.center.translateY(colCylinder.width / 2f),
                colCylinder.length,
                colCylinder.width,
                colCylinder.depth
        );

        CollapsedRectangle colCubeBottom = new CollapsedRectangle(
                colCylinder.center.translateY(-colCylinder.width / 2f),
                colCylinder.length,
                colCylinder.width,
                colCylinder.depth
        );

        builder.appendColRectangle(colCubeTop, numPoints);
        builder.appendColRectangle(colCubeBottom, numPoints);
        builder.appendColCylinder(colCylinder, numPoints);

        return builder.build();
    }

    static GeneratedData createOblique(TiltedCylinder tilCylinder) {
        int size = sizeOfTilCylinderInVertices();

        ObjectBuilder builder = new ObjectBuilder(size);

        builder.appendTilCylinder(tilCylinder);
        return builder.build();
    }

    static GeneratedData createCurCube(CurvingCylinder curCylinder, int numPoints) {
        int size = sizeOfCurCylinderInVertices(numPoints)
                + sizeOfCurRectangleInVertices(numPoints) * 2;

        ObjectBuilder builder = new ObjectBuilder(size);

        CurvingRectangle front = new CurvingRectangle(
                curCylinder.center.translateZ(curCylinder.width / 2f),
                curCylinder.length,
                curCylinder.width,
                curCylinder.angle,
                curCylinder.maxDeflection
        );

        CurvingRectangle behind = new CurvingRectangle(
                curCylinder.center.translateZ(-curCylinder.width / 2f),
                curCylinder.length,
                curCylinder.width,
                curCylinder.angle,
                curCylinder.maxDeflection
        );

        builder.appendCurCylinder(curCylinder, numPoints);
        builder.appendCurRectangle(front, numPoints);
        builder.appendCurRectangle(behind, numPoints);
        return builder.build();
    }

    private void appendCircle(Circle circle, int numPoints) {
        final int startVertex = offset / FLOAT_PER_VERTEX;
        final int numVertices = sizeOfCircleInVertices(numPoints);

        //Center point of fan
        vertexData[offset++] = circle.center.x;
        vertexData[offset++] = circle.center.y;
        vertexData[offset++] = circle.center.z;

        //Fan around center point <= is used because we want to generate
        //the point at the starting angle twice to complete the fan.
        for (int i = 0; i <= numPoints; ++i) {
            float angleInRadians = ((float) i / (float) numPoints) * ((float) Math.PI * 2f);

            vertexData[offset++] = circle.center.x + circle.radius * (float) cos(angleInRadians);
            vertexData[offset++] = circle.center.y;
            vertexData[offset++] = circle.center.z + circle.radius * (float) sin(angleInRadians);
        }

        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices);
            }
        });
    }

    private void appendOpenCylinder(Cylinder cylinder, int numPoints){
        final int startVertex = offset / FLOAT_PER_VERTEX;
        final int numVertices = sizeOfOpenCylinderInVertices(numPoints);
        final float yStart = cylinder.center.y - (cylinder.height / 2f);
        final float yEnd = cylinder.center.y + (cylinder.height / 2f);

        for (int i = 0; i <= numPoints; ++i) {
            float angleInRadians = ((float) i / (float) numPoints) * ((float) PI * 2f);

            float xPosition = cylinder.center.x + cylinder.radius * (float) cos(angleInRadians);
            float zPosition = cylinder.center.z + cylinder.radius * (float) sin(angleInRadians);

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yStart;
            vertexData[offset++] = zPosition;

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yEnd;
            vertexData[offset++] = zPosition;
        }

        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices);
            }
        });
    }

    private void appendColRectangle(CollapsedRectangle colRectangle, int numPoints) {
        final int startVertex = offset / FLOAT_PER_VERTEX;
        final int numVertices = sizeOfColRectangleInVertices(numPoints);
        final float zStart = colRectangle.center.z + (colRectangle.width / 2f);
        final float zEnd = colRectangle.center.z - (colRectangle.width / 2f);

        for (int i = 0; i < numPoints; ++i) {
            float xPosition = colRectangle.center.x - colRectangle.length / 2f
                    + colRectangle.length * i / (numPoints - 1);
            float yPosition = colRectangle.center.y - colRectangle.depth +
                    (float) pow((double) 2 * i / (double) (numPoints - 1) - 1, 2) * colRectangle.depth;

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yPosition;
            vertexData[offset++] = zStart;

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yPosition;
            vertexData[offset++] = zEnd;
        }

        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices);
            }
        });
    }

    private void appendColCylinder(CollapsedCylinder colCylinder, int numPoints) {
        final int startVertex = offset / FLOAT_PER_VERTEX;
        final int numVertices = sizeOfColCylinderInVertices(numPoints);
        final float zFront = colCylinder.center.z + colCylinder.width / 2f;
        final float zBehind = colCylinder.center.z - colCylinder.width / 2f;
        final float xStart = colCylinder.center.x - colCylinder.length / 2f;

        for (int i = 0; i < numPoints; ++i) {
            float xPosition = xStart + colCylinder.length * i / (numPoints - 1);
            float yPosition = colCylinder.center.y - colCylinder.depth +
                    (float) pow((double) 2 * i / (double) (numPoints - 1) - 1, 2) * colCylinder.depth;

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yPosition + colCylinder.width / 2f;
            vertexData[offset++] = zFront;

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yPosition - colCylinder.width / 2f;
            vertexData[offset++] = zFront;
        }

        for (int i = numPoints - 1; i >= 0; --i) {
            float xPosition = xStart + colCylinder.length * i / (numPoints - 1);
            float yPosition = colCylinder.center.y - colCylinder.depth +
                    (float) pow((double) 2 * i / (double) (numPoints - 1) - 1, 2) * colCylinder.depth;

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yPosition + colCylinder.width / 2f;
            vertexData[offset++] = zBehind;

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yPosition - colCylinder.width / 2f;
            vertexData[offset++] = zBehind;
        }

        vertexData[offset++] = xStart;
        vertexData[offset++] = colCylinder.center.y + colCylinder.width / 2f;
        vertexData[offset++] = zFront;

        vertexData[offset++] = xStart;
        vertexData[offset++] = colCylinder.center.y - colCylinder.width / 2f;
        vertexData[offset++] = zFront;

        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices);
            }
        });
    }

    private void appendTilCylinder(TiltedCylinder tilCylinder) {
        final int startVertex = offset / FLOAT_PER_VERTEX;
        final int numVertices = sizeOfTilCylinderInVertices();
        float x = tilCylinder.center.x;
        float y = tilCylinder.center.y;
        float height = tilCylinder.height;
        float width = tilCylinder.width;
        float zFront = tilCylinder.center.z + width / 2f;
        float zBehind = zFront - width;
        float mCos = (float) cos(tilCylinder.angle);
        float mSin = (float) sin(tilCylinder.angle);

        float x1 = x + mCos * height / 2f - mSin * width / 2f;
        float y1 = y + mSin * height / 2f + mCos * width / 2f;
        float x2 = x - mCos * height / 2f - mSin * width / 2f;
        float y2 = y - mSin * height / 2f + mCos * width / 2f;
        float x3 = x + mCos * height / 2f + mSin * width / 2f;
        float y3 = y + mSin * height / 2f - mCos * width / 2f;
        float x4 = x - mCos * height / 2f + mSin * width / 2f;
        float y4 = y - mSin * height / 2f - mCos * width / 2f;

        vertexData[offset++] = x1;
        vertexData[offset++] = y1;
        vertexData[offset++] = zFront;

        vertexData[offset++] = x2;
        vertexData[offset++] = y2;
        vertexData[offset++] = zFront;

        vertexData[offset++] = x3;
        vertexData[offset++] = y3;
        vertexData[offset++] = zFront;

        vertexData[offset++] = x4;
        vertexData[offset++] = y4;
        vertexData[offset++] = zFront;

        vertexData[offset++] = x3;
        vertexData[offset++] = y3;
        vertexData[offset++] = zBehind;

        vertexData[offset++] = x4;
        vertexData[offset++] = y4;
        vertexData[offset++] = zBehind;

        vertexData[offset++] = x1;
        vertexData[offset++] = y1;
        vertexData[offset++] = zBehind;

        vertexData[offset++] = x2;
        vertexData[offset++] = y2;
        vertexData[offset++] = zBehind;

        vertexData[offset++] = x1;
        vertexData[offset++] = y1;
        vertexData[offset++] = zFront;

        vertexData[offset++] = x2;
        vertexData[offset++] = y2;
        vertexData[offset++] = zFront;

        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices);
            }
        });

    }

    private void appendCurCylinder(CurvingCylinder curCylinder, int numPoints) {
        final int startVertex = offset / FLOAT_PER_VERTEX;
        final int numVertices = sizeOfCurCylinderInVertices(numPoints);

        final float zFront = curCylinder.center.z + curCylinder.width / 2f;
        final float zBehind = curCylinder.center.z - curCylinder.width / 2f;
        final float angle = curCylinder.angle / 180f * (float) PI * 2;

        float xFirst = curCylinder.center.x - curCylinder.width / 2f;
        float yFirst = curCylinder.center.y - curCylinder.length / 2f;
        for (int i = 0; i < numPoints; ++i) {
            final float y = (float) i / (float) (numPoints - 1) * curCylinder.length;
            final float x = angle * (3 * curCylinder.length - y) * y * y
                    / 3f / (float) pow(curCylinder.length, 2);

            vertexData[offset++] = xFirst + x;
            vertexData[offset++] = yFirst + y;
            vertexData[offset++] = zFront;

            vertexData[offset++] = xFirst + x;
            vertexData[offset++] = yFirst + y;
            vertexData[offset++] = zBehind;
        }

        for (int i = numPoints - 1; i >= 0; --i) {
            final float y = (float) i / (float) (numPoints - 1) * curCylinder.length;
            final float x = angle * (3 * curCylinder.length - y) * y * y
                    / 3f / (float) pow(curCylinder.length, 2);

            vertexData[offset++] = curCylinder.center.x + curCylinder.width / 2f + x;
            vertexData[offset++] = yFirst + y;
            vertexData[offset++] = zFront;

            vertexData[offset++] = curCylinder.center.x + curCylinder.width / 2f + x;
            vertexData[offset++] = yFirst + y;
            vertexData[offset++] = zBehind;
        }

        vertexData[offset++] = xFirst;
        vertexData[offset++] = yFirst;
        vertexData[offset++] = zFront;

        vertexData[offset++] = xFirst;
        vertexData[offset++] = yFirst;
        vertexData[offset++] = zBehind;

        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices);
            }
        });

    }

    private void appendCurRectangle(CurvingRectangle curRectangle, int numPoints) {
        final int startVertex = offset / FLOAT_PER_VERTEX;
        final int numVertices = sizeOfCurRectangleInVertices(numPoints);

        final float angle = curRectangle.angle / 180f * (float) PI * 2;

        for (int i = 0; i < numPoints; ++i) {
            final float y = (float) i / (float) (numPoints - 1) * curRectangle.length;
            final float x = angle * (3 * curRectangle.length - y) * y * y
                    / 3f / (float) pow(curRectangle.length, 2);
            float Y = curRectangle.center.y - curRectangle.length / 2f + y;

            vertexData[offset++] = curRectangle.center.x - curRectangle.width / 2f + x;
            vertexData[offset++] = Y;
            vertexData[offset++] = curRectangle.center.z;

            vertexData[offset++] = curRectangle.center.x + curRectangle.width / 2f + x;
            vertexData[offset++] = Y;
            vertexData[offset++] = curRectangle.center.z;
        }

        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices);
            }
        });

    }

    private GeneratedData build() {
        return new GeneratedData(vertexData, drawList);
    }
}
