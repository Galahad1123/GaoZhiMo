package com.myopen.android.objects;

import com.myopen.android.data.VertexArray;
import com.myopen.android.programs.ColorShaderProgram;
import com.myopen.android.util.Geometry;

import java.util.List;

public class Oblique {
    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float height, width;
    public final double angle;

    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public Oblique(float height, float width, double angle) {
        ObjectBuilder.GeneratedData generatedData =
                ObjectBuilder.createOblique(new Geometry.TiltedCylinder
                        (new Geometry.Point(0f, 0f, 0f), height, width, angle));

        this.height = height;
        this.width = width;
        this.angle = angle;

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
