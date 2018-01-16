// Zmniejszenie domyślnego poziomu precyzji.
precision mediump float;

uniform sampler2D diffuseTexture;

// Zmienne przekazane z vertex shadera.
varying vec2 interpolatedTexCoord;

void main()
{
    // Przypisanie koloru fragmentowi obrazu.
    gl_FragColor = texture2D(diffuseTexture, interpolatedTexCoord);
}
