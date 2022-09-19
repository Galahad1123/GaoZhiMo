package com.myopen.android.programs;

import static android.opengl.GLES20.glUseProgram;

import android.content.Context;

import com.myopen.android.util.ShaderHelper;
import com.myopen.android.util.TextResourceReader;

public class ShaderProgram {
    //Uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String U_COLOR = "u_Color";

    //Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";

    //Shader program
    protected final int program;
    protected ShaderProgram(Context context, int vertexShaderResourceId,
                            int fragmentShaderResourceId) {
        //Compile the shaders and link the program
        program = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId)
        );
    }

    public void useProgram() {
        //Set the current OpenGL shader program to this program
        glUseProgram(program);
    }
}
