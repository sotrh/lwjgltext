package com.sotrh.lwjgltext.texture

import com.sotrh.lwjgltext.common.loadByteBufferFromFile
import com.sotrh.lwjgltext.common.myAssert
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.stb.STBImage
import org.lwjgl.stb.STBImageResize
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

class TextureManager {
    private val textureMap = mutableMapOf<String, Texture>()

    private fun textureMapToIntArray(): IntArray {
        return textureMap.values.map { it.textureId }.toIntArray()
    }

    fun cleanup() {
        GL11.glDeleteTextures(textureMapToIntArray())
        textureMap.clear()
    }

    fun loadTexture2D(name: String): Texture {
        if (textureMap.containsKey(name)) return textureMap[name]!!

        val imageBuffer = loadByteBufferFromFile(name)

        MemoryStack.stackPush().use { stack ->
            val w = stack.mallocInt(1)
            val h = stack.mallocInt(1)
            val comp = stack.mallocInt(1)

            myAssert(STBImage.stbi_info_from_memory(imageBuffer, w, h, comp), {
                STBImage.stbi_failure_reason()
            })

            val isHdrFromMemory = STBImage.stbi_is_hdr_from_memory(imageBuffer)
            val image = STBImage.stbi_load_from_memory(imageBuffer, w, h, comp, 0)
            val textureId = createOpenGLTexture(comp[0], w[0], h[0], image)

            val texture = Texture(textureId, name, w[0], h[0], comp[0], isHdrFromMemory)
            textureMap.put(name, texture)
            return texture
        }
    }

    private fun createOpenGLTexture(components: Int, width: Int, height: Int,
                                    image: ByteBuffer): Int {
        val textureId = GL11.glGenTextures()

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)

        val format = if (components == 3) GL11.GL_RGB else GL11.GL_RGBA
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, format, width, height, 0,
                format, GL11.GL_UNSIGNED_BYTE, image)

        createMipmaps(image, width, height, components, format)

        return textureId
    }

    private fun createMipmaps(image: ByteBuffer, width: Int, height: Int, components: Int,
                              format: Int) {
        var inputPixels = image
        var inputWidth = width
        var inputHeight = height
        var mipmapLevel = 0

        while (inputWidth > 1 && inputHeight > 1) {
            val outputWidth = Math.max(1, inputWidth / 2)
            val outputHeight = Math.max(1, inputHeight / 2)
            val outputPixels = MemoryUtil.memAlloc(outputWidth * outputHeight * components)

            STBImageResize.stbir_resize_uint8_generic(
                    inputPixels, inputWidth, inputHeight,
                    inputWidth * components,
                    outputPixels, outputWidth, outputHeight,
                    outputWidth * components,
                    components,
                    if (components == 4) 3 else STBImageResize.STBIR_ALPHA_CHANNEL_NONE,
                    STBImageResize.STBIR_FLAG_ALPHA_PREMULTIPLIED,
                    STBImageResize.STBIR_EDGE_CLAMP,
                    STBImageResize.STBIR_FILTER_MITCHELL,
                    STBImageResize.STBIR_COLORSPACE_SRGB)

            if (mipmapLevel == 0) {
                STBImage.stbi_image_free(image)
            } else {
                MemoryUtil.memFree(inputPixels)
            }

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, ++mipmapLevel, format,
                    outputWidth, outputHeight, 0, format,
                    GL11.GL_UNSIGNED_BYTE, outputPixels)

            inputPixels = outputPixels
            inputWidth = outputWidth
            inputHeight = outputHeight
        }

        if (mipmapLevel == 0) {
            STBImage.stbi_image_free(image)
        } else {
            MemoryUtil.memFree(inputPixels)
        }
    }
}