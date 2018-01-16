package com.example.jacek.a3dball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import com.example.jacek.a3dball.meshes.CubeMesh;
import com.example.jacek.a3dball.meshes.TexturedCubeMesh;
import com.example.jacek.a3dball.shaders.ShaderProgram;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GameRenderer implements GLSurfaceView.Renderer {

    // Macierze modelu, widoku i projekcji.
    private float[] modelMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] projectionMatrix = new float[16];
    private float[] rotationMatrix = new float[16];
    private float[] rotationXMatrix = new float[16];
    private float[] rotationYMatrix = new float[16];

    // Iloczyn macierzy modelu, widoku i projekcji.
    private float[] MVPMatrix = new float[16];
    private float[] MVMatrix = new float[16];

    // Informacja o tym, z ilu elementów składają się poszczególne atrybuty.
    private final int POSITION_DATA_SIZE = 3;
    private final int COLOUR_DATA_SIZE = 4;
    private final int NORMAL_DATA_SIZE = 3;
    private final int TEXCOORD_DATA_SIZE = 2;

    // Wartości wykorzystywane przez naszą kamerę. Pierwsze trzy elementy opisują położenie obserwatora,
    // kolejne trzy wskazują na punkt, na który on patrzy, a ostatnie wartości definiują, w którym kierunku
    // jest "góra" (tzw. "up vector").
    private float[] camera;

    private ShaderProgram colShaders;
    private ShaderProgram texShaders;

    // Kontekst aplikacji.
    private Context appContext = null;

    // Modele obiektów.
    private CubeMesh cubeMesh;
    private TexturedCubeMesh texturedCubeMesh;

    // Adresy tekstur w pamięci modułu graficznego.
    private int crateTextureDataHandle;

    private float xAngle = 0;
    private float yAngle = 0;

    private float ballScale = 0.5f;
    private float[] ballPosition;

    GameRenderer() {
        camera = new float[]{
                0.f, 0.f, 10.f, // pozycja obserwatora
                0.f, 0.f, 0.f,  // punkt na który obserwator patrzy
                0.f, 1.f, 0.f   // "up vector"
        };

        ballPosition = new float[] {
                0.f, // x
                0.f  // y
        };

        cubeMesh = new CubeMesh();
        texturedCubeMesh = new TexturedCubeMesh();
    }

    @Override
    // Stworzenie kontekstu graficznego.
    public void onSurfaceCreated(GL10 notUse, EGLConfig config) {
        // Kolor tła.
        GLES20.glClearColor(0.05f, 0.05f, 0.1f, 1.0f);

        // Ukrywanie wewnętrznych ścian.
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        // Właczenie sprawdzania głębokości.
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthMask(true);

        // Wczytanie tekstur do pamięci.
        crateTextureDataHandle = readTexture(R.drawable.crate_borysses_deviantart_com);

        // Utworzenie shaderów korzystających z kolorów wierzchołków.
        colShaders = new ShaderProgram();
        String[] colShadersAttributes = new String[]{"vertexPosition", "vertexColour", "vertexNormal"};
        colShaders.init(R.raw.col_vertex_shader, R.raw.col_fragment_shader,
                colShadersAttributes, appContext, "kolorowanie");

        // Utworzenie shaderów korzystających z tekstur.
        texShaders = new ShaderProgram();
        String[] texShadersAttributes = new String[]{"vertexPosition", "vertexTexCoord", "vertexNormal"};
        texShaders.init(R.raw.tex_vertex_shader, R.raw.tex_fragment_shader,
                texShadersAttributes, appContext, "teksturowanie");
    }

    @Override
    // Metoda wywoływana przy każdym przeskalowaniu okna.
    public void onSurfaceChanged(GL10 notUse, int width, int height) {
        Log.d("KSG", "Rozdzielczość: " + width + " x " + height);

        // Rozciągnięcie widoku OpenGL ES do rozmiarów ekranu.
        GLES20.glViewport(0, 0, width, height);

        // Przygotowanie macierzy projekcji perspektywicznej z uwzględnieniem Field of View.
        final float ratio = (float) width / height;
        final float fov = 60;
        final float near = 1.0f;
        final float far = 10000.0f;
        final float top = (float) (Math.tan(fov * Math.PI / 360.0f) * near);
        final float bottom = -top;
        final float left = ratio * bottom;
        final float right = ratio * top;
        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    // Metoda renderująca aktualną klatkę.
    public void onDrawFrame(GL10 notUse) {
        // Wyczyszczenie buforów głębi i kolorów.
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        // Ustawienie kamery.
        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.setLookAtM(viewMatrix, 0, camera[0], camera[1], camera[2], camera[3],
                camera[4], camera[5], camera[6], camera[7], camera[8]);

        // Transformacja i rysowanie brył.
        GLES20.glUseProgram(texShaders.programHandle); // Użycie shaderów korzystających z teksturowania.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0); // Wykorzystanie tekstury o indeksie 0.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, crateTextureDataHandle); // Podpięcie tekstury skrzyni.
        GLES20.glUniform1i(texShaders._diffuseTextureHandle, 0); // Przekazanie shaderom indeksu tekstury (0).

        Matrix.setIdentityM(modelMatrix, 0); // Zresetowanie pozycji modelu.
        Matrix.translateM(modelMatrix, 0, ballPosition[0], ballPosition[1], -1.0f); // Przesunięcie modelu.
        Matrix.scaleM(modelMatrix, 0, ballScale, ballScale, ballScale);

        Matrix.setRotateM(rotationXMatrix, 0, xAngle, -1.0f, 0, 0);
        Matrix.setRotateM(rotationYMatrix, 0, yAngle, 0, 1.0f, 0);
        Matrix.multiplyMM(rotationMatrix, 0, rotationXMatrix, 0, rotationYMatrix, 0);

        drawShape(texturedCubeMesh.getPositionBuffer(), null,
                texturedCubeMesh.getNormalBuffer(), texturedCubeMesh.getTexCoordsBuffer(),
                texShaders, texturedCubeMesh.getNumberOfVertices());
    }

    void setXAngle(float xAngle) {
        this.xAngle = xAngle;
    }

    void setYAngle(float yAngle) {
        this.yAngle = yAngle;
    }

    public void setBallPosition(float[] ballPosition) {
        this.ballPosition = ballPosition;
    }

    private void drawShape(final FloatBuffer positionBuffer, final FloatBuffer colourBuffer,
                           final FloatBuffer normalBuffer, final FloatBuffer texCoordsBuffer,
                           ShaderProgram shaderProgram, final int numberOfVertices) {
        if (positionBuffer == null) {
            return;
        }

        // Podpięcie bufora pozycji wierzchołków.
        positionBuffer.position(0);
        GLES20.glVertexAttribPointer(shaderProgram._vertexPositionHandle, POSITION_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, positionBuffer);
        GLES20.glEnableVertexAttribArray(shaderProgram._vertexPositionHandle);

        // Podpięcie buforów kolorów lub współrzędnych tekstury (w zależności od wykorzystanych shaderów).
        if (colourBuffer != null && shaderProgram._vertexColourHandle >= 0) {
            colourBuffer.position(0);
            GLES20.glVertexAttribPointer(shaderProgram._vertexColourHandle, COLOUR_DATA_SIZE,
                    GLES20.GL_FLOAT, false, 0, colourBuffer);
            GLES20.glEnableVertexAttribArray(shaderProgram._vertexColourHandle);
        } else if (texCoordsBuffer != null && shaderProgram._vertexTexCoordHandle >= 0) {
            texCoordsBuffer.position(0);
            GLES20.glVertexAttribPointer(shaderProgram._vertexTexCoordHandle, TEXCOORD_DATA_SIZE,
                    GLES20.GL_FLOAT, false, 0, texCoordsBuffer);
            GLES20.glEnableVertexAttribArray(shaderProgram._vertexTexCoordHandle);
        }

        // Podpięcie bufora normalnych.
        normalBuffer.position(0);
        GLES20.glVertexAttribPointer(shaderProgram._vertexNormalHandle, NORMAL_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, normalBuffer);
        GLES20.glEnableVertexAttribArray(shaderProgram._vertexNormalHandle);

        // Przemnożenie macierzy modelu, widoku i projekcji.
        Matrix.multiplyMM(MVMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(MVPMatrix, 0, projectionMatrix, 0, MVMatrix, 0);

        float[] scratch = new float[16];
        Matrix.multiplyMM(scratch, 0, MVPMatrix, 0, rotationMatrix, 0);

        // Przekazanie zmiennych uniform.
        GLES20.glUniformMatrix4fv(shaderProgram._MVPMatrixHandle, 1, false, scratch, 0);
        GLES20.glUniformMatrix4fv(shaderProgram._MVMatrixHandle, 1, false, MVMatrix, 0);

        // Narysowanie obiektu.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, numberOfVertices);
    }

    void setContext(Context context) {
        appContext = context;
    }

    // Metoda wczytująca teksturę z katalogu drawable.
    private int readTexture(int resourceId) {
        Log.d("KSG", "Wczytywanie tekstury.");
        final int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0); // Wygenerowanie tekstury i pobranie jej adresu.

        if (textureHandle[0] == 0) {
            Log.e("KSG", "Błąd przy wczytywaniu tekstury.");
            return -1;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false; // Wyłączenie skalowania.

        if (appContext == null) {
            Log.e("KSG", "appContext = null");
        }
        final Bitmap bitmap = BitmapFactory.decodeResource(appContext.getResources(), resourceId, options);
        Log.d("KSG", " bitmap resolution: " + bitmap.getWidth() + " x " + bitmap.getHeight());

        // Podpięcie tekstury.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

        // Ustawienie rodzaju filtrowania tekstury.
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        // Wczytanie zawartości bitmapy do tekstury.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        // Zwolnienie pamięci zajmowanej przez zmienną bitmap.
        bitmap.recycle();

        return textureHandle[0];
    }
}