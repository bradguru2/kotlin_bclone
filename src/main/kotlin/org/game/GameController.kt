package org.game

import org.lwjgl.glfw.GLFW.glfwPollEvents
import org.lwjgl.glfw.GLFW.glfwSwapBuffers
import org.lwjgl.glfw.GLFW.glfwWindowShouldClose
import org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT
import org.lwjgl.opengl.GL11.glClear

class GameController(private val window: Long) {
    private val paddle = Paddle(PaddleShader())
    private val ball = Ball(BallShader())
    private val bricks = mutableListOf<Brick>() // assign BrickShader per brick
    private val hud = Hud(RetroFont.loadDefault(), HudShader())
    private val soundManager = SoundManager()

    private var gameOver = false

    fun execute() {
        initLevel()
        while (!glfwWindowShouldClose(window) && !gameOver) {
            pollInput()
            update()
            render()
            glfwSwapBuffers(window)
            glfwPollEvents()
        }
        cleanup()
    }

    private fun initLevel() {
        // Create bricks, set positions, assign shaders
    }

    private fun pollInput() {
        // Keyboard input for paddle movement
    }

    private fun update() {
        // Update ball movement, collision, score
    }

    private fun render() {
        glClear(GL_COLOR_BUFFER_BIT)
        paddle.render()
        ball.render()
        bricks.forEach { it.render() }
        hud.render()
    }

    private fun cleanup() {
        soundManager.cleanup()
        paddle.cleanup()
        ball.cleanup()
        bricks.forEach { it.cleanup() }
        hud.cleanup()
    }
}
