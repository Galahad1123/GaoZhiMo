package com.example.myopen;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;

import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;


import static com.example.myopen.Constant.max;
import static java.lang.Math.sqrt;

import com.myopen.android.objects.*;
import com.myopen.android.programs.ColorShaderProgram;
import com.myopen.android.util.Geometry;
import com.myopen.android.util.MatrixHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    //参数
    public static float angle = 0f;//最大偏转角
    public static float maxDeflection = 0f;//最大偏转量

    private final Context context;

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    private Cube cube;
    private CollapsedCube colCube;
    private Oblique oblique, oblique2;
    private Oblique Front, Behind;
    private CurvingCube curCube;

    private ColorShaderProgram colorProgram;


    //视图矩阵
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    private final float[] rotationMatrix = new float[16];

    private final float[] rotationOfCube = new float[16];
    private final float[] rotationOfReCube = new float[16];
    private final float[] rotationOfCube2 = new float[16];
    private final float[] rotationOfReCube2 = new float[16];
    private final float[] rotationOfCube3 = new float[16];


    public MyGLRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        //设置视口尺寸
        glViewport(0, 0, width, height);

        MatrixHelper.perspectiveM(projectionMatrix, 40,
                (float) width / (float) height, 1f, 10f);//45°视野，从z值-1到-10

        setLookAtM(viewMatrix, 0, 0f, 1f, 3f,
                0f, 0f, 0f, 0f, 1f, 0f);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        curCube = new CurvingCube(0.5f, 0.02f, angle, maxDeflection, 20);

        //Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);

        //旋转
        long time = SystemClock.uptimeMillis() % 4L;
        float angle = 0.090f * ((int) time);
        setRotateM(rotationMatrix, 0, angle, 0f, 1f, 0f);
        setRotateM(rotationOfCube, 0, 90f, 0f, 1f, 0f);
        setRotateM(rotationOfReCube, 0, 90f, 0f, -1f, 0f);
        setRotateM(rotationOfCube2, 0, 90f, 0f, 0f, 1f);
        setRotateM(rotationOfReCube2, 0, 90f, 0f, 0f, -1f);
        setRotateM(rotationOfCube3, 0, 180f, 1f, 0f, 0f);
        multiplyMM(viewMatrix, 0, viewMatrix, 0, rotationMatrix, 0);

        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        colorProgram.useProgram();


//        oblique
        {
            positionObjectInScene(0f, 0f, 0.2f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 0.9f);
            oblique.bindData(colorProgram);
            oblique.draw();

            positionObjectInScene(0f, 0f, 0.2f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 0.9f);
            oblique2.bindData(colorProgram);
            oblique2.draw();
        }

//        front & behind
        {
            positionObjectInScene(0f, 0.35f, 0.2f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
            Front.bindData(colorProgram);
            Front.draw();

            positionObjectInScene(0f, 0f, 0.2f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
            Front.bindData(colorProgram);
            Front.draw();

            positionObjectInScene(0f, -0.35f, 0.2f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
            Front.bindData(colorProgram);
            Front.draw();

//

            positionObjectInScene(0f, 0f, -0.2f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
            Behind.bindData(colorProgram);
            Behind.draw();

            positionObjectInScene(0f, -0.35f, -0.2f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
            Behind.bindData(colorProgram);
            Behind.draw();
        }

//        collapsed cube
        {
            multiplyMM(viewProjectionMatrix, 0, viewProjectionMatrix, 0, rotationOfCube, 0);

            positionObjectInScene(0f, 0f, -0.2f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 0.8f);
            colCube.bindData(colorProgram);
            colCube.draw();

            positionObjectInScene(0f, -0.35f, -0.2f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 0.8f);
            colCube.bindData(colorProgram);
            colCube.draw();

            positionObjectInScene(0f, 0.35f, -0.2f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 0.8f);
            colCube.bindData(colorProgram);
            colCube.draw();

            positionObjectInScene(0f, 0f, 0.2f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 0.8f);
            colCube.bindData(colorProgram);
            colCube.draw();

            positionObjectInScene(0f, 0.35f, 0.2f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 0.8f);
            colCube.bindData(colorProgram);
            colCube.draw();

            positionObjectInScene(0f, -0.35f, 0.2f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 0.8f);
            colCube.bindData(colorProgram);
            colCube.draw();
        }

//        cube
        {
            positionObjectInScene(0.2f, 0f, 0.2f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
            cube.bindData(colorProgram);
            cube.draw();

            positionObjectInScene(0.2f, 0f, -0.2f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
            cube.bindData(colorProgram);
            cube.draw();

            positionObjectInScene(-0.2f, 0f, 0.2f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
            cube.bindData(colorProgram);
            cube.draw();

            positionObjectInScene(-0.2f, 0f, -0.2f);
            colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
            cube.bindData(colorProgram);
            cube.draw();
        }
        multiplyMM(viewProjectionMatrix, 0, viewProjectionMatrix, 0, rotationOfReCube, 0);
        multiplyMM(viewProjectionMatrix, 0, viewProjectionMatrix, 0, rotationOfCube3, 0);

        {
            multiplyMM(viewProjectionMatrix, 0, viewProjectionMatrix, 0, rotationOfCube2, 0);

            positionObjectInScene(-0.35f, 0f, 0.2f);
            colorProgram.setUniforms(modelViewProjectionMatrix, getR(), getG(), 0f);
            curCube.bindData(colorProgram);
            curCube.draw();
            multiplyMM(viewProjectionMatrix, 0, viewProjectionMatrix, 0, rotationOfReCube2, 0);
        }

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0f, 0f, 0f, 0f);

        cube = new Cube(0.02f, 0.8f);
        colCube = new CollapsedCube(0.5f, 0.025f, 0f, 20);
        oblique = new Oblique(0.4f * (float) sqrt(5), 0.02f, 45f);
        oblique2 = new Oblique(0.4f * (float) sqrt(5), 0.02f, -45f);
        Front = new Oblique(0.5f, 0.025f, 0f);
        Behind = new Oblique(0.5f, 0.025f, 0f);

        colorProgram = new ColorShaderProgram(context);

    }

    private void positionObjectInScene(float x, float y, float z) {
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, x, y, z);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }

    private float getR(){
        if (angle <= max * 0.2f)return 0f;
        else if (angle <= max * 0.3f)return 0.25f;
        else if (angle <= max * 0.4f)return 0.5f;
        else if (angle <= max * 0.5f)return 0.75f;
        else return 1f;
    }

    private float getG(){
        if (angle <= max * 0.6f)return 1f;
        else if (angle <= max * 0.7f)return 0.75f;
        else if (angle <= max * 0.8f)return 0.5f;
        else if (angle <= max * 0.9f)return 0.25f;
        else return 0f;
    }
}