package com.sotrh.lwjgltext.render

import com.sotrh.lwjgltext.common.loadStringFromFile
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.GL20

class BoxShader: Shader(loadStringFromFile("box-shader.vert"), loadStringFromFile("box-shader.frag")) {
    private val colorUniform = GL20.glGetUniformLocation(program, "u_color")
    private val scaleUniform = GL20.glGetUniformLocation(program, "u_scale")
    private val positionUniform = GL20.glGetUniformLocation(program, "u_position")

    fun scale(scale: Vector2f) {
        assertBound()
        GL20.glUniform2f(scaleUniform, scale.x, scale.y)
    }

    fun color(color: Vector3f) {
        assertBound()
        GL20.glUniform3f(colorUniform, color.x, color.y, color.z)
    }

    fun position(position: Vector3f) {
        assertBound()
        GL20.glUniform3f(positionUniform, position.x, position.y, position.z)
    }
}