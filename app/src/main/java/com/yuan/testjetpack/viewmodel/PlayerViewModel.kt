package com.yuan.testjetpack.viewmodel

import android.media.MediaPlayer
import android.opengl.Visibility
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yuan.testjetpack.view.MyMediaplayer

class PlayerViewModel :ViewModel() {
    val mediaPlayer = MyMediaplayer()
    private val _progressBarVisibility = MutableLiveData(View.VISIBLE)
    val progressBarVisibility:LiveData<Int> = _progressBarVisibility
    private val _videoResolution = MutableLiveData(Pair(0,0))
    val videoResolution: LiveData<Pair<Int, Int>> = _videoResolution
    init {
        loadVideo()
    }
    fun loadVideo() {
        mediaPlayer.apply {
            _progressBarVisibility.value = View.VISIBLE
            setDataSource("https://stream7.iqilu.com/10339/upload_transcode/202002/18/20200218114723HDu3hhxqIT.mp4")
            setOnPreparedListener {
                _progressBarVisibility.value = View.INVISIBLE
                isLooping =true
                it.start()
            }
            setOnVideoSizeChangedListener { mp, width, height ->
                _videoResolution.value = Pair(width, height)
            }
            prepareAsync()
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }
}