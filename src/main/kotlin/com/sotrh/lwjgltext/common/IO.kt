package com.sotrh.lwjgltext.common

import org.lwjgl.BufferUtils
import java.io.File
import java.nio.ByteBuffer
import java.nio.channels.Channels

fun loadStringFromFile(filename: String): String {
    val url = Thread.currentThread().contextClassLoader.getResource(filename)
    val file = File(url.file)
    myAssert(file.isFile) {"File \"$filename\" not found"}

    return file.readText()
}

fun loadByteBufferFromFile(filename: String, bufferSize: Int = 1024): ByteBuffer {
    val url = Thread.currentThread().contextClassLoader.getResource(filename)
    val file = File(url.file)
    myAssert(file.isFile) {"File \"$filename\" not found"}

    var buffer = BufferUtils.createByteBuffer(bufferSize)

    Thread.currentThread().contextClassLoader.getResourceAsStream(filename).use { stream ->
        Channels.newChannel(stream).use { readableByteChannel ->
            while (true) {
                val bytes = readableByteChannel.read(buffer)
                if (bytes == -1) break;
                else if (buffer.remaining() == 0) {
                    buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2)
                }
            }
        }
    }

    buffer.flip()
    return buffer.slice()
}

fun resizeBuffer(buffer: ByteBuffer, newCapacity: Int): ByteBuffer {
    val newBuffer = BufferUtils.createByteBuffer(newCapacity)
    buffer.flip()
    newBuffer.put(buffer)
    return newBuffer
}