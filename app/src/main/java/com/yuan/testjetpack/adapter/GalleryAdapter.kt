package com.yuan.testjetpack.adapter

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.yuan.testjetpack.R
import com.yuan.testjetpack.bean.PhotoItem
import com.yuan.testjetpack.viewmodel.DATA_STATUS_CAN_LOAD_MORE
import com.yuan.testjetpack.viewmodel.DATA_STATUS_NETWORK_ERROR
import com.yuan.testjetpack.viewmodel.DATA_STATUS_NO_MORE
import com.yuan.testjetpack.viewmodel.GalleryViewModel
import kotlinx.android.synthetic.main.gallery_footer.view.*
import kotlinx.android.synthetic.main.gallery_item.view.*

class GalleryAdapter(val galleryViewModel: GalleryViewModel) : ListAdapter<PhotoItem, MyViewHolder>(DIFFCALLBACK) {

    companion object {
        const val NORMAL_VIEW_TYPE = 0
        const val FOOTER_VIEW_TYPE = 1
    }

    var footerViewStatus = DATA_STATUS_CAN_LOAD_MORE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val holder: MyViewHolder
        if (viewType == NORMAL_VIEW_TYPE) {
            holder = MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false))
            holder.itemView.setOnClickListener {
                Bundle().apply {
//                putParcelable("PHOTO",getItem(holder.adapterPosition))
//                holder.itemView.findNavController().navigate(R.id.action_galleryFragment_to_photoFragment,this)
                    putParcelableArrayList("PHOTO_LIST", ArrayList(currentList))
                    putInt("PHOTO_POSITION", holder.adapterPosition)
                    holder.itemView.imageView.findNavController().navigate(R.id.action_galleryFragment_to_pagerPhotoFragment, this)
                }
            }
        } else {
            holder = MyViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.gallery_footer,
                            parent,
                            false
                    ).also {
                        (it.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
                        it.setOnClickListener { view ->
                            view.progressBar2.visibility = View.VISIBLE
                            view.textView6.text = "正在加载"
                            galleryViewModel.fetchData()
                        }
                    }
            )
        }

        return holder
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) FOOTER_VIEW_TYPE else NORMAL_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (position == itemCount -1) {
            with(holder.itemView) {
                when(footerViewStatus) {
                    DATA_STATUS_CAN_LOAD_MORE -> {
                        progressBar2.visibility = View.VISIBLE
                        textView6.text = "正在加载"
                        isClickable = false
                    }
                    DATA_STATUS_NO_MORE -> {
                        progressBar2.visibility = View.GONE
                        textView6.text = "全部加载完毕"
                        isClickable = false
                    }
                    DATA_STATUS_NETWORK_ERROR -> {
                        progressBar2.visibility = View.GONE
                        textView6.text = "网络故障,稍后重试"
                        isClickable = true
                    }
                }
            }
            return
        }

        holder.itemView.shimmerLayoutCell.apply {
            setShimmerColor(0x55FFFFFF)
            setShimmerAngle(0)
            startShimmerAnimation()
        }

        holder.itemView.imageView.layoutParams.height = getItem(position).photoHeight

        Glide.with(holder.itemView)
                .load(getItem(position).previewUrl)
                .placeholder(R.drawable.ic_baseline_gray_24)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: com.bumptech.glide.load.DataSource?, isFirstResource: Boolean): Boolean {
                        return false.also { holder.itemView.shimmerLayoutCell?.stopShimmerAnimation() }
                    }

                })
                .into(holder.itemView.imageView)

    }

    object DIFFCALLBACK : DiffUtil.ItemCallback<PhotoItem>() {
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem.photoId === newItem.photoId
        }

        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem == newItem
        }
    }


}


class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    val shimmerLayoutCell: ShimmerLayout = itemView.findViewById(R.id.shimmerLayoutCell)
//    val imageView: ImageView = itemView.findViewById(R.id.imageView)
}