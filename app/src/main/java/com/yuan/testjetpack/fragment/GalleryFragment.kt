package com.yuan.testjetpack.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.yuan.testjetpack.R
import com.yuan.testjetpack.adapter.GalleryAdapter
import com.yuan.testjetpack.viewmodel.GalleryViewModel
import kotlinx.android.synthetic.main.gallery_fragment.*

class GalleryFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.gallery_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var galleryAdapter = GalleryAdapter()
        recyclerview.apply {
            adapter = galleryAdapter
            layoutManager = StaggeredGridLayoutManager(2, 1)
        }
        val galleryViewModel: GalleryViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(GalleryViewModel::class.java)
        galleryViewModel.photoListLive.observe(viewLifecycleOwner, Observer {
            galleryAdapter.submitList(it)
            swipeLayout.isRefreshing = false
        })

        galleryViewModel.photoListLive.value?:galleryViewModel.fetchData()
        swipeLayout.setOnRefreshListener{
            galleryViewModel.fetchData()
        }
    }

}