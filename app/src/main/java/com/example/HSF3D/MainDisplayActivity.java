package com.example.HSF3D;

import static com.example.myopen.Constant.port;
import static com.example.myopen.MyGLRenderer.angle;
import static com.example.myopen.MyGLRenderer.maxDeflection;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myopen.MyGLRenderer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class MainDisplayActivity extends Activity {

    private GLSurfaceView gLView;
    private boolean renderSet = false;//记录GLSurfaceView是否有效


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        gLView = new MyGLSurfaceView(this);
        setContentView(gLView);

        //Toast.makeText(this, "主线程仍然执行", Toast.LENGTH_SHORT).show();
        ConnectThread connectThread = new ConnectThread();
        connectThread.start();
    }

    class MyGLSurfaceView extends GLSurfaceView {

        private final MyGLRenderer renderer;

        public MyGLSurfaceView(Context context){
            super(context);

            // Create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2);

            renderer = new MyGLRenderer(context);

            // Set the Renderer for drawing on the GLSurfaceView
            setRenderer(renderer);
            renderSet = true;
        }
    }

    class ConnectThread extends Thread{
        Socket socket;

        @Override
        public void run() {
            try {

                // 1.新建ServerSocket对象，创建指定端口的连接
                ServerSocket serverSocket = new ServerSocket(port);

                // 2.进行监听
                socket = serverSocket.accept();// 开始监听10000端口，并接收到此套接字的连接。
                // 3.拿到输入流（客户端发送的信息就在这里）

                Log.i("a","连接成功");

                while (true)
                {
                    final byte[] buffer = new byte[1024];//创建接收缓冲区
                    InputStream inputStream = socket.getInputStream();
                    final int len = inputStream.read(buffer);//数据读出来，并且返回数据的长度

                    //读取四个数据
                    int num1 = 0;
                    int num2 = 0;
                    int num3 = 0;
                    int num4 = 0;
                    byte a = 48;
                    int i = 0;
                    if (i < len) {
                        //大矩形的长
                        for (; i < len && buffer[i] != ' '; ++i) {
                            num1 = num1 * 10 + buffer[i] - a;
                        }
                        ++i;
                        //大矩形的宽
                        for (; i < len && buffer[i] != ' '; ++i) {
                            num2 = num2 * 10 + buffer[i] - a;
                        }
                        //小矩形的长
                        ++i;
                        for (; i < len && buffer[i] != ' '; ++i) {
                            num3 = num3 * 10 + buffer[i] - a;
                        }
                        //小矩形的宽
                        ++i;
                        for (; i < len && buffer[i] != ' '; ++i) {
                            num4 = num4 * 10 + buffer[i] - a;
                        }


                        float nagle = (float) Math.toDegrees(
                                Math.atan((double) num4 / (double) num3));
                        float amxDeflection = (float) num2 / (float) num1 * 0.8f;

                        if (nagle > 35f || amxDeflection > 0.35f){
                            continue;
                        }else {
                            angle = nagle;
                            maxDeflection = amxDeflection;
                        }

                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
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