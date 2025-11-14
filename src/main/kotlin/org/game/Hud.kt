package org.game

class Hud(private val font: RetroFont, private val shader: ShaderProgram) {
    var score = 0
    var lives = 3
    var level = 1

    fun render() {
        shader.use()
        font.drawText("Score: $score  Lives: $lives  Level: $level", 10f, 560f)
    }

    fun cleanup() {
        shader.cleanup()
    }
}
