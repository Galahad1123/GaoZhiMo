package com.example.myopen;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.setRotateM;
import static android.opengl.Matrix.translateM;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;

import com.myopen.android.objects.CollapsedCube;
import com.myopen.android.objects.Cube;
import com.myopen.android.programs.ColorShaderProgram;
import com.myopen.android.util.MatrixHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer2 implements GLSurfaceView.Renderer {

    private final Context context;

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    private Cube cube;
    private Cube cube2;
    private Cube cube3;
    private CollapsedCube colCube;

    private ColorShaderProgram colorProgram;


    //视图矩阵
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    private final float[] rotationMatrix = new float[16];

    private final float[] rotationOfCube = new float[16];
    private final float[] rotationOfCube2 = new float[16];


    public MyGLRenderer2(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        //设置视口尺寸
        glViewport(0, 0, width, height);

        MatrixHelper.perspectiveM(projectionMatrix, 50,
                (float) width / (float) height, 1f, 10f);//45°视野，从z值-1到-10

        setLookAtM(viewMatrix, 0, 0f, 1f, 3f,
                0f, 0f, 0f, 0f, 1f, 0f);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        //Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);

        //旋转
        long time = SystemClock.uptimeMillis() % 4L;
        float angle = 0.090f * ((int) time);
        setRotateM(rotationMatrix, 0, angle, 0f, 1f, 0f);
        setRotateM(rotationOfCube, 0, 90f, 0f, 0f, 1f);
        setRotateM(rotationOfCube2, 0, 90f, 1f, 0f, 0f);
        multiplyMM(viewMatrix, 0, viewMatrix, 0, rotationMatrix, 0);

        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        colorProgram.useProgram();

        {
            //Draw the cube
            positionObjectInScene(-0.2f, -0.5f, -0.2f);
            translateM(modelViewProjectionMatrix, 0, 0f, 0.5f, 0f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0.8f, 0f);
            cube.bindData(colorProgram);
            cube.draw();

            positionObjectInScene(0.2f, -0.5f, -0.2f);
            translateM(modelViewProjectionMatrix, 0, 0f, 0.5f, 0f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0.8f, 0f);
            cube.bindData(colorProgram);
            cube.draw();

            positionObjectInScene(-0.2f, -0.5f, 0.2f);
            translateM(modelViewProjectionMatrix, 0, 0f, 0.5f, 0f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 1f, 0.5f, 0f);
            cube.bindData(colorProgram);
            cube.draw();

            positionObjectInScene(0.2f, -0.5f, 0.2f);
            translateM(modelViewProjectionMatrix, 0, 0f, 0.5f, 0f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0.8f, 0f);
            cube.bindData(colorProgram);
            cube.draw();

            //Draw the collapsed cube
            positionObjectInScene(0f, -0.5f, -0.2f);
            translateM(modelViewProjectionMatrix, 0, 0f, 0.5f, 0f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.5f, 0.5f, 0.5f);
            colCube.bindData(colorProgram);
            colCube.draw();

            positionObjectInScene(0f, -0.5f, 0.2f);
            translateM(modelViewProjectionMatrix, 0, 0f, 0.5f, 0f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.5f, 0.5f, 0.5f);
            colCube.bindData(colorProgram);
            colCube.draw();

            //相隔较远的横向支架
            multiplyMM(viewProjectionMatrix, 0, viewProjectionMatrix, 0, rotationOfCube2, 0);

            positionObjectInScene(-0.7f, 0f, 0f);
            translateM(modelViewProjectionMatrix, 0, 0.5f, 0f, 0f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 0.9f);
            cube3.bindData(colorProgram);
            cube3.draw();

            positionObjectInScene(-0.3f, 0f, 0f);
            translateM(modelViewProjectionMatrix, 0, 0.5f, 0f, 0f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 0.9f);
            cube3.bindData(colorProgram);
            cube3.draw();

            multiplyMM(viewProjectionMatrix, 0, viewProjectionMatrix, 0, rotationOfCube, 0);

            positionObjectInScene(-0.7f, -0.4f, 0f);
            translateM(modelViewProjectionMatrix, 0, 0.5f, 0f, 0f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 0.9f);
            cube2.bindData(colorProgram);
            cube2.draw();

            positionObjectInScene(-0.7f, 0.4f, 0f);
            translateM(modelViewProjectionMatrix, 0, 0.5f, 0f, 0f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 0.9f);
            cube2.bindData(colorProgram);
            cube2.draw();

            positionObjectInScene(-0.3f, -0.4f, 0f);
            translateM(modelViewProjectionMatrix, 0, 0.5f, 0f, 0f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 0.9f);
            cube2.bindData(colorProgram);
            cube2.draw();

            positionObjectInScene(-0.3f, 0.4f, 0f);
            translateM(modelViewProjectionMatrix, 0, 0.5f, 0f, 0f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 0.9f);
            cube2.bindData(colorProgram);
            cube2.draw();
        }

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0f, 0f, 0f, 0f);

        cube = new Cube(0.05f, 1f);
        cube2 = new Cube(0.05f, 0.4f);
        cube3 = new Cube(0.05f, 1f);
        colCube = new CollapsedCube(0.4f, 0.08f, 0.04f, 10);

        colorProgram = new ColorShaderProgram(context);

    }

    private void positionObjectInScene(float x, float y, float z) {
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, x, y, z);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }
}

