package org.game

import org.lwjgl.opengl.GL11.GL_FLOAT
import org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN
import org.lwjgl.opengl.GL11.glDrawArrays
import org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER
import org.lwjgl.opengl.GL15.GL_STATIC_DRAW
import org.lwjgl.opengl.GL15.glBindBuffer
import org.lwjgl.opengl.GL15.glBufferData
import org.lwjgl.opengl.GL15.glDeleteBuffers
import org.lwjgl.opengl.GL15.glGenBuffers
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glDeleteVertexArrays
import org.lwjgl.opengl.GL30.glGenVertexArrays

class Ball(private val shader: ShaderProgram, val radius: Float = 10f, val segments: Int = 36) {
    var x = 400f
    var y = 300f
    var vx = 200f
    var vy = 200f

    private val vao: Int
    private val vbo: Int
    private val vertexCount: Int

    init {
        val vertices = FloatArray((segments + 2) * 2)
        vertices[0] = 0f
        vertices[1] = 0f
        for (i in 0..segments) {
            val angle = 2.0 * Math.PI * i / segments
            vertices[(i + 1) * 2] = (radius * Math.cos(angle)).toFloat()
            vertices[(i + 1) * 2 + 1] = (radius * Math.sin(angle)).toFloat()
        }
        vertexCount = segments + 2

        vao = glGenVertexArrays()
        glBindVertexArray(vao)
        vbo = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 2 * 4, 0)
        glEnableVertexAttribArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)
    }

    fun render() {
        shader.use()
        shader.setUniformVec3("objectPos", x, y, 0f)
        shader.setUniformVec3("objectColor", 1f, 1f, 0f)
        glBindVertexArray(vao)
        glDrawArrays(GL_TRIANGLE_FAN, 0, vertexCount)
        glBindVertexArray(0)
    }

    fun cleanup() {
        glDeleteVertexArrays(vao)
        glDeleteBuffers(vbo)
        shader.cleanup()
    }
}
