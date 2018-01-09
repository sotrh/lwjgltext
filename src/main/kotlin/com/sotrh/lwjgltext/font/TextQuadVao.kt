package com.sotrh.lwjgltext.font

import com.sotrh.lwjgltext.common.myAssert
import com.sotrh.lwjgltext.common.rangeUntil
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30

class TextQuadVao {
    companion object {
        val POSTION_ATTRIBUTE = 0
        val TEXCOORD_INDEX_ATTRIBUTE = 1
    }

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
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW)
        numElements = indexData.size

        val positionData = floatArrayOf(
                -0.5f, -0.5f, // 0
                -0.5f, 0.5f, // 3
                0.5f, 0.5f, // 2
                0.5f, -0.5f // 1
        )
        val textureCoordIndexData = intArrayOf(
                0, 3, 2, 1
        )
        val vertexBuffer = BufferUtils.createByteBuffer((positionData.size + textureCoordIndexData.size) * 4)
        rangeUntil(textureCoordIndexData.size).forEach {
            vertexBuffer.putFloat(positionData[0 + it * 2])
            vertexBuffer.putFloat(positionData[1 + it * 2])
            vertexBuffer.putInt(textureCoordIndexData[it])
        }
        vertexBuffer.flip()
        val stride = 3 * 4
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW)
        GL20.glVertexAttribPointer(POSTION_ATTRIBUTE, 2, GL11.GL_FLOAT, false,
                stride, 0L * 4L)
        GL20.glVertexAttribPointer(TEXCOORD_INDEX_ATTRIBUTE, 1, GL11.GL_INT, false,
                stride, 2L * 4L)

        unbind()
    }

    fun bind() {
        assertVaoValid()
        GL30.glBindVertexArray(vao)
        GL20.glEnableVertexAttribArray(POSTION_ATTRIBUTE)
        GL20.glEnableVertexAttribArray(TEXCOORD_INDEX_ATTRIBUTE)
    }

    fun draw() {
        GL11.glDrawElements(GL11.GL_TRIANGLES, numElements, GL11.GL_UNSIGNED_INT, 0L)
    }

    fun unbind() {
        assertVaoValid()
        GL30.glBindVertexArray(0)
        GL20.glDisableVertexAttribArray(POSTION_ATTRIBUTE)
        GL20.glDisableVertexAttribArray(TEXCOORD_INDEX_ATTRIBUTE)
    }

    fun cleanup() {
        assertVaoValid()
        GL30.glDeleteVertexArrays(vao)
        GL15.glDeleteBuffers(vbo)
        GL15.glDeleteBuffers(ebo)
        vao = 0
        vbo = 0
        ebo = 0
        numElements = 0
    }

    private fun assertVaoValid() {
        myAssert(vao != 0)
    }
}