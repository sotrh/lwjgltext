#version 150 core

uniform vec3 u_position;
uniform vec2 u_scale;

in vec3 a_position;

void main() {
    gl_Position = vec4(
        a_position.x * u_scale.x + u_position.x,
        a_position.y * u_scale.y + u_position.y,
        a_position.z + u_position.z,
        1.0
    );
}
