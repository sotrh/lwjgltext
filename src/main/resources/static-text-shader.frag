#version 150 core

uniform sampler2D u_textAtlas;
uniform vec3 u_color;

in vec2 v_texCoords;

out vec4 f_color;

void main() {
    f_color = vec4(u_color, 1) * texture(u_textAtlas, v_texCoords);
}