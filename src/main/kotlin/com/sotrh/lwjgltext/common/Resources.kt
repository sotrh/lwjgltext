package com.sotrh.lwjgltext.common

import com.sotrh.lwjgltext.font.FontManager
import com.sotrh.lwjgltext.texture.TextureManager

object Resources {
    val textureManager = TextureManager()
    val fontManager = FontManager(textureManager)

    fun cleanup() {
        fontManager.cleanup()
        textureManager.cleanup()
    }
}