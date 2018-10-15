package com.example.liwb.glsurfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity {
    private String TAG="glsurfaceview";
    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
GLTools.init(this);
        glSurfaceView=new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new GLSurfaceViewRender(this));
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        setContentView(glSurfaceView);



    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    class GLSurfaceViewRender implements GLSurfaceView.Renderer {
        Context context;
        private GLSurfaceViewRender(Context context){
            this.context=context;
        }
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            Log.i(TAG, "onSurfaceCreated");

            // 设置背景颜色
            gl.glClearColor(0.0f, 0f, 1f, 0.5f);

            GLES20.glEnable(GLES20.GL_TEXTURE_2D);
            // Active the texture unit 0
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            loadVertex();
            initShader();
            loadTexture();
            GLTools.init(this.context);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            // 设置输出屏幕大小
            gl.glViewport(0, 0, width, height);
            GLTools.init(width,height);
            Log.i(TAG, "onSurfaceChanged");
        }


        @Override
        public void onDrawFrame(GL10 gl) {
            Bitmap b=Bitmap.createBitmap(200,200, Bitmap.Config.ARGB_8888);
            Canvas canvas=new Canvas(b);

            Log.i(TAG, "onDrawFrame"+canvas.isHardwareAccelerated());
            // 清除屏幕和深度缓存(如果不调用该代码, 将不显示glClearColor设置的颜色)
            // 同样如果将该代码放到 onSurfaceCreated 中屏幕会一直闪动
            //gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

//            int mRed=100,mGreen=80,mBlue=100;
//            GLES20.glClearColor(mRed, mGreen, mBlue, 1.0f);
//            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT| GLES20.GL_DEPTH_BUFFER_BIT);

            //gl.glClearColor(mRed, mGreen, mBlue, 1.0f);
            //gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            // clear screen to black
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

            setMatrix(translate(GLTools.toGLX(1), 0f,0));
            if(matrix!=null){
                GLES20.glUniformMatrix4fv(hMatrix,1,false,matrix,0);
            }

            vertex.position(0);
// load the position
// 3(x , y , z)
// (2 + 3 )* 4 (float size) = 20
            GLES20.glVertexAttribPointer(attribPosition,
                    3, GLES20.GL_FLOAT,
                    false, 20, vertex);
            vertex.position(3);
// load the texture coordinate
            GLES20.glVertexAttribPointer(attribTexCoord,
                    2, GLES20.GL_FLOAT,
                    false, 20, vertex);
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT,
                    index);

        }

        private void loadVertex() {

            // float size = 4
            this.vertex = ByteBuffer.allocateDirect(quadVertex.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            this.vertex.put(quadVertex).position(0);
            // short size = 2
            this.index = ByteBuffer.allocateDirect(quadIndex.length * 2)
                    .order(ByteOrder.nativeOrder())
                    .asShortBuffer();
            this.index.put(quadIndex).position(0);
        }

        private void initShader() {

            String vertexSource = Tools.readFromAssets("VertexShaderMatrix.glsl");
            String fragmentSource = Tools.readFromAssets("FragmentShader.glsl");
            // Load the shaders and get a linked program
            int program =GLHelper.loadProgram(vertexSource, fragmentSource);
            // Get the attribute locations
            attribPosition = GLES20.glGetAttribLocation(program, "a_position");
            attribTexCoord = GLES20.glGetAttribLocation(program, "a_texCoord");
            hMatrix=GLES20.glGetUniformLocation(program,"u_MVPMatrix");
            int uniformTexture = GLES20.glGetUniformLocation(program,"u_samplerTexture");

            GLES20.glUseProgram(program);

            GLES20.glEnableVertexAttribArray(attribPosition);
            GLES20.glEnableVertexAttribArray(attribTexCoord);

            // Set the sampler to texture unit 0
            GLES20.glUniform1i(uniformTexture, 0);
        }





          int[] loadTexture() {

            int[] textureId = new int[1];
            // Generate a texture object
            GLES20.glGenTextures(1, textureId, 0);

            int[] result = null;
            if (textureId[0] != 0) {
                this.textureId=textureId[0];
                //InputStream is = Tools.readFromAsserts(path);
                Bitmap bitmap;
                try {
                    //bitmap = BitmapFactory.decodeStream(is);
                    bitmap=BitmapFactory.decodeResource(context.getResources(),R.drawable.testimage);
                } finally {
//                    try {
//                       // is.close();
//                    } catch (IOException e) {
//                        throw new RuntimeException("Error loading Bitmap.");
//                    }
                }
                result = new int[3];
                result[TEXTURE_ID] = textureId[0]; // TEXTURE_ID
                result[TEXTURE_WIDTH] = bitmap.getWidth(); // TEXTURE_WIDTH
                result[TEXTURE_HEIGHT] = bitmap.getHeight(); // TEXTURE_HEIGHT
                // Bind to the texture in OpenGL
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);
                // Set filtering
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                        GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                        GLES20.GL_NEAREST);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                        GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                        GLES20.GL_CLAMP_TO_EDGE);
                // Load the bitmap into the bound texture.
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

                // Recycle the bitmap, since its data has been loaded into OpenGL.
                bitmap.recycle();

            } else {
                throw new RuntimeException("Error loading texture.");
            }
            return result;
        }

        private float[] matrix;
        public void setMatrix(float[] matrix){
            this.matrix=matrix;
        }

        public float[] translate(float x,float y,float z){
             float[] mMatrixCurrent=     //原始矩阵
                    {1,0,0,0,
                            0,1,0,0,
                            0,0,1,0,
                            0,0,0,1};
            Matrix.translateM(mMatrixCurrent,0,x,y,z);
            return mMatrixCurrent;
        }

        private static final  int TEXTURE_ID=0;
        private static final  int TEXTURE_WIDTH=1;
        private static final int TEXTURE_HEIGHT=2;

        int attribPosition;
        int attribTexCoord;
        int hMatrix;
        private int textureId;
        private FloatBuffer vertex;
        private ShortBuffer index;
        //st(uv 用1-t是图像反了)反了图像会上下反着显示
        private float[] quadVertex = new float[] {
                -1.0f, 1.0f, 0.0f, // Position 0
                0, 1-1.0f, // TexCoord 0
                -1.0f, -1.0f, 0.0f, // Position 1
                0, 1-0f, // TexCoord 1
                1.0f , -1.0f, 0.0f, // Position 2
                1.0f, 1-0f, // TexCoord 2
                1.0f, 1.0f, 0.0f, // Position 3
                1.0f, 1-1.0f, // TexCoord 3
        };

        private short[] quadIndex = new short[] {
                (short)(0), // Position 0
                (short)(1), // Position 1
                (short)(2), // Position 2
                (short)(2), // Position 2
                (short)(3), // Position 3
                (short)(0), // Position 0
        };

    }

}
