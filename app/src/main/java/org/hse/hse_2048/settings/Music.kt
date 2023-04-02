package org.hse.hse_2048.settings

import android.content.Context
import android.media.MediaPlayer

class Music {

    var player: MediaPlayer = MediaPlayer()
    var pathR: Int
    private var volume = 0.5f

    constructor(pathR: Int){
        this.pathR = pathR
    }

    fun playMusic(context: Context) {
        player = MediaPlayer.create(context, pathR)
        player.isLooping = true
        player.start()
    }

    fun stopMusic(){
        player.stop()
    }

    fun setVolume(vol: Float) {
        volume = vol
        player.setVolume(volume, volume)
    }
}