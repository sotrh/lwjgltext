package com.sotrh.lwjgltext.render

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30

class TestOrthoVAO {
    private val vao = GL30.glGenVertexArrays()
    private val ebo = GL15.glGenBuffers()
    private val vbo = GL15.glGenBuffers()
    private val numElements: Int

    companion object {
        val POSTION_ATTRIBUTE = 0
        val COLOR_ATTRIBUTE = 1
    }

    init {
        bind()

        val elementData = intArrayOf(
                0, 1, 2,
                2, 1, 3
        )
        numElements = elementData.size
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, elementData, GL15.GL_STATIC_DRAW)

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatArrayOf(
                50f, 50f, 0f, 0f, 1f,
                50f, 100f, 0f, 1f, 0f,
                100f, 50f, 1f, 0f, 0f,
                100f, 100f, 0f, 0f, 0f
        ), GL15.GL_STATIC_DRAW)
        GL20.glVertexAttribPointer(POSTION_ATTRIBUTE, 2, GL11.GL_FLOAT, false, 5 * 4, 0L)
        GL20.glVertexAttribPointer(COLOR_ATTRIBUTE, 3, GL11.GL_FLOAT, false, 5 * 4, 2 * 4L)

        unbind()
    }

    fun bind() {
        GL30.glBindVertexArray(vao)
        GL20.glEnableVertexAttribArray(POSTION_ATTRIBUTE)
        GL20.glEnableVertexAttribArray(COLOR_ATTRIBUTE)
    }

    fun draw() {
        GL11.glDrawElements(GL11.GL_TRIANGLES, numElements, GL11.GL_UNSIGNED_INT, 0L)
    }

    fun unbind() {
        GL20.glDisableVertexAttribArray(POSTION_ATTRIBUTE)
        GL20.glDisableVertexAttribArray(COLOR_ATTRIBUTE)
        GL30.glBindVertexArray(0)
    }

    fun cleanup() {
        GL30.glDeleteVertexArrays(vao)
        GL15.glDeleteBuffers(vbo)
        GL15.glDeleteBuffers(ebo)
    }
}