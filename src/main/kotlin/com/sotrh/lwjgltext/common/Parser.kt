package com.sotrh.lwjgltext.common

/**
 * Created by benjamin on 12/2/17
 */
open class Parser(data: String) {
    private val scanner = Scanner(data)

    fun setData(data: String) {
        scanner.setData(data)
    }

    fun require(data: String) {
        data.forEach {
            assert(it == scanner.read())
        }
    }

    fun requireWhitespace() {
        assert(scanner.peek()?.isWhitespace() == true)
        consumeWhile(Char::isWhitespace)
    }

    fun consumeWhile(check: (Char) -> Boolean): String {
        val sb = StringBuilder()
        while (scanner.peek()?.let { check(it) } == true) {
            sb.append(scanner.read()!!)
        }
        return sb.toString()
    }

    fun consumeWhitespace(): String {
        return consumeWhile(Char::isWhitespace)
    }

    fun parseId(): String {
        val id = consumeWhile { it.isLetterOrDigit() || it == '_' }
        assert(id.isNotEmpty() && (id[0].isLetter() || id[0] == '_')) { "Invalid id: $id" }
        return id
    }

    fun parseNumber(): String {
        val firstPart = if (scanner.peek() == '-')
            scanner.read()!! + consumeWhile(Char::isDigit)
        else consumeWhile(Char::isDigit)

        return if (scanner.peek() == '.') {
            scanner.read()
            val secondPart = consumeWhile(Char::isDigit)
            val combined = "$firstPart.$secondPart"
            assert(firstPart.isNotEmpty() || secondPart.isNotEmpty()) { "Invalid number: $combined" }
            combined
        } else {
            if (firstPart.startsWith("-"))
                assert(firstPart.length > 2)
            else
                assert(firstPart.isNotEmpty())

            firstPart
        }
    }

    fun parseString(): String {
        assert(scanner.read() == '"') { "Missing opening quote" }
        val innerPart = consumeWhile { it != '"' }
        assert(scanner.read() == '"') { "Missing closing quote" }
        return innerPart
    }
}