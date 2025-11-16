package org.game

import org.joml.Matrix4f
import org.lwjgl.opengl.GL30

class Frame(
    private val shader: FrameShader,
    private var windowWidth: Int,
    private var windowHeight: Int,
) {
    private data class Quad(val vao: Int, val vbo: Int, var vertexCount: Int)
    private lateinit var top: Quad
    private lateinit var left: Quad
    private lateinit var right: Quad

    private var hudHeight = windowHeight * Constants.HUD_HEIGHT_RATIO

    init {
        buildGeometry()
    }

    fun cleanup() {
        listOf(top, left, right).forEach { quad ->
            GL30.glDeleteVertexArrays(quad.vao)
            GL30.glDeleteBuffers(quad.vbo)
        }
        shader.cleanup()
    }

    fun updateWindowSize(w: Int, h: Int) {
        windowWidth = w
        windowHeight = h
        hudHeight = windowHeight * Constants.HUD_HEIGHT_RATIO
        buildGeometry()
    }

    fun render() {
        shader.use()
        // Projection for Window Coordinates
        val proj =
            Matrix4f().ortho2D(0f, windowWidth.toFloat(), 0f, windowHeight.toFloat()).get(FloatArray(16))
        shader.setUniformMat4("uProjection", proj)
        // set frame color
        shader.setUniformVec3("uColor", Constants.FRAME_COLOR_R, Constants.FRAME_COLOR_G, Constants.FRAME_COLOR_B)
        // Draw top
        GL30.glBindVertexArray(top.vao)
        GL30.glDrawArrays(GL30.GL_QUADS, 0, 4)
        // Draw left
        GL30.glBindVertexArray(left.vao)
        GL30.glDrawArrays(GL30.GL_QUADS, 0, 4)
        // Draw right
        GL30.glBindVertexArray(right.vao)
        GL30.glDrawArrays(GL30.GL_QUADS, 0, 4)

        GL30.glBindVertexArray(0)
    }

    private fun buildQuad(x: Float, y: Float, w: Float, h: Float): Quad {
        // Vertices for a quad (two triangles); GL_QUADS is deprecated but used for simplicity
        val vertices = floatArrayOf(
            x, y,
            x + w, y,
            x + w, y + h,
            x, y + h
        )

        val vao = GL30.glGenVertexArrays()
        val vbo = GL30.glGenBuffers()
        GL30.glBindVertexArray(vao)
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo)
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertices, GL30.GL_STATIC_DRAW)

        GL30.glEnableVertexAttribArray(0)
        GL30.glVertexAttribPointer(0, 2, GL30.GL_FLOAT, false, 2 * 4, 0)

        GL30.glBindVertexArray(0)
        return Quad(vao, vbo, 4)
    }

    private fun buildGeometry() {
        val topHeight = windowHeight * 0.025f
        val sideWidth = windowWidth * 0.05f

        top = buildQuad(0f, windowHeight - hudHeight - topHeight, windowWidth.toFloat(), topHeight)
        left = buildQuad(0f, 0f, sideWidth, windowHeight - hudHeight - topHeight)
        right = buildQuad(windowWidth - sideWidth, 0f, sideWidth, windowHeight - hudHeight - topHeight)
    }
}