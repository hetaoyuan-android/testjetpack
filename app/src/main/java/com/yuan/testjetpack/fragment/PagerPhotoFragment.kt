package com.yuan.testjetpack.fragment

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.yuan.testjetpack.R
import com.yuan.testjetpack.adapter.PagerPhotoListAdapter
import com.yuan.testjetpack.adapter.PagerPhotoViewHolder
import com.yuan.testjetpack.bean.PhotoItem
import kotlinx.android.synthetic.main.activity_view_pager.*
import kotlinx.android.synthetic.main.pager_photo_fragment.*
import kotlinx.android.synthetic.main.pager_photo_fragment.view.*
import kotlinx.android.synthetic.main.pager_photo_fragment.viewpager2
import kotlinx.android.synthetic.main.pager_photo_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val REQUEST_WRITE_EXTERNAL_STORAGE = 0x01

class PagerPhotoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pager_photo_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val photoList = arguments?.getParcelableArrayList<PhotoItem>("PHOTO_LIST")
        PagerPhotoListAdapter().apply {
            viewpager2.adapter = this
            submitList(photoList)
        }

        viewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                phototag.text = getString(R.string.photo_tag, position + 1, photoList?.size)
            }
        })
        viewpager2.setCurrentItem(arguments?.getInt("PHOTO_POSITION") ?: 0, false)
        imageView.setOnClickListener {
            if (Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(requireContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_WRITE_EXTERNAL_STORAGE)
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    savePhoto()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        savePhoto()
                    }
                } else {
                    MainScope().launch { Toast.makeText(requireContext(), "存储失败", Toast.LENGTH_SHORT).show() }
                }
            }
        }
    }

    suspend fun savePhoto() {

        //之前的版本
        /*val holder = (viewpager2[0] as RecyclerView).findViewHolderForAdapterPosition(viewpager2.currentItem)
                as PagerPhotoViewHolder
        val bitmap = holder.itemView.pagerPhoto.drawable.toBitmap()
        if (MediaStore.Images.Media.insertImage(requireContext().contentResolver, bitmap, "", "") == null) {
            Toast.makeText(requireContext(), "存储失败", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "存储成功", Toast.LENGTH_LONG).show()
        }*/

        withContext(Dispatchers.IO) {
            val holder =
                    (viewpager2[0] as RecyclerView).findViewHolderForAdapterPosition(viewpager2.currentItem)
                            as PagerPhotoViewHolder
            val bitmap = holder.itemView.pagerPhoto.drawable.toBitmap()

            val saveUri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    ContentValues()
            )?: kotlin.run {
                MainScope().launch { Toast.makeText(requireContext(), "存储失败", Toast.LENGTH_SHORT).show() }
                return@withContext
            }
            requireContext().contentResolver.openOutputStream(saveUri).use {
                if (bitmap.compress(Bitmap.CompressFormat.JPEG,90,it)) {
                    MainScope().launch {Toast.makeText(requireContext(), "存储成功", Toast.LENGTH_SHORT).show() }
                } else {
                    MainScope().launch { Toast.makeText(requireContext(), "存储失败", Toast.LENGTH_SHORT).show() }
                }
            }
        }
    }
}