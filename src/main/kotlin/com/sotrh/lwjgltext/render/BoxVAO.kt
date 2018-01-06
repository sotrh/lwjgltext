package com.sotrh.lwjgltext.render

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30

class BoxVAO {
    var vao: Int = GL30.glGenVertexArrays(); private set
    var vbo: Int = GL15.glGenBuffers(); private set
    var ebo: Int = GL15.glGenBuffers(); private set
    var numElements: Int = 0; private set

    init {
        bind()

        val indexData = intArrayOf(
                0, 1, 2,
                0, 3, 2
        )
        val indexBuffer = BufferUtils.createIntBuffer(indexData.size)
        indexBuffer.put(indexData)
        indexBuffer.flip()
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW)
        numElements = indexData.size

        val vertexData = floatArrayOf(
                -0.5f, -0.5f,
                -0.5f, 0.5f,
                0.5f, 0.5f,
                0.5f, -0.5f
        )
        val vertexBuffer = BufferUtils.createFloatBuffer(vertexData.size)
        vertexBuffer.put(vertexData)
        vertexBuffer.flip()
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW)
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0)

        unbind()
    }

    fun draw() {
        GL11.glDrawElements(GL11.GL_TRIANGLES, numElements, GL11.GL_UNSIGNED_INT, 0L)
    }

    fun bind() {
        GL30.glBindVertexArray(vao)
        GL20.glEnableVertexAttribArray(0)
    }

    fun unbind() {
        GL20.glDisableVertexAttribArray(0)
        GL30.glBindVertexArray(0)
    }

    fun destroy() {
        GL30.glDeleteVertexArrays(vao)
        GL15.glDeleteBuffers(vbo)
        GL15.glDeleteBuffers(ebo)
        vao = 0
        vbo = 0
        ebo = 0
        numElements = 0
    }
}

