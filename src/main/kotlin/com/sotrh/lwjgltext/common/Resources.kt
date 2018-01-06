package com.sotrh.lwjgltext.common

import com.sotrh.lwjgltext.texture.TextureManager

object Resources {
    val textureManager = TextureManager()

    fun cleanup() {
        textureManager.cleanup()
    }
}