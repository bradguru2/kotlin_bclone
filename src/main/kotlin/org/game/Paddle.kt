package org.game

class Paddle(private val shader: ShaderProgram) {
    private val vao: Int = 0
    private val vbo: Int = 0
    var x = 400f
    var y = 50f
    var width = 100f
    var height = 20f

    init { /* setup VAO/VBO with rectangle vertices */ }

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
