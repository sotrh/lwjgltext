package com.sotrh.lwjgltext.texture

import com.sotrh.lwjgltext.common.myAssert
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13

/**
 * Created by benjamin on 11/13/17
 */
data class Texture(val textureId: Int,
                   val name: String,
                   val width: Int,
                   val height: Int,
                   val components: Int,
                   val isHdrFromMemory: Boolean
) {
    private var activeIndex = 0

    fun bind(index: Int) {
        activeIndex = GL13.GL_TEXTURE0 + index
        GL13.glActiveTexture(activeIndex)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId)
    }

    fun unbind() {
        myAssert(activeIndex != 0)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0)
    }
}