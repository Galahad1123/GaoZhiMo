package com.myopen.android.objects;

import com.myopen.android.data.VertexArray;
import com.myopen.android.programs.ColorShaderProgram;
import com.myopen.android.util.Geometry;

import java.util.List;

public class CurvingCube {
    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float length, width, angle, maxDeflection;

    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public CurvingCube(float length, float width, float angle, float maxDeflection, int numPoints) {
        ObjectBuilder.GeneratedData generatedData =
                ObjectBuilder.createCurCube(new Geometry.CurvingCylinder(new Geometry.Point
                        (0f, 0f, 0f), length, width, angle, maxDeflection), numPoints);

        this.length = length;
        this.width = width;
        this.angle = angle;
        this.maxDeflection = maxDeflection;

        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;

    }

    public void bindData(ColorShaderProgram colorProgram) {
        vertexArray.setVertexAttribPointer(0,
                colorProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, 0);
    }

    public void draw() {
        for (ObjectBuilder.DrawCommand drawCommand : drawList) {
            drawCommand.draw();
        }
    }

}
