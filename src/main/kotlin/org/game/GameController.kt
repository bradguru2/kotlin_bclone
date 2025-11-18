package org.game

import org.joml.Math.clamp
import org.lwjgl.glfw.GLFW.GLFW_KEY_A
import org.lwjgl.glfw.GLFW.GLFW_KEY_D
import org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT
import org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.glfwGetKey
import org.lwjgl.glfw.GLFW.glfwPollEvents
import org.lwjgl.glfw.GLFW.glfwSwapBuffers
import org.lwjgl.glfw.GLFW.glfwWindowShouldClose
import org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT
import org.lwjgl.opengl.GL11.glClear
import kotlin.math.roundToInt

class GameController(window: Long, width:Int, height:Int) {

    // Track bricks
    data class Brick (
        var brickX: Int,
        var brickY: Int,
        var isActive: Boolean,
        var brickColor: Triple<Float, Float, Float>,
        val renderer: BrickRenderer,
    )

    val brickColorArray = arrayOf(
        Triple(0.75f, 0f, 0f),
        Triple(0f, 0.75f, 0f),
        Triple(0f, 0f, 0.75f),
        Triple(0.75f, 0.75f, 0.75f),
        Triple(0.75f, 0f, 0.75f),
        Triple(0.75f, 0.75f, 0f),
        Triple(0f, 0.75f, 0.75f),
        Triple(0.633f, 0.333f, 0.333f)
    )

    private var paddleRenderer = PaddleRenderer(PaddleShader(), width, height)
    private val ballRenderer = BallRenderer(BallShader(), width, height)
    private val bricks = mutableListOf<Brick>() // assign BrickShader per brick
    private val soundManager = SoundManager()
    private var hudRenderer = HudRenderer(RetroFont(), HudShader(), width, height)
    private var frameRenderer = FrameRenderer(FrameShader(), width, height)
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
    private var brickCount = Constants.BRICK_COLUMN_COUNT * Constants.BRICK_ROW_COUNT
    private var brickColorIndex = -1
    private var brickWidth = (windowWidth * Constants.BRICK_WIDTH_RATIO).roundToInt()
    private var frameWidth = (windowWidth * Constants.SIDE_FRAME_RATIO).roundToInt()
    private var brickHeight = (windowHeight * Constants.BRICK_HEIGHT_RATIO).roundToInt()
    private var ballX = 0
    private var ballY = 0
    private var lastTime = System.currentTimeMillis()
    private var deltaTime = 0f
    private var paddleSpeed = 600f  // pixels per second


    fun execute() {
        while (!glfwWindowShouldClose(gameWindow) && !gameOver) {
            deltaTime = computeDeltaTime()
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
        val scaleX = 1f * newWidth / windowWidth
        val scaleY = 1f * newHeight / windowHeight
        windowWidth = newWidth
        windowHeight = newHeight

        // Update state as needed
        brickWidth = (windowWidth * Constants.BRICK_WIDTH_RATIO).roundToInt()
        frameWidth = (windowWidth * Constants.SIDE_FRAME_RATIO).roundToInt()
        brickHeight = (windowHeight * Constants.BRICK_HEIGHT_RATIO).roundToInt()

        for (brick in bricks) {
            brick.renderer.updateWindowSize(windowWidth, windowHeight)
            brick.brickX = (brick.brickX * scaleX).roundToInt()
            brick.brickY = (brick.brickY * scaleY).roundToInt()
        }

        ballRenderer.updateWindowSize(windowWidth, windowHeight)
        ballX = (ballX * scaleX).roundToInt()
        ballY = (ballY * scaleY).roundToInt()

        paddleX = (paddleX * scaleX).roundToInt()
        hudRenderer.updateWindowSize(windowWidth, windowHeight)
        frameRenderer.updateWindowSize(windowWidth, windowHeight)
        paddleRenderer.updateWindowSize(windowWidth, windowHeight, paddleState)
    }

    private fun initLevel() {
        // Create bricks, set positions, assign shaders
        brickCount = Constants.BRICK_COLUMN_COUNT * Constants.BRICK_ROW_COUNT
        paddleX = ((windowWidth - paddleRenderer.paddleSize()) / 2).roundToInt()
        if(++brickColorIndex>4) brickColorIndex = 0
        rebuildBricks() // Unnecessary but works
        ballX = (windowWidth / 2f - (windowHeight * Constants.BALL_HEIGHT_RATIO) / 2f).roundToInt()  // center horizontally
        ballY = (windowHeight * Constants.BALL_START_RATIO).roundToInt()
    }

    private fun computeDeltaTime(): Float {
        val now = System.currentTimeMillis()
        val dt = (now - lastTime) / 1000f   // convert ms → seconds
        lastTime = now
        return dt
    }

    private fun pollInput() {
        // left movement
        if (glfwGetKey(gameWindow, GLFW_KEY_LEFT) == GLFW_PRESS) {
            paddleX -= (paddleSpeed * deltaTime).toInt()
        }

        // right movement
        if (glfwGetKey(gameWindow, GLFW_KEY_RIGHT) == GLFW_PRESS) {
            paddleX += (paddleSpeed * deltaTime).toInt()
        }

        // Clamp to window boundaries
        val paddleWidth = paddleRenderer.paddleSize().roundToInt()
        paddleX = paddleX.coerceIn(
            frameWidth,                           // left wall
            windowWidth - frameWidth - paddleWidth // right wall
        )
    }


    private fun update() {
        var velocityX = 0f

        if (glfwGetKey(gameWindow, GLFW_KEY_LEFT) == GLFW_PRESS ||
            glfwGetKey(gameWindow, GLFW_KEY_A) == GLFW_PRESS) {
            velocityX -= paddleSpeed
        }

        if (glfwGetKey(gameWindow, GLFW_KEY_RIGHT) == GLFW_PRESS ||
            glfwGetKey(gameWindow, GLFW_KEY_D) == GLFW_PRESS) {
            velocityX += paddleSpeed
        }

        paddleX = (paddleX + velocityX * deltaTime).toInt()

        val paddleWidth = paddleRenderer.paddleSize().roundToInt()

        paddleX = clamp(
            paddleX,
            frameWidth,
            windowWidth - frameWidth - paddleWidth
        )
    }

    private fun rebuildBricks() {
        bricks.clear()
        val totalPixels = windowWidth - 2 * frameWidth
        val calculatedPixels = Constants.BRICK_COLUMN_COUNT * brickWidth
        val margin = (totalPixels - calculatedPixels) / 2

       var brickY = (Constants.BRICK_START_RATIO * windowHeight).roundToInt()
        for (i in 0 ..< Constants.BRICK_ROW_COUNT) {
            var brickX = frameWidth + margin
            for (j in 0 ..<Constants.BRICK_COLUMN_COUNT) {
                val brick = Brick(
                    brickX,
                    brickY,
                    true,
                    brickColorArray[i/2 + brickColorIndex],
                    BrickRenderer(BrickShader(), windowWidth, windowHeight)
                )
                bricks.add(brick)
                brickX += brickWidth
            }
            brickY += brickHeight
        }
    }

    private fun render() {
        glClear(GL_COLOR_BUFFER_BIT)
        paddleRenderer.render(paddleX)
        ballRenderer.render(ballX.toFloat(), ballY.toFloat())
        bricks.forEach { it.renderer.render(it.brickX, it.brickY, it.brickColor)}
        hudRenderer.render(score, balls)
        frameRenderer.render()
    }

    private fun cleanup() {
        soundManager.cleanup()
        paddleRenderer.cleanup()
        ballRenderer.cleanup()
        bricks.forEach { it.renderer.cleanup() }
        hudRenderer.cleanup()
        frameRenderer.cleanup()
    }
}
