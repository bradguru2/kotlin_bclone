package org.game

class HudShader : ShaderProgram(
    vertexSource = """
        #version 330 core
        layout(location = 0) in vec2 aPos;
        layout(location = 1) in vec2 aTexCoord;
        out vec2 TexCoord;
        uniform vec2 objectPos;
        uniform mat4 projection;
        void main() {
            gl_Position = projection * vec4(aPos + objectPos, 0.0, 1.0);
            TexCoord = aTexCoord;
        }
    """.trimIndent(),
    fragmentSource = """
        #version 330 core
        in vec2 TexCoord;
        out vec4 FragColor;
        uniform sampler2D fontTexture;
        uniform vec3 textColor;
        void main() {
            float alpha = texture(fontTexture, TexCoord).r;
            FragColor = vec4(textColor, alpha);
        }
    """.trimIndent()
)
