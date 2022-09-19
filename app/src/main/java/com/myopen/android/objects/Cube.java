package com.myopen.android.objects;

import com.myopen.android.data.VertexArray;
import com.myopen.android.programs.ColorShaderProgram;
import com.myopen.android.util.Geometry;

import java.util.List;

public class Cube {
    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float radius, height;

    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public Cube(float radius, float height) {
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createCube(
                new Geometry.Cylinder(new Geometry.Point(0f, 0f, 0f), radius, height));
        this.radius = radius;
        this.height = height;

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
