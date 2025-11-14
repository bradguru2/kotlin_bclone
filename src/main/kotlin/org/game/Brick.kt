package org.game

import org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN
import org.lwjgl.opengl.GL11.glDrawArrays
import org.lwjgl.opengl.GL15.glDeleteBuffers
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glDeleteVertexArrays

class Brick(private val shader: ShaderProgram, val x: Float, val y: Float, val width: Float = 60f, val height: Float = 20f) {
    private val vao: Int = 0
    private val vbo: Int = 0

    init { /* setup rectangle VAO/VBO */ }

    fun render() {
        shader.use()
        // Set uniforms for position & color
        glBindVertexArray(vao)
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4)
        glBindVertexArray(0)
    }

    fun cleanup() {
        glDeleteVertexArrays(vao)
        glDeleteBuffers(vbo)
        shader.cleanup()
    }
}
