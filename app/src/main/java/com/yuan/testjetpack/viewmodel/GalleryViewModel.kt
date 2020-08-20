package com.yuan.testjetpack.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.yuan.testjetpack.bean.PhotoItem
import com.yuan.testjetpack.bean.Pixabay
import com.yuan.testjetpack.volley.VolleySingleton


class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    private val _photoListLive = MutableLiveData<List<PhotoItem>>()

    val photoListLive : LiveData<List<PhotoItem>>
    get() = _photoListLive

    fun fetchData() {
        val stringRequest = StringRequest(
                Request.Method.GET,
                getUrl(),
                Response.Listener {
                    _photoListLive.value = Gson().fromJson(it, Pixabay::class.java).hits.toList()
                    Log.e("hello", it.toString())
                },
                Response.ErrorListener {
                    Log.e("hello", it.toString())
                }
        )
        VolleySingleton.getInstance(getApplication()).requestQueue.add(stringRequest)
    }

    private fun getUrl():String{
        return "https://pixabay.com/api/?key=17923462-d77de095b5efb67e704156987&q=${keyWords.random()}"
    }

    private val keyWords = arrayOf("cat", "dog", "car", "phone", "flower")

}