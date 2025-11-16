package org.game

import org.lwjgl.opengl.GL30

abstract class ShaderProgram(vertexSource: String, fragmentSource: String) {
    val programId: Int

    init {
        val vertexId = compileShader(loadResourceString(vertexSource), GL30.GL_VERTEX_SHADER)
        val fragmentId = compileShader(loadResourceString(fragmentSource), GL30.GL_FRAGMENT_SHADER)
        programId = GL30.glCreateProgram()
        GL30.glAttachShader(programId, vertexId)
        GL30.glAttachShader(programId, fragmentId)
        GL30.glLinkProgram(programId)
        GL30.glValidateProgram(programId)
        GL30.glDeleteShader(vertexId)
        GL30.glDeleteShader(fragmentId)
    }

    fun use() = GL30.glUseProgram(programId)

    fun setUniformVec2(name: String, x: Float, y: Float) {
        val location = GL30.glGetUniformLocation(programId, name)
        GL30.glUniform2f(location, x, y)
    }
    
    fun setUniformVec3(name: String, x: Float, y: Float, z: Float) {
        val location = GL30.glGetUniformLocation(programId, name)
        GL30.glUniform3f(location, x, y, z)
    }

    fun setUniformMat4(name: String, matrix: FloatArray) {
        val location = GL30.glGetUniformLocation(programId, name)
        GL30.glUniformMatrix4fv(location, false, matrix)
    }

    fun cleanup() = GL30.glDeleteProgram(programId)

    private fun compileShader(source: String, type: Int): Int {
        val shaderId = GL30.glCreateShader(type)
        GL30.glShaderSource(shaderId, source)
        GL30.glCompileShader(shaderId)
        val success = GL30.glGetShaderi(shaderId, GL30.GL_COMPILE_STATUS)
        if (success == 0) {
            throw RuntimeException("Error compiling shader: " + GL30.glGetShaderInfoLog(shaderId))
        }
        return shaderId
    }

    private fun loadResourceString(path: String): String =
        requireNotNull(this.javaClass.getResourceAsStream(path)) {
            "Resource not found: $path"
        }.bufferedReader().use { it.readText() }

}

