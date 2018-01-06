package com.sotrh.lwjgltext.render

import org.joml.Vector2f
import org.joml.Vector3f

class BoxRenderer {
    private val vao = BoxVAO()
    private val shader = BoxShader()

    fun bind() {
        vao.bind()
        shader.bind()
    }

    fun drawBox(position: Vector3f, scale: Vector2f, color: Vector3f) {
        shader.position(position)
        shader.scale(scale)
        shader.color(color)
        vao.draw()
    }

    fun unbind() {
        shader.unbind()
        vao.unbind()
    }

    fun destroy() {
        vao.destroy()
        shader.destroy()
    }
}