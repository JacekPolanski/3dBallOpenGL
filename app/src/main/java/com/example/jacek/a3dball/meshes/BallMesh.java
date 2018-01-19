package com.example.jacek.a3dball.meshes;

public class BallMesh extends BaseMesh {

    public void Draw(float step) {
        float angleA;
        float angleB;
        float cos, sin;
        float r1, r2;
        float h1, h2;
//        float step = 90.0f;

        final float[] positionData = new float[10000000];
        final float[] colourData = new float[]{};
        final float[] normalData = new float[]{};

        int n = 0;
        numberOfVertices = 0;
        for (angleA = -90.0f; angleA <= 90.0f; angleA += step) { // 3

            r1 = (float) Math.cos(angleA * Math.PI / 180.0);
            r2 = (float) Math.cos((angleA + step) * Math.PI / 180.0);
            h1 = (float) Math.sin(angleA * Math.PI / 180.0);
            h2 = (float) Math.sin((angleA + step) * Math.PI / 180.0);

            for (angleB = 0.0f; angleB <= 360.0f; angleB += step) { // 6

                cos = (float) Math.cos(angleB * Math.PI / 180.0);
                sin = -(float) Math.sin(angleB * Math.PI / 180.0);

                positionData[n] = (r2 * cos);
                positionData[n + 1] = (h2);
                positionData[n + 2] = (r2 * sin);

                positionData[n + 3] = (r1 * cos);
                positionData[n + 3 + 1] = (h1);
                positionData[n + 3 + 2] = (r1 * sin);

                n += 6;
                numberOfVertices += 2;
            }
        }

        positionBuffer = createBuffer(positionData);
        colourBuffer = createBuffer(positionData);
        normalBuffer = createBuffer(positionData);
    }
}
