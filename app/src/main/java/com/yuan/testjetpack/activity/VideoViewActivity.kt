package com.yuan.testjetpack.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yuan.testjetpack.R
import com.yuan.testjetpack.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.activity_video_view.*

class VideoViewActivity : AppCompatActivity() {
    private lateinit var playerMediaModel : PlayerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_view)
        playerMediaModel = ViewModelProvider(this).get(PlayerViewModel::class.java).apply {
            progressBarVisibility.observe(this@VideoViewActivity, Observer {
                progressBar3.visibility = it
            })

            videoResolution.observe(this@VideoViewActivity, Observer {
                playerFrame.post {
                    resizePlayer(it.first, it.second)
                }
            })

        }

        lifecycle.addObserver(playerMediaModel.mediaPlayer)

        surfaceView.holder.addCallback(object : SurfaceHolder.Callback{
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                playerMediaModel.mediaPlayer.setDisplay(holder)
                playerMediaModel.mediaPlayer.setScreenOnWhilePlaying(true)
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {

            }

            override fun surfaceCreated(holder: SurfaceHolder?) {

            }

        })
    }

    private fun resizePlayer(width: Int, height: Int) {
        if (width == 0 || height == 0) return
        surfaceView.layoutParams = FrameLayout.LayoutParams(
                playerFrame.height * width / height,
                FrameLayout.LayoutParams.MATCH_PARENT,
                Gravity.CENTER
        )
    }
}