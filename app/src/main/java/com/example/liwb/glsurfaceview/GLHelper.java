package com.example.liwb.glsurfaceview;

import android.opengl.GLES20;

/**
 * Created by liwb on 2017/6/7.
 */

public class GLHelper {

    public static int loadProgram(String vertexSource, String  fragmentSource) {
        // Load the vertex shaders
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexSource);
        // Load the fragment shaders
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentSource);
        // Create the program object
        int program = GLES20.glCreateProgram();
        if (program == 0) {
            throw new RuntimeException("Error create program.");
        }
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        // Link the program
        GLES20.glLinkProgram(program);
        int[] linked = new int[1];
        // Check the link status
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linked, 0);
        if (linked[0] == 0) {
            GLES20.glDeleteProgram(program);
            throw new RuntimeException("Error linking program: " +
                    GLES20.glGetProgramInfoLog(program));
        }
        // Free up no longer needed shader resources
        GLES20.glDeleteShader(vertexShader);
        GLES20.glDeleteShader(fragmentShader);
        return program;
    }

    public static int loadShader(int shaderType, String source) {

        // Create the shader object
        int shader = GLES20.glCreateShader(shaderType);
        if (shader == 0) {
            throw new RuntimeException("Error create shader.");
        }
        int[] compiled = new int[1];
        // Load the shader source
        GLES20.glShaderSource(shader, source);
        // Compile the shader
        GLES20.glCompileShader(shader);
        // Check the compile status
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            GLES20.glDeleteShader(shader);
            throw new RuntimeException("Error compile shader: " +
                    GLES20.glGetShaderInfoLog(shader));
        }
        return shader;
    }



}
