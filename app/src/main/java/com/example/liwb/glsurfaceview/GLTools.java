package com.example.liwb.glsurfaceview;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

public class GLTools {
     static float ratio=1;
     static float screenWidth;
     static float screenHeight;
  public static void init(Context context){
     DisplayMetrics dm= context.getResources().getDisplayMetrics();
     screenWidth=dm.widthPixels;
     screenHeight=dm.heightPixels;
     ratio=1;
  }
  public static void init(float ViewWidth,float ViewHeight){
      screenWidth=ViewWidth;
      screenHeight=ViewHeight;
      ratio=1;
  }


    /**
     * Convert x to openGL
     *
     * @param x
     *            Screen x offset top left
     * @return Screen x offset top left in OpenGL
     */
    public static float toGLX(float x) {
        //return -1.0f * ratio + toGLWidth(x);
        return  2.0f * (x / screenWidth);
    }

    /**
     * Convert y to openGL y
     *
     * @param y
     *            Screen y offset top left
     * @return Screen y offset top left in OpenGL
     */
    public static float toGLY(float y) {
        return 0.0f - toGLHeight(y);
    }

    /**
     * Convert width to openGL width
     *
     * @param width
     * @return Width in openGL
     */
//    public static float toGLWidth(float width) {
//        return 2.0f * (width / screenWidth) * ratio;
//    }

    /**
     * Convert height to openGL height
     *
     * @param height
     * @return Height in openGL
     */
    public static float toGLHeight(float height) {
        return 2.0f * (height / screenHeight);
    }

    /**
     * Convert x to screen x
     *
     * @param glX
     *            openGL x
     * @return screen x
     */
//    public static float toScreenX(float glX) {
//        return toScreenWidth(glX - (-1 * ratio));
//    }

    /**
     * Convert y to screent y
     *
     * @param glY
     *            openGL y
     * @return screen y
     */
//    public static float toScreenY(float glY) {
//        return toScreenHeight(1.0f - glY);
//    }

    /**
     * Convert glWidth to screen width
     *
     * @param glWidth
     * @return Width in screen
     */
//    public static float toScreenWidth(float glWidth) {
//        return (glWidth * screenWidth) / (2.0f * ratio);
//    }

    /**
     * Convert height to screen height
     *
     * @param glHeight
     * @return Height in screen
     */
//    public static float toScreenHeight(float glHeight) {
//        return (glHeight * screenHeight) / 2.0f;
//    }

}
