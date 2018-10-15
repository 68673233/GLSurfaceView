package com.example.liwb.glsurfaceview;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by liwb on 2017/6/7.
 */

public class Tools {

        public  static  String readFromAssets(String s){
            String tem;
            if (s.equals("VertexShader.glsl")){
                tem="uniform mat4 u_MVPMatrix;\n" +
                        "  \n" +
                        "attribute vec4 a_position;\n" +
                        "attribute vec2 a_texCoord;\n" +
                        "  \n" +
                        "varying vec2 v_texCoord;\n" +
                        "  \n" +
                        " void main() \n" +
                        " {\n" +
                        "    gl_Position = a_position;\n" +
                        "    v_texCoord  = a_texCoord;   \n" +
                        " }";
            }
            else if (s.equals("VertexShaderMatrix.glsl")){
                tem=    "attribute vec4 a_position;" +
                        "uniform mat4 u_MVPMatrix;"+
                        "attribute  vec2 a_texCoord;"+
                        "varying  vec2 v_texCoord;"+
                        "void main() {" +
                        "  gl_Position = u_MVPMatrix*a_position;" +
                        "  v_texCoord=a_texCoord;"+
                        "}";
            }
            else if (s.equals("FragmentShader.glsl")){
               tem="precision lowp float;       \n" +
                       "  \n" +
                       "varying vec2 v_texCoord;                       \n" +
                       "uniform sampler2D u_samplerTexture;\n" +
                       "  \n" +
                       "void main()                                          \n" +
                       "{                                                    \n" +
                       "  gl_FragColor = texture2D(u_samplerTexture, v_texCoord);\n" +
                       "}";
            }
            else    tem="";
            return tem;
        }

        public  static InputStream readFromAsserts(String pathFile){
            InputStream in=null;
            File f = new File(pathFile);
            if (f.exists())
                try {
                    in =new  FileInputStream(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            return in;
        }

}
