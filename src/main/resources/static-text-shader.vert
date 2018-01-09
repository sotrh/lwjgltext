#version 150 core

uniform mat4 u_projection;

in vec2 a_position;
in vec2 a_texCoords;

out vec2 v_texCoords;

void main() {
    gl_Position = u_projection * vec4(a_position, 0, 1);
    v_texCoords = a_texCoords;
}