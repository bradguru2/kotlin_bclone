package org.game

class RetroFont(private val textureId: Int) {
    fun drawText(text: String, x: Float, y: Float) {
        // bind texture atlas, render quads per character
    }

    companion object {
        fun loadDefault(): RetroFont {
            // Load bundled retro bitmap font
            return RetroFont(0)
        }
    }
}
