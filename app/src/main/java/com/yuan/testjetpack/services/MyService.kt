package com.yuan.testjetpack.services

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyService : LifecycleService() {

    private var number = 0
    var numberLiveData = MutableLiveData(0)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("tag", "onStartCommand  --service")
        lifecycleScope.launch {
            while (true) {
                delay(1000)
                Log.e("tag", "onStartCommand  --service ${number++}")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("tag", "onCreate  --service")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("tag", "onDestroy --service")
    }


    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        lifecycleScope.launch {
            while (true) {
                delay(1000)
                numberLiveData.value = numberLiveData.value?.plus(1)
            }
        }
        return MyBinder()
       
    }
    
    inner class MyBinder: Binder() {
        var service:MyService = this@MyService
    }
}
