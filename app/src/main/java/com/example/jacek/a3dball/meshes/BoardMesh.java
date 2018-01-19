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

        final float[] texCoordData = {
                -width, height,
                -width, -height,
                width, height,
                -width, -height,
                width, -height,
                width, height,
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
        texCoordsBuffer = createBuffer(texCoordData);
        normalBuffer = createBuffer(normalData);
    }
}
