package org.game

class SoundManager {
    private val sounds = mutableMapOf<String, Int>()

    fun loadSound(name: String, path: String) { /* OpenAL buffer */ }
    fun play(name: String) { /* play sound */ }
    fun cleanup() { /* free OpenAL buffers */ }
}
