package org.game

class BallShader : ShaderProgram(
    vertexSource = """
        #version 330 core
        layout(location = 0) in vec2 aPos;
        uniform vec3 objectPos;
        uniform mat4 projection;
        void main() {
            gl_Position = projection * vec4(aPos + objectPos.xy, objectPos.z, 1.0);
        }
    """.trimIndent(),
    fragmentSource = """
        #version 330 core
        out vec4 FragColor;
        uniform vec3 objectColor;
        void main() {
            FragColor = vec4(objectColor, 1.0);
        }
    """.trimIndent()
)
