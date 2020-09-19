package com.yuan.testjetpack.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.Observer
import com.yuan.testjetpack.R
import com.yuan.testjetpack.services.MyService
import kotlinx.android.synthetic.main.activity_services.*

class ServicesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)

    }

    override fun onStart() {
        super.onStart()
        Log.e("tag", "onStart  --activity");
        service.setOnClickListener {
            Intent(this, MyService::class.java).also {
                startService(it)
            }
        }

        bindservice.setOnClickListener {
            val bindIntent = Intent(this, MyService::class.java)
            val serviceConnection = object : ServiceConnection {
                override fun onServiceDisconnected(name: ComponentName?) {
                    TODO("Not yet implemented")
                }

                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    (service as MyService.MyBinder).service.numberLiveData.observe(this@ServicesActivity, Observer {
                        textview.text = "$it"
                    })
                }

            }
            startService(bindIntent)
            bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        Log.e("tag", "onStop  --activity");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("tag", "onDestroy  --activity");
    }
}