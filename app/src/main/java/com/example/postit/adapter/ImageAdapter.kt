package com.example.postit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.postit.R
import kotlinx.android.synthetic.main.rv_item_add_img.view.*

class ImageAdapter(val imgList: ArrayList<String>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    lateinit var context:Context
    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgItem=view.findViewById<ImageView>(R.id.img_rv_item_image)
        fun bind(){
            Glide.with(context)
                .load(imgList[adapterPosition])
                .centerCrop()
                .into(imgItem)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageAdapter.ImageViewHolder {
        context=parent.context
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rv_image_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return imgList.size
    }

    override fun onBindViewHolder(holder: ImageAdapter.ImageViewHolder, position: Int) {
        holder.bind()
    }
}