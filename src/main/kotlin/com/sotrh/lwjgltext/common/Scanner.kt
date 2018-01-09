package com.sotrh.lwjgltext.common

/**
 * Created by benjamin on 12/2/17
 */
class Scanner(private var data: String) {
    private var position: Int = 0

    fun setData(data: String) {
        this.data = data
        this.position = 0
    }

    fun peek(): Char? {
        return if (position < data.length) data[position] else null
    }

    fun read(): Char? {
        val char = peek()
        position++
        return char
    }
}
