package com.sotrh.lwjgltext.render

import org.joml.Matrix4f

class TestOrthoRenderer {
    private val vao = TestOrthoVAO()
    private val shader = TestOrthoShader()
    private val projection = Matrix4f()

    fun ortho(width: Int, height: Int) {
        projection.identity().ortho(0f, width.toFloat(), height.toFloat(), 0f, 1f, -1f)
        shader.projection(projection)
    }

    fun bind() {
        vao.bind()
        shader.bind()
    }

    fun draw() {
        vao.draw()
    }

    fun unbind() {
        vao.unbind()
        shader.unbind()
    }

    fun cleanup() {
        vao.cleanup()
        shader.cleanup()
    }
}

