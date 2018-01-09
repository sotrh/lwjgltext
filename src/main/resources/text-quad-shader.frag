#version 150 core

uniform vec3 u_color;
uniform sampler2D u_texture;

in vec2 v_textureCoords;

out vec4 f_color;

void main() {
    f_color = vec4(u_color, 1) * texture(u_texture, v_textureCoords);
}
