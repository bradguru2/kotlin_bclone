package org.game

/**
 * Constants - define common constant values for project.
 */
object Constants {
    // HUD
    const val HUD_HEIGHT_RATIO: Float = 0.05f
    const val RIGHT_PADDING_PX: Float = 20f

    // Colors
    const val TEXT_COLOR_R: Float = 1f
    const val TEXT_COLOR_G: Float = 0.647f
    const val TEXT_COLOR_B: Float = 0f

    const val FRAME_COLOR_R: Float = 0.333f
    const val FRAME_COLOR_G: Float = 0.333f
    const val FRAME_COLOR_B: Float = 0.333f

    const val PADDLE_COLOR_R: Float = 0.863f
    const val PADDLE_COLOR_G: Float = 0.816f
    const val PADDLE_COLOR_B: Float = 0.314f

    // Paddle Size Ratios
    const val NORMAL_PADDLE_RATIO: Float = 0.15f   // 15% of window width
    const val SMALL_PADDLE_RATIO: Float = 0.10f    // 10% of window width
    const val PADDLE_HEIGHT_RATIO = 0.03f     // 2% of window height
    const val PADDLE_MARGIN = 10f              // Margin from bottom of window in pixels

    // Shader Resource Paths
    const val HUD_VERTEX_SHADER_PATH = "/shaders/hud_vertex.glsl"
    const val HUD_FRAGMENT_SHADER_PATH = "/shaders/hud_fragment.glsl"
    const val FRAME_VERTEX_SHADER_PATH = "/shaders/frame_vertex.glsl"
    const val FRAME_FRAGMENT_SHADER_PATH = "/shaders/frame_fragment.glsl"
    const val PADDLE_VERTEX_SHADER_PATH = "/shaders/paddle_vertex.glsl"
    const val PADDLE_FRAGMENT_SHADER_PATH = "/shaders/paddle_fragment.glsl"
    const val BALL_VERTEX_SHADER_PATH = "/shaders/ball_vertex.glsl"
    const val BALL_FRAGMENT_SHADER_PATH = "/shaders/ball_fragment.glsl"
}
