package com.example.jacek.a3dball;

import android.content.Context;
import android.opengl.GLSurfaceView;

class SurfaceView extends GLSurfaceView {
    protected GameRenderer renderer = null;

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    public SurfaceView(Context context) {
        super(context);

        // Stworzenie kontekstu OpenGL ES 2.0.
        setEGLContextClientVersion(2);

        // Przypisanie renderera do widoku.
        renderer = new GameRenderer();
        renderer.setContext(getContext());
        setRenderer(renderer);

        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void setBallPosition(float x, float y) {
        x /= 100;
        y /= -100;

        renderer.setBallPosition(new float[] {x, y});
        renderer.setXAngle(y*100);
        renderer.setYAngle(x*100);
        requestRender();
    }
}
