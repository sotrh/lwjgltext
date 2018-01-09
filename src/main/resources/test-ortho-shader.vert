#version 150 core

uniform mat4 u_projection;

in vec2 a_position;
in vec3 a_color;

out vec4 v_color;

void main() {
    gl_Position = u_projection * vec4(a_position, 0, 1);
    v_color = vec4(a_color, 1);
}