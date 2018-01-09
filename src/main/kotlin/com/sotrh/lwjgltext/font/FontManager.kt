package com.sotrh.lwjgltext.font

import com.sotrh.lwjgltext.common.Parser
import com.sotrh.lwjgltext.common.loadBufferedReaderFromFile
import com.sotrh.lwjgltext.common.toIntOrZero
import com.sotrh.lwjgltext.texture.TextureManager

/**
 * Created by benjamin on 11/16/17
 */
class FontManager(private val textureManager: TextureManager) {

    private val fontMap = mutableMapOf<String, BitmapFont>()

    fun cleanup() {
        fontMap.clear()
    }

    fun loadBitmapFont(resource: String): BitmapFont {

        if (fontMap.containsKey(resource)) return fontMap[resource]!!

        val parser = loadBufferedReaderFromFile(resource).use {
            Parser(it.readLines().joinToString("\n"))
        }

        parser.consumeWhitespace()
        val info = parseInfo(parser)
        parser.requireWhitespace()
        val common = parseCommon(parser)
        parser.requireWhitespace()
        val pageArray = parsePages(parser, common.pages)
        parser.requireWhitespace()
        val charMap = parseChars(parser)
        parser.requireWhitespace()
        val kernings = parseKernings(parser)

        return BitmapFont(info, common, pageArray, charMap, kernings)
    }

    private fun parseInfo(parser: Parser): BitmapFont.Info {
        parser.require("info")
        parser.requireWhitespace()
        val face = requireKeyEqualsParseString(parser, "face")
        parser.requireWhitespace()
        val size = requireKeyEqualsParseNumber(parser, "size")
        parser.requireWhitespace()
        val bold = requireKeyEqualsParseNumber(parser, "bold")
        parser.requireWhitespace()
        val italic = requireKeyEqualsParseNumber(parser, "italic")
        parser.requireWhitespace()
        val charset = requireKeyEqualsParseString(parser, "charset")
        parser.requireWhitespace()
        val unicode = requireKeyEqualsParseNumber(parser, "unicode")
        parser.requireWhitespace()
        val stretchH = requireKeyEqualsParseNumber(parser, "stretchH")
        parser.requireWhitespace()
        val smooth = requireKeyEqualsParseNumber(parser, "smooth")
        parser.requireWhitespace()
        val aa = requireKeyEqualsParseNumber(parser, "aa")
        parser.requireWhitespace()
        val padding = parsePadding(parser)
        parser.requireWhitespace()
        val spacing = parseSpacing(parser)

        return BitmapFont.Info(face, size, bold, italic, charset, unicode, stretchH, smooth, aa, padding, spacing)
    }

    private fun parsePadding(parser: Parser): BitmapFont.Padding {
        requireKeyEquals(parser, "padding")
        parser.consumeWhitespace()
        val top = parser.parseNumber().toIntOrZero()
        parser.consumeWhitespace()
        parser.require(",")
        parser.consumeWhitespace()
        val right = parser.parseNumber().toIntOrZero()
        parser.consumeWhitespace()
        parser.require(",")
        parser.consumeWhitespace()
        val bottom = parser.parseNumber().toIntOrZero()
        parser.consumeWhitespace()
        parser.require(",")
        parser.consumeWhitespace()
        val left = parser.parseNumber().toIntOrZero()

        return BitmapFont.Padding(top, right, bottom, left)
    }

    private fun parseSpacing(parser: Parser): BitmapFont.Spacing {
        requireKeyEquals(parser, "spacing")
        parser.consumeWhitespace()
        val x = parser.parseNumber().toIntOrZero()
        parser.consumeWhitespace()
        parser.require(",")
        parser.consumeWhitespace()
        val y = parser.parseNumber().toIntOrZero()

        return BitmapFont.Spacing(x, y)
    }

    private fun parseCommon(parser: Parser): BitmapFont.Common {
        parser.require("common")
        parser.requireWhitespace()
        val lineHeight = requireKeyEqualsParseNumber(parser, "lineHeight")
        parser.requireWhitespace()
        val base = requireKeyEqualsParseNumber(parser, "base")
        parser.requireWhitespace()
        val scaleW = requireKeyEqualsParseNumber(parser, "scaleW")
        parser.requireWhitespace()
        val scaleH = requireKeyEqualsParseNumber(parser, "scaleH")
        parser.requireWhitespace()
        val pages = requireKeyEqualsParseNumber(parser, "pages")
        parser.requireWhitespace()
        val packed = requireKeyEqualsParseNumber(parser, "packed")

        return BitmapFont.Common(lineHeight, base, scaleW, scaleH, pages, packed)
    }

    private fun parsePages(parser: Parser, numPages: Int): Array<BitmapFont.Page> {
        assert(numPages > 0) { "Invalid page count: $numPages" }
        return Array(numPages) {
            if (it > 0) parser.requireWhitespace()
            parser.require("page")
            parser.requireWhitespace()
            val id = requireKeyEqualsParseNumber(parser, "id")
            parser.requireWhitespace()
            val file = requireKeyEqualsParseString(parser, "file")

            BitmapFont.Page(id, file, textureManager.loadTexture2D(file))
        }
    }

    private fun parseChars(parser: Parser): MutableMap<Int, BitmapFont.Char> {
        val count = requireKeyParseCount(parser, "chars")
        assert(count > 0) { "Invalid chars count: $count" }
        parser.requireWhitespace()
        val charMap = mutableMapOf<Int, BitmapFont.Char>()
        repeat(count) {
            if (it > 0) parser.requireWhitespace()
            parser.require("char")
            parser.requireWhitespace()
            val id = requireKeyEqualsParseNumber(parser, "id")
            parser.requireWhitespace()
            val x = requireKeyEqualsParseNumber(parser, "x")
            parser.requireWhitespace()
            val y = requireKeyEqualsParseNumber(parser, "y")
            parser.requireWhitespace()
            val width = requireKeyEqualsParseNumber(parser, "width")
            parser.requireWhitespace()
            val height = requireKeyEqualsParseNumber(parser, "height")
            parser.requireWhitespace()
            val xoffset = requireKeyEqualsParseNumber(parser, "xoffset")
            parser.requireWhitespace()
            val yoffset = requireKeyEqualsParseNumber(parser, "yoffset")
            parser.requireWhitespace()
            val xadvance = requireKeyEqualsParseNumber(parser, "xadvance")
            parser.requireWhitespace()
            val page = requireKeyEqualsParseNumber(parser, "page")
            parser.requireWhitespace()
            val chnl = requireKeyEqualsParseNumber(parser, "chnl")

            charMap.put(id, BitmapFont.Char(id, x, y, width, height, xoffset, yoffset, xadvance, page, chnl))
        }
        return charMap
    }

    private fun parseKernings(parser: Parser): Map<Int, BitmapFont.Kerning> {
        val count = requireKeyParseCount(parser, "kernings")
        assert(count > 0) { "Invalid chars count: $count" }
        parser.requireWhitespace()
        val map = mutableMapOf<Int, BitmapFont.Kerning>()
        repeat(count) {
            if (it > 0) parser.requireWhitespace()
            parser.require("kerning")
            parser.requireWhitespace()
            val first = requireKeyEqualsParseNumber(parser, "first")
            parser.requireWhitespace()
            val second = requireKeyEqualsParseNumber(parser, "second")
            parser.requireWhitespace()
            val amount = requireKeyEqualsParseNumber(parser, "amount")

            map.put(first, BitmapFont.Kerning(first, second, amount))
        }
        return map
    }

    private fun requireKeyParseCount(parser: Parser, key: String): Int {
        parser.require(key)
        parser.requireWhitespace()
        return requireKeyEqualsParseNumber(parser, "count")
    }

    private fun requireKeyEquals(parser: Parser, key: String) {
        parser.require(key)
        parser.consumeWhitespace()
        parser.require("=")
    }

    private fun requireKeyEqualsParseString(parser: Parser, key: String): String {
        requireKeyEquals(parser, key)
        parser.consumeWhitespace()
        return parser.parseString()
    }

    private fun requireKeyEqualsParseNumber(parser: Parser, key: String): Int {
        requireKeyEquals(parser, key)
        parser.consumeWhitespace()
        return parser.parseNumber().toIntOrZero()
    }
}
