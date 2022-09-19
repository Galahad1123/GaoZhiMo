package com.myopen.android.objects;

import com.myopen.android.data.VertexArray;
import com.myopen.android.programs.ColorShaderProgram;
import com.myopen.android.util.Geometry;

import java.util.List;

public class CollapsedCube {
    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float length, width, depth;

    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public CollapsedCube(float length, float width, float depth, int numPoints) {
        ObjectBuilder.GeneratedData generatedData =
                ObjectBuilder.createColCube(new Geometry.CollapsedCylinder
                (new Geometry.Point(0f, 0f, 0f), length, width, depth), numPoints);
        this.length = length;
        this.width = width;
        this.depth = depth;

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
