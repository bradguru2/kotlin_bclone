#version 330 core
layout(location = 0) in vec2 aPos;
uniform vec2 uObjectPos;
uniform mat4 uProjection;
void main() {
    gl_Position = uProjection * vec4(aPos + uObjectPos, 0.0f, 1.0);
}