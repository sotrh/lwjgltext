package com.sotrh.lwjgltext

import com.sotrh.lwjgltext.common.Resources
import com.sotrh.lwjgltext.common.createUpdateLambda
import com.sotrh.lwjgltext.render.BoxRenderer
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11

fun main(args: Array<String>) {
    com.sotrh.lwjgltext.common.myAssert(GLFW.glfwInit())

    GLFW.glfwDefaultWindowHints()
    GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE)
    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3)
    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3)
    GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE)
    GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE)

    val window = GLFW.glfwCreateWindow(800, 600, "LWJGL Text", 0, 0)
    com.sotrh.lwjgltext.common.myAssert(window != 0L)

    GLFW.glfwSwapInterval(1)
    GLFW.glfwMakeContextCurrent(window)
    GL.createCapabilities()

    GL11.glClearColor(0.2f, 0.2f, 0.4f, 1.0f)

    val update = createUpdateLambda(60.0)
    val boxRenderer = BoxRenderer()
    val boxPosition = Vector3f()
    val boxScale = Vector2f(1f, 1f)
    val boxColor = Vector3f(1f)

    while (!GLFW.glfwWindowShouldClose(window)) {
        GLFW.glfwPollEvents()

        update {
            // do update stuff
        }

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)

        boxRenderer.bind()
        boxRenderer.drawBox(boxPosition, boxScale, boxColor)
        boxRenderer.unbind()

        GLFW.glfwSwapBuffers(window)
    }

    Resources.cleanup()
    Callbacks.glfwFreeCallbacks(window)
    GLFW.glfwDestroyWindow(window)
}