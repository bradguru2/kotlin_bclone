package org.game

import org.lwjgl.glfw.GLFW.glfwPollEvents
import org.lwjgl.glfw.GLFW.glfwSwapBuffers
import org.lwjgl.glfw.GLFW.glfwWindowShouldClose
import org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT
import org.lwjgl.opengl.GL11.glClear

class GameController(window: Long, width:Int, height:Int) {
    private val paddle = Paddle(PaddleShader(), width, height)
    private val ball = Ball(BallShader())
    private val bricks = mutableListOf<Brick>() // assign BrickShader per brick
    private val soundManager = SoundManager()
    private val retroFont = RetroFont()                 // defaults: 8x12 glyphs, 16x6 grid
    private val hud = Hud(retroFont, HudShader(), width, height)
    private val frame = Frame(FrameShader(), width, height)
    private var score = 0
    private var balls = 3
    private var isLevelEvent = true
    private var level = 0
    private var gameWindow = window
    private var windowWidth = width
    private var windowHeight = height
    private var gameOver = false
    private var paddleState = Constants.NORMAL_PADDLE_RATIO
    private var paddleX = windowWidth / 2

    fun execute() {
        while (!glfwWindowShouldClose(gameWindow) && !gameOver) {
            if (isLevelEvent) {
                level++
                initLevel()
                isLevelEvent = false
            }
            pollInput()
            update()
            render()
            glfwSwapBuffers(gameWindow)
            glfwPollEvents()
        }
        cleanup()
    }

    fun onResizeWindow(newWindow: Long,newWidth: Int, newHeight: Int) {
        gameWindow = newWindow
        windowWidth = newWidth
        windowHeight = newHeight
        // Update state as needed
        hud.updateWindowSize(windowWidth, windowHeight)
        frame.updateWindowSize(windowWidth, windowHeight)
        paddle.updateWindowSize(windowWidth, windowHeight, paddleX, paddleState)
    }

    private fun initLevel() {
        // Create bricks, set positions, assign shaders
        paddleX = ((windowWidth - paddle.paddleSize()) / 2).toInt()
    }

    private fun pollInput() {
        // Keyboard input for paddle movement
    }

    private fun update() {
        // Update ball movement, collision, score
    }

    private fun render() {
        glClear(GL_COLOR_BUFFER_BIT)
        paddle.render(paddleX)
        ball.render()
        bricks.forEach { it.render() }
        hud.render(score, balls)
        frame.render()
    }

    private fun cleanup() {
        soundManager.cleanup()
        paddle.cleanup()
        ball.cleanup()
        bricks.forEach { it.cleanup() }
        hud.cleanup()
        frame.cleanup()
    }
}
