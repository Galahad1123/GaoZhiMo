package com.example.HSF3D;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.setRotateM;
import static android.opengl.Matrix.translateM;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

import com.example.myopen.MyGLRenderer2;
import com.myopen.android.objects.CollapsedCube;
import com.myopen.android.objects.Cube;
import com.myopen.android.programs.ColorShaderProgram;
import com.myopen.android.util.MatrixHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class MainDisplayActivity2 extends Activity {

    private GLSurfaceView gLView;
    private boolean renderSet = false;//记录GLSurfaceView是否有效

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gLView = new MyGLSurfaceView(this);
        setContentView(gLView);
    }

    class MyGLSurfaceView extends GLSurfaceView {

        private final MyGLRenderer2 renderer;

        public MyGLSurfaceView(Context context){
            super(context);

            // Create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2);

            renderer = new MyGLRenderer2(context);

            // Set the Renderer for drawing on the GLSurfaceView
            setRenderer(renderer);
            renderSet = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(renderSet)gLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(renderSet)gLView.onResume();
    }

}