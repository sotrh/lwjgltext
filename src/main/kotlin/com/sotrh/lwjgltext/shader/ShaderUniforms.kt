package com.sotrh.lwjgltext.shader

import com.sotrh.lwjgltext.common.myAssert
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL20

fun createUniform1f(program: Int, name: String): (Float)->Unit {
    val location = GL20.glGetUniformLocation(program, name)
    return {
        GL20.glUniform1f(location, it)
    }
}

fun uniform2f(program: Int, name: String): (Vector2f)->Unit {
    val location = GL20.glGetUniformLocation(program, name)
    return {
        GL20.glUniform2f(location, it.x, it.y)
    }
}

fun uniform3f(program: Int, name: String): (Vector3f)->Unit {
    val location = GL20.glGetUniformLocation(program, name)
    return {
        GL20.glUniform3f(location, it.x, it.y, it.z)
    }
}

fun uniform4f(program: Int, name: String): (Vector4f)->Unit {
    val location = GL20.glGetUniformLocation(program, name)
    return {
        GL20.glUniform4f(location, it.x, it.y, it.z, it.w)
    }
}

fun uniform2fv(program: Int, name: String): (FloatArray)->Unit {
    val location = GL20.glGetUniformLocation(program, name)
    return {
        GL20.glUniform2fv(location, it)
    }
}

fun createUniform1i(program: Int, name: String): (Int)->Unit {
    val location = GL20.glGetUniformLocation(program, name)
    return {
        GL20.glUniform1i(location, it)
    }
}

private val matrixBuffer = FloatArray(16)

fun uniformMat4(program: Int, name: String): (Matrix4f)->Unit {
    val location = GL20.glGetUniformLocation(program, name)
    return {
        GL20.glUniformMatrix4fv(location, false, it.get(matrixBuffer))
    }
}