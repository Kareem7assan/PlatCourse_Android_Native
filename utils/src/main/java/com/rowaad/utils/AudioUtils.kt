package com.rowaad.utils

import android.content.Context
import android.media.MediaPlayer
import java.io.IOException

class AudioUtils(val context: Context, var mediaPlayer: MediaPlayer) {

    private var tmpMediaPlayer: MediaPlayer? = null

    fun playAudioFromUrl(url: String, callback: OnPlayCallback) {
        tmpMediaPlayer?.stop()

        try {
            mediaPlayer.apply {
                reset()
                setDataSource(url)
                prepareAsync()
                callback.onPrepare()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        mediaPlayer.setOnPreparedListener {
            tmpMediaPlayer = mediaPlayer
            mediaPlayer.start()
            callback.onStart()
        }

        mediaPlayer.setOnCompletionListener {
            callback.onFinish()
        }

    }

    fun pause() {
        if (mediaPlayer.isPlaying)
            mediaPlayer.pause()
    }

    fun resume(position: Int) {
        mediaPlayer.seekTo(position)
        mediaPlayer.start()
    }

    interface OnPlayCallback {
        fun onFinish()
        fun onPrepare()
        fun onStart()
    }

}