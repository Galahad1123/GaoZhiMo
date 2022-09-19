package com.myopen.android.util;

import static android.opengl.GLES20.*;

import android.util.Log;

public class ShaderHelper {

    private static final String TAG = "ShaderHelper";

    public static int compileVertexShader(String shaderCode){
        return compileShader(GL_VERTEX_SHADER,shaderCode);
    }

    public static int compileFragmentShader(String shaderCode){
        return compileShader(GL_FRAGMENT_SHADER,shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        final int shaderObjectId = glCreateShader(type);//新建着色器对象

        if(shaderObjectId == 0){
            if(Loggerconfig.ON){
                Log.w(TAG, "Could not create new shader.");
            }
            return 0;
        }

        glShaderSource(shaderObjectId,shaderCode);//上传代码
        glCompileShader(shaderObjectId);//编译

        //检查编译状态
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

        //获取信息日志
        if(Loggerconfig.ON){
            //把日志输出到Android日志输出
            Log.v(TAG,"Results of compile source:" + '\n' + shaderCode +
                    "\n:" + glGetShaderInfoLog(shaderObjectId));
        }

        //验证编译状态
        if (compileStatus[0] == 0){
            //编译失败，删除着色器对象
            glDeleteShader(shaderObjectId);
            if(Loggerconfig.ON){
                Log.w(TAG, "Compilation of shader failed.");
            }
            return 0;
        }

        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId){
        final int programObjectId = glCreateProgram();//新建程序对象

        if(programObjectId == 0){
            if(Loggerconfig.ON){
                Log.w(TAG,"Could not create new program.");
            }
            return 0;
        }

        //附上着色器
        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);
        glLinkProgram(programObjectId);

        //检查编译状态
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);

        if (Loggerconfig.ON){
            Log.v(TAG,"Results of linking program:\n" +
                    glGetProgramInfoLog(programObjectId));
        }

        if(linkStatus[0] == 0){
            glDeleteProgram(programObjectId);
            if(Loggerconfig.ON){
                Log.w(TAG,"Linking of program failed.");
            }
            return 0;
        }

        return programObjectId;
    }

    public static boolean validateProgram(int projectObjectId){
        glValidateProgram(projectObjectId);

        final int[] validateStatus = new int[1];
        glGetProgramiv(projectObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
        Log.v(TAG, "Results of validating program:" + validateStatus[0] +
                "\nLog:" + glGetProgramInfoLog(projectObjectId));

        return validateStatus[0] != 0;
    }//验证程序是否有效

    public static int buildProgram(String vertexShaderSource, String fragmentShaderSource) {
        int program;

        //Compile the shaders
        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);

        //Link them into a shader program
        program = linkProgram(vertexShader, fragmentShader);

        if (Loggerconfig.ON)validateProgram(program);

        return program;
    }

}
