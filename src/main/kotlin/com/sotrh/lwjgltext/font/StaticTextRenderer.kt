package com.sotrh.lwjgltext.font

import com.sotrh.lwjgltext.common.loadStringFromFile
import com.sotrh.lwjgltext.common.rangeUntil
import com.sotrh.lwjgltext.shader.Shader
import com.sotrh.lwjgltext.shader.uniform3f
import com.sotrh.lwjgltext.shader.uniformMat4
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30

class StaticTextRenderer(val font: BitmapFont) {

    private val shader = StaticTextShader()
    private val projection = Matrix4f()

    fun ortho(width: Int, height: Int) {
        shader.bind()
        projection.identity().ortho(0f, width.toFloat(), height.toFloat(), 0f, 1f, -1f)
        shader.projection(projection)
        shader.unbind()
    }

    fun createStaticTextVao(x: Float, y: Float, text: String): StaticTextVao {
        val cursor = Vector2f(x, y)
        val staticTextQuads = mutableListOf<StaticTextQuad>()

        val scaleW = font.common.scaleW.toFloat()
        val scaleH = font.common.scaleH.toFloat()

        text.forEach {
            val char = font.charMap[it.toInt()]!!

            if (char.width > 0 && char.height > 0) {
                val src = Vector4f()
                src.x = char.x / scaleW
                src.y = char.y / scaleH
                src.z = (char.x + char.width) / scaleW
                src.w = (char.y + char.height) / scaleH
                val dst = Vector4f()
                dst.x = cursor.x + char.xoffset
                dst.y = cursor.y + char.yoffset
                dst.z = dst.x + char.width
                dst.w = dst.y + char.height
                staticTextQuads.add(StaticTextQuad(src, dst))
            }

            if (it == '\n') {
                cursor.x = x
                cursor.y += font.common.lineHeight
            } else {
                cursor.x += char.xadvance
            }
        }

        return StaticTextVao(staticTextQuads)
    }

    fun bind() {
        shader.bind()
        font.pageArray[0].texture.bind(0)
    }

    fun draw(staticTextVao: StaticTextVao, color: Vector3f) {
        shader.color(color)
        staticTextVao.bind()
        staticTextVao.draw()
        staticTextVao.unbind()
    }

    fun unbind() {
        shader.unbind()
    }

}

class StaticTextQuad(
        val src: Vector4f,
        val dst: Vector4f
)

class StaticTextVao(staticTextQuads: List<StaticTextQuad>) {
    private val vao = GL30.glGenVertexArrays()
    private val ebo = GL15.glGenBuffers()
    private val vbo = GL15.glGenBuffers()
    private val numElements: Int

    companion object {
        val POSITION_ATTRIBUTE = 0
        val TEXCOORD_ATTRIBUTE = 1
    }

    init {
        val vertexBuffer = BufferUtils.createFloatBuffer(staticTextQuads.size * 16)
        val indexBuffer = BufferUtils.createIntBuffer(staticTextQuads.size * 6)

        numElements = indexBuffer.capacity()

        staticTextQuads.forEachIndexed { index, quad ->
            // top, left
            vertexBuffer.put(quad.dst.x).put(quad.dst.y)
            vertexBuffer.put(quad.src.x).put(quad.src.y)
            // bottom, left
            vertexBuffer.put(quad.dst.x).put(quad.dst.w)
            vertexBuffer.put(quad.src.x).put(quad.src.w)
            // bottom, right
            vertexBuffer.put(quad.dst.z).put(quad.dst.w)
            vertexBuffer.put(quad.src.z).put(quad.src.w)
            // top, right
            vertexBuffer.put(quad.dst.z).put(quad.dst.y)
            vertexBuffer.put(quad.src.z).put(quad.src.y)

            indexBuffer.put(index * 4 + 0).put(index * 4 + 1).put(index * 4 + 2)
            indexBuffer.put(index * 4 + 2).put(index * 4 + 0).put(index * 4 + 3)
        }

        vertexBuffer.flip()
        indexBuffer.flip()

        GL30.glBindVertexArray(vao)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW)
        GL20.glVertexAttribPointer(POSITION_ATTRIBUTE, 2, GL11.GL_FLOAT, false, 16, 0L)
        GL20.glVertexAttribPointer(TEXCOORD_ATTRIBUTE, 2, GL11.GL_FLOAT, false, 16, 8L)
        GL30.glBindVertexArray(0)
    }

    fun bind() {
        GL30.glBindVertexArray(vao)
        GL20.glEnableVertexAttribArray(POSITION_ATTRIBUTE)
        GL20.glEnableVertexAttribArray(TEXCOORD_ATTRIBUTE)
    }

    fun draw() {
        GL11.glDrawElements(GL11.GL_TRIANGLES, numElements, GL11.GL_UNSIGNED_INT, 0L)
    }

    fun unbind() {
        GL20.glDisableVertexAttribArray(TEXCOORD_ATTRIBUTE)
        GL20.glDisableVertexAttribArray(POSITION_ATTRIBUTE)
        GL30.glBindVertexArray(0)
    }

    fun cleanup() {
        GL30.glDeleteVertexArrays(vao)
        GL15.glDeleteBuffers(ebo)
        GL15.glDeleteBuffers(vbo)
    }
}

class StaticTextShader : Shader(
        loadStringFromFile("static-text-shader.vert"),
        loadStringFromFile("static-text-shader.frag")
) {
    val projection = uniformMat4(program, "u_projection")
    val color = uniform3f(program, "u_color")
}
