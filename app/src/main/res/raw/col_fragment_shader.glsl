// Zmniejszenie domyślnego poziomu precyzji.
precision mediump float;

// Zmienne przekazane z vertex shadera.
varying vec4 interpolatedColour;

void main()
{
    vec3 lightPosition = vec3(0.0, 2.0, 1.0);
    vec4 lightColour = vec4(1.0, 1.0, 1.0, 1.0);

    // Przypisanie koloru fragmentowi obrazu.
    gl_FragColor = interpolatedColour;
}
