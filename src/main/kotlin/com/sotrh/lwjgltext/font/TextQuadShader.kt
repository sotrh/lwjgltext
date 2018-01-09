package com.sotrh.lwjgltext.font

import com.sotrh.lwjgltext.common.loadStringFromFile
import com.sotrh.lwjgltext.shader.*

class TextQuadShader : Shader(
        loadStringFromFile("text-quad-shader.vert"),
        loadStringFromFile("text-quad-shader.frag")
) {
    val scale = uniform2f(program, "u_scale")
    val color = uniform3f(program, "u_color")
    val position = uniform3f(program, "u_position")
    val textureCoords = uniform2fv(program, "u_textureCoords")
}