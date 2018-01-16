package com.example.jacek.a3dball.meshes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 @author Marek Kulawiak
 */

// Klasa reprezentująca pojedynczy model.
public abstract class BaseMesh
{
    // Rozmiar typu float w bajtach.
    protected final int BYTES_PER_FLOAT = 4;

    protected FloatBuffer positionBuffer=null;
    protected FloatBuffer colourBuffer=null;
    protected FloatBuffer normalBuffer=null;
    protected FloatBuffer texCoordsBuffer=null;
    protected int numberOfVertices;

    public FloatBuffer createBuffer(float[] data)
    {
        FloatBuffer buffer = ByteBuffer.allocateDirect(data.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        buffer.put(data).position(0);
        return buffer;
    }

    public FloatBuffer getPositionBuffer()
    {
        return positionBuffer;
    }

    public FloatBuffer getColourBuffer()
    {
        return colourBuffer;
    }

    public FloatBuffer getNormalBuffer()
    {
        return normalBuffer;
    }

    public FloatBuffer getTexCoordsBuffer()
    {
        return texCoordsBuffer;
    }

    public int getNumberOfVertices()
    {
        return numberOfVertices;
    }
}
