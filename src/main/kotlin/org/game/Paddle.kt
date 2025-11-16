package org.game

import org.joml.Matrix4f
import org.lwjgl.opengl.GL30

class Paddle(private val shader: PaddleShader, private var windowWidth: Int, private var windowHeight: Int) {
    private data class Quad(val vao: Int, val vbo: Int, var vertexCount: Int)
    private lateinit var paddle: Quad

    private var paddleHeight = windowHeight * Constants.PADDLE_HEIGHT_RATIO
    private var paddleWidth = windowWidth * Constants.NORMAL_PADDLE_RATIO

    init {
        buildGeometry((windowWidth - paddleWidth.toInt()) / 2) // Initial paddle position at roughly center
    }

    fun cleanup() {
        listOf(paddle).forEach { quad ->
            GL30.glDeleteVertexArrays(quad.vao)
            GL30.glDeleteBuffers(quad.vbo)
        }
        shader.cleanup()
    }

    fun paddleSize(): Float {
        return paddleWidth
    }

    fun onUpdatePaddleSize(x: Int, s: Float) {
        paddleWidth = windowWidth * s
        buildGeometry(x)
    }

    fun updateWindowSize(w: Int, h: Int, x:Int, s: Float) {
        windowWidth = w
        windowHeight = h
        paddleHeight = windowHeight * Constants.PADDLE_HEIGHT_RATIO
        paddleWidth = windowWidth * s
        buildGeometry(x)
    }

    fun render(paddleX: Int) {
        buildGeometry(paddleX) // Rebuild geometry to update paddle position

        shader.use()
        // Projection for Window Coordinates
        val proj =
            Matrix4f().ortho2D(0f, windowWidth.toFloat(), 0f, windowHeight.toFloat()).get(FloatArray(16))
        shader.setUniformVec2("uPaddlePos", paddleX.toFloat(), 0.0f) // Y flipped
        shader.setUniformMat4("uProjection", proj)
        shader.setUniformVec3("uColor", Constants.PADDLE_COLOR_R, Constants.PADDLE_COLOR_G, Constants.PADDLE_COLOR_B)

        // Draw paddle
        GL30.glBindVertexArray(paddle.vao)
        GL30.glDrawArrays(GL30.GL_QUADS, 0, 4)

        GL30.glBindVertexArray(0)
    }

    private fun buildQuad(x: Float,  w: Float, h: Float): Quad {
        // Vertices for a quad (two triangles); GL_QUADS is deprecated but used for simplicity
        val vertices = floatArrayOf(
            x, Constants.PADDLE_MARGIN,
            x + w, Constants.PADDLE_MARGIN,
            x + w, Constants.PADDLE_MARGIN + h,
            x, Constants.PADDLE_MARGIN + h
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

    private fun buildGeometry(x: Int) {
        // The paddle is built at y=0; its position is set in the shader uniform during render
        paddle = buildQuad(x.toFloat(), paddleWidth, paddleHeight)
    }
}
