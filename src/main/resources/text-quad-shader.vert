#version 150 core

uniform vec3 u_position;
uniform vec2 u_scale;
uniform vec2 u_textureCoords[4];

in vec2 a_position;
in float a_textureCoordIndex;

out vec2 v_textureCoords;

void main() {
    gl_Position = vec4(
        a_position.x * u_scale.x + u_position.x,
        a_position.y * u_scale.y + u_position.y,
        u_position.z,
        1.0
    );

    v_textureCoords = u_textureCoords[int(a_textureCoordIndex)];
}