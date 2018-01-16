// Zmienne globalne, przekazywane z programu głownego.
uniform mat4 MVPMatrix;
uniform mat4 MVMatrix;

// Atrybuty wierzchołka, przekazywane z programu głównego.
attribute vec4 vertexPosition;
attribute vec2 vertexTexCoord;
attribute vec3 vertexNormal;

// Zmienne stworzone przez vertex shader, dostępne we fragment shaderze w zinterpolowanej postaci.
varying vec2 interpolatedTexCoord;

varying vec3 interpolatedPosition;
varying vec3 interpolatedNormal;

void main()
{
    // Przekazanie współrzędnych tekstury wierzchołka do fragment shadera.
    interpolatedTexCoord = vertexTexCoord;

    // Wyliczenie położenia i normalnych wierzchołka w przestrzeni widoku.
    interpolatedPosition = vec3(MVMatrix * vertexPosition);
    interpolatedNormal = vec3(MVMatrix * vec4(vertexNormal, 0.0));

    // Wyznaczenie ostatecznej pozycji wierzchołka.
    gl_Position = MVPMatrix * vertexPosition;
}
