package com.sotrh.lwjgltext

import com.sotrh.lwjgltext.common.Resources
import com.sotrh.lwjgltext.common.updateLambda
import com.sotrh.lwjgltext.font.StaticTextRenderer
import com.sotrh.lwjgltext.font.TextQuadRenderer
import com.sotrh.lwjgltext.render.TestOrthoRenderer
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
    GL11.glEnable(GL11.GL_BLEND)
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

    val update = updateLambda(60.0)

    val font = Resources.fontManager.loadBitmapFont("liberation_serif.fnt")
    val textQuadRenderer = TextQuadRenderer(font)
    val textPosition = Vector3f(-0.75f, 0.75f, 0f)
    val textPosition2 = Vector3f(-0.75f, 0.5f, 0f)
    val textScale = Vector2f(0.5f, 0.5f)
    val textColor = Vector3f(1f)

    val testOrthoRenderer = TestOrthoRenderer()
    testOrthoRenderer.bind()
    testOrthoRenderer.ortho(800, 600)
    testOrthoRenderer.unbind()

    val staticTextRenderer = StaticTextRenderer(font)
    staticTextRenderer.ortho(800, 600)
    val testText = staticTextRenderer.createStaticTextVao(10f, 10f, "This is a test.\nThis has a newline.")

    while (!GLFW.glfwWindowShouldClose(window)) {
        GLFW.glfwPollEvents()

        val elapsedTime = update {
            // do update stuff
        } / 1000

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)

//        textQuadRenderer.bind()
//        textQuadRenderer.drawString("FPS: ${1/elapsedTime}", textPosition, textScale, textColor)
//        textQuadRenderer.drawString("ElapsedTime: $elapsedTime", textPosition2, textScale, textColor)
//        textQuadRenderer.unbind()
//
//        testOrthoRenderer.bind()
//        testOrthoRenderer.draw()
//        testOrthoRenderer.unbind()

        staticTextRenderer.bind()
        staticTextRenderer.draw(testText, textColor)
        staticTextRenderer.unbind()

        GLFW.glfwSwapBuffers(window)
    }

    textQuadRenderer.cleanup()

    Resources.cleanup()
    Callbacks.glfwFreeCallbacks(window)
    GLFW.glfwDestroyWindow(window)
}