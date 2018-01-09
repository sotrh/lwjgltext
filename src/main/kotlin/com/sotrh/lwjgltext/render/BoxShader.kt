package com.sotrh.lwjgltext.render

import com.sotrh.lwjgltext.common.loadStringFromFile
import com.sotrh.lwjgltext.shader.Shader
import com.sotrh.lwjgltext.shader.uniform2f
import com.sotrh.lwjgltext.shader.uniform3f

class BoxShader: Shader(loadStringFromFile("box-shader.vert"), loadStringFromFile("box-shader.frag")) {
    val scale = uniform2f(program, "u_scale")
    val color = uniform3f(program, "u_color")
    val position = uniform3f(program, "u_position")
}