package com.example.jacek.a3dball.meshes;

public class BoardMesh extends BaseMesh {
    public BoardMesh(float width, float height) {
        final float[] positionData = {
                -width, height, 1.0f,
                -width, -height, 1.0f,
                width, height, 1.0f,
                -width, -height, 1.0f,
                width, -height, 1.0f,
                width, height, 1.0f,
        };

        final float[] colourData = {
                1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
        };

        final float[] normalData = {
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
        };

        numberOfVertices = 6;

        positionBuffer = createBuffer(positionData);
        colourBuffer = createBuffer(colourData);
        normalBuffer = createBuffer(normalData);
    }
}
