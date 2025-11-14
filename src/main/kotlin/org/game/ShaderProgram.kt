package org.game

import org.lwjgl.opengl.GL20

abstract class ShaderProgram(vertexSource: String, fragmentSource: String) {
    val programId: Int

    init {
        val vertexId = compileShader(vertexSource, GL20.GL_VERTEX_SHADER)
        val fragmentId = compileShader(fragmentSource, GL20.GL_FRAGMENT_SHADER)
        programId = GL20.glCreateProgram()
        GL20.glAttachShader(programId, vertexId)
        GL20.glAttachShader(programId, fragmentId)
        GL20.glLinkProgram(programId)
        GL20.glValidateProgram(programId)
        GL20.glDeleteShader(vertexId)
        GL20.glDeleteShader(fragmentId)
    }

    fun use() = GL20.glUseProgram(programId)

    fun setUniformVec3(name: String, x: Float, y: Float, z: Float) {
        val location = GL20.glGetUniformLocation(programId, name)
        GL20.glUniform3f(location, x, y, z)
    }

    fun setUniformMat4(name: String, matrix: FloatArray) {
        val location = GL20.glGetUniformLocation(programId, name)
        GL20.glUniformMatrix4fv(location, false, matrix)
    }

    fun cleanup() = GL20.glDeleteProgram(programId)

    private fun compileShader(source: String, type: Int): Int {
        val shaderId = GL20.glCreateShader(type)
        GL20.glShaderSource(shaderId, source)
        GL20.glCompileShader(shaderId)
        val success = GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS)
        if (success == 0) {
            throw RuntimeException("Error compiling shader: " + GL20.glGetShaderInfoLog(shaderId))
        }
        return shaderId
    }
}

