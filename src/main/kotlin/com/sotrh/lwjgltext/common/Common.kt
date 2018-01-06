package com.sotrh.lwjgltext.common

import org.lwjgl.glfw.GLFW

inline fun myAssert(condition: Boolean, lazyMessage: ()->String = { "Assertion Error" }) {
    if (!condition) throw AssertionError(lazyMessage())
}

inline fun assertNotNull(obj: Any?, lazyMessage: () -> String = { "Given object was null" }) {
    myAssert(obj != null, lazyMessage)
}

fun createUpdateLambda(fps: Double): (block: ()->Unit) -> Unit {
    val msPerUpdate = 1 / fps
    var previous = GLFW.glfwGetTime()
    var lag = 0.0

    return { block ->
        val current = GLFW.glfwGetTime()
        val elapsed = current - previous
        previous = current
        lag += elapsed

        while (lag >= msPerUpdate) {
            block()
            lag -= msPerUpdate
        }
    }
}

fun rangeUntil(value: Int): IntRange {
    return 0 until value
}