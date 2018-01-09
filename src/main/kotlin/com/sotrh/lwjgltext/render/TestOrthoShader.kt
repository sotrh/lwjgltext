package com.sotrh.lwjgltext.render

import com.sotrh.lwjgltext.common.loadStringFromFile
import com.sotrh.lwjgltext.shader.Shader
import com.sotrh.lwjgltext.shader.uniformMat4

class TestOrthoShader: Shader(
        loadStringFromFile("test-ortho-shader.vert"),
        loadStringFromFile("test-ortho-shader.frag")
) {
    val projection = uniformMat4(program, "u_projection")
}