package com.sotrh.lwjgltext.render

import com.sotrh.lwjgltext.common.myAssert
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30

open class Shader(vertexShaderSource: String, fragmentShaderSource: String) {
    protected var program: Int = GL20.glCreateProgram(); private set
    private var isBound = false

    init {
        val vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER)
        GL20.glShaderSource(vertexShader, vertexShaderSource)
        GL20.glCompileShader(vertexShader)
        var status = GL20.glGetShaderi(vertexShader, GL20.GL_COMPILE_STATUS)
        myAssert(status == GL11.GL_TRUE) { GL20.glGetShaderInfoLog(vertexShader) }

        val fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER)
        GL20.glShaderSource(fragmentShader, fragmentShaderSource)
        GL20.glCompileShader(fragmentShader)
        status = GL20.glGetShaderi(fragmentShader, GL20.GL_COMPILE_STATUS)
        myAssert(status == GL11.GL_TRUE) { GL20.glGetShaderInfoLog(fragmentShader) }

        GL20.glAttachShader(program, vertexShader)
        GL20.glAttachShader(program, fragmentShader)
        GL30.glBindFragDataLocation(program, 0, "f_color")
        GL20.glLinkProgram(program)
        status = GL20.glGetProgrami(program, GL20.GL_LINK_STATUS)
        myAssert(status == GL11.GL_TRUE) { GL20.glGetProgramInfoLog(program) }

        GL20.glDeleteShader(vertexShader)
        GL20.glDeleteShader(fragmentShader)
    }

    fun bind() {
        assertNotBound()
        GL20.glUseProgram(program)
        isBound = true
    }

    protected fun assertBound() {
        myAssert(isBound)
    }

    protected fun assertNotBound() {
        myAssert(!isBound)
    }

    fun unbind() {
        assertBound()
        GL20.glUseProgram(0)
        isBound = false
    }

    fun destroy() {
        GL20.glDeleteProgram(program)
        program = 0
    }
}