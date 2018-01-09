package com.sotrh.lwjgltext.font

import com.sotrh.lwjgltext.texture.Texture

/**
 * Created by benjamin on 11/24/17
 */
class BitmapFont(val info: Info,
                 val common: Common,
                 val pageArray: Array<BitmapFont.Page>,
                 val charMap: Map<Int, BitmapFont.Char>,
                 val kernings: Map<Int, BitmapFont.Kerning>
) {
    data class Info(val face: String, val size: Int, val bold: Int, val italic: Int, val charset: String, val unicode: Int, val stretchH: Int, val smooth: Int, val aa: Int, val padding: Padding, val spacing: Spacing)
    data class Padding(val top: Int, val right: Int, val bottom: Int, val left: Int)
    data class Spacing(val x: Int, val y: Int)
    data class Common(val lineHeight: Int, val base: Int, val scaleW: Int, val scaleH: Int, val pages: Int, val packed: Int)
    data class Page(val id: Int, val file: String, val texture: Texture)
    data class Char(val id: Int, val x: Int, val y: Int, val width: Int, val height: Int, val xoffset: Int, val yoffset: Int, val xadvance: Int, val page: Int, val chnl: Int)
    data class Kerning(val first: Int, val second: Int, val amount: Int)
}