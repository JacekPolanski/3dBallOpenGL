package com.example.jacek.a3dball.meshes;

/**
 @author Marek Kulawiak
 */

// Klasa reprezentująca kwadrat umieszczony w przestrzeni trójwymiarowej.
public class TexturedQuadMesh extends BaseMesh
{
    public TexturedQuadMesh()
    {
        final float[] positionData = {
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
             1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
             1.0f, -1.0f, 1.0f,
             1.0f, 1.0f, 1.0f
        };

        final float[] texCoordData = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
        };

        final float[] normalData = {
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f
        };

        numberOfVertices = 6;

        positionBuffer = createBuffer(positionData);
        texCoordsBuffer = createBuffer(texCoordData);
        normalBuffer = createBuffer(normalData);
    }
}
