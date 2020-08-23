package com.yuan.testjetpack.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.yuan.testjetpack.R
import com.yuan.testjetpack.adapter.GalleryAdapter
import com.yuan.testjetpack.viewmodel.DATA_STATUS_NETWORK_ERROR
import com.yuan.testjetpack.viewmodel.GalleryViewModel
import kotlinx.android.synthetic.main.gallery_fragment.*

class GalleryFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.gallery_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val galleryViewModel: GalleryViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(GalleryViewModel::class.java)
        var galleryAdapter = GalleryAdapter(galleryViewModel)
        recyclerview.apply {
            adapter = galleryAdapter
            layoutManager = StaggeredGridLayoutManager(2, 1)
        }
        galleryViewModel.photoListLive.observe(viewLifecycleOwner, Observer {

            if (galleryViewModel.needToScrollTop) {
                recyclerview.scrollToPosition(0)
                galleryViewModel.needToScrollTop = false
            }
            galleryAdapter.submitList(it)
            swipeLayout.isRefreshing = false
        })

        galleryViewModel.dataStatusLive.observe(viewLifecycleOwner, Observer {
            galleryAdapter.footerViewStatus = it
            galleryAdapter.notifyItemChanged(galleryAdapter.itemCount - 1)
            if (it == DATA_STATUS_NETWORK_ERROR) swipeLayout.isRefreshing = false
        })

        // galleryViewModel.photoListLive.value?:galleryViewModel.fetchData()
        swipeLayout.setOnRefreshListener{
            galleryViewModel.resetQuery()
        }

        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) return
                val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
                val intArray = IntArray(2)
                layoutManager.findLastVisibleItemPositions(intArray)
                if (intArray[0] == galleryAdapter.itemCount -1) {
                    galleryViewModel.fetchData()
                }
            }
        })
    }

}