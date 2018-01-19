package com.example.jacek.a3dball;

import android.content.Context;
import android.opengl.GLSurfaceView;

class SurfaceView extends GLSurfaceView {
    private float ballTrianglesDensity = 30f;
    protected GameRenderer renderer = null;

    public SurfaceView(Context context) {
        super(context);

        // Stworzenie kontekstu OpenGL ES 2.0.
        setEGLContextClientVersion(2);

        // Przypisanie renderera do widoku.
        renderer = new GameRenderer();
        renderer.setContext(getContext());
        renderer.setBallTrianglesDensity(ballTrianglesDensity);
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

    public float getBallTrianglesDensity() {
        return renderer.getBallTrianglesDensity();
    }

    public float[] getWall1Position() {
        return renderer.getWall1Position();
    }

    public float[] getWall2Position() {
        return renderer.getWall2Position();
    }

    public void setCameraHeight(float cameraHeight) {
        renderer.setCameraHeight(cameraHeight);
    }

    public void flipFollowingCameraOnOff() {
        renderer.flipFollowingCameraOnOff();
    }

    public void setBallTrianglesDensity(float ballTrianglesDensity) {
        renderer.setNewBallTrianglesDensity(ballTrianglesDensity);
    }
}
