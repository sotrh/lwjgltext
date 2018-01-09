package com.sotrh.lwjgltext.font

import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f

class TextQuadRenderer(private val font: BitmapFont) {
    private val vao = TextQuadVao()
    private val shader = TextQuadShader()
    private val textureCoords = Vector4f()
    private val textureCoordsBuffer = FloatArray(2 * 4)

    private val cursor = Vector3f()
    private val drawPosition = Vector3f()
    private val drawScale = Vector2f()

    fun bind() {
        vao.bind()
        shader.bind()
        font.pageArray[0].texture.bind(0)
    }

    fun drawChar(char: BitmapFont.Char, position: Vector3f, scale: Vector2f, color: Vector3f) {
        textureCoords.x = char.x / font.common.scaleW.toFloat() // left
        textureCoords.y = (char.y + char.height) / font.common.scaleH.toFloat() // bottom
        textureCoords.z = (char.x + char.width) / font.common.scaleW.toFloat() // right
        textureCoords.w = char.y / font.common.scaleH.toFloat() // top

        // left, bottom
        textureCoordsBuffer[0] = textureCoords.x
        textureCoordsBuffer[1] = textureCoords.y
        // right, bottom
        textureCoordsBuffer[2] = textureCoords.z
        textureCoordsBuffer[3] = textureCoords.y
        // right, top
        textureCoordsBuffer[4] = textureCoords.z
        textureCoordsBuffer[5] = textureCoords.w
        // left, top
        textureCoordsBuffer[6] = textureCoords.x
        textureCoordsBuffer[7] = textureCoords.w

        shader.textureCoords(textureCoordsBuffer)
        shader.position(position)
        shader.scale(scale)
        shader.color(color)
        vao.draw()
    }

    fun drawString(string: String, position: Vector3f, scale: Vector2f, color: Vector3f) {
        cursor.set(position)
        drawScale.set(scale)
        val scaleW = font.common.scaleW.toFloat()
        val scaleH = font.common.scaleH.toFloat()
        string.forEachIndexed { index, c ->
            val char = font.charMap[c.toInt()]!!

            drawScale.x = scale.x * char.width / scaleW
            drawScale.y = scale.y * char.height / scaleH

            drawPosition.set(cursor)
            drawPosition.x += char.xoffset / scaleW * scale.x
            drawPosition.y += -char.yoffset / scaleH * scale.y

            if (char.width > 0 && char.height > 0) {
                drawChar(char, drawPosition, drawScale, color)
            }

            cursor.x += char.xadvance * scale.x / scaleW
        }
    }

    fun unbind() {
        vao.unbind()
        shader.unbind()
        font.pageArray[0].texture.unbind()
    }

    fun cleanup() {
        vao.cleanup()
        shader.cleanup()
    }
}