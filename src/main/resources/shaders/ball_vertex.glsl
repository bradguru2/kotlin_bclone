#version 330 core
layout(location = 0) in vec2 aPos;
uniform vec3 objectPos;
uniform mat4 projection;
void main() {
    gl_Position = projection * vec4(aPos + objectPos.xy, objectPos.z, 1.0);
}