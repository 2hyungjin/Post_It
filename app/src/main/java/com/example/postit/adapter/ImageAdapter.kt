package com.example.postit.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.postit.R
import kotlinx.android.synthetic.main.rv_item_add_img.view.*

class ImageAdapter(val imgList: ArrayList<String>, val VIEW_TYPE: Boolean) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    lateinit var context: Context

    inner class ImageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        lateinit var imgItem: ImageView
        fun bind() {
            if (VIEW_TYPE) imgItem = view.findViewById(R.id.img_rv_item_image)
            else imgItem = view.findViewById(R.id.img_rv_item_img_post)
            Glide.with(context)
                .load(imgList[adapterPosition])
                .centerCrop()
                .into(imgItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (VIEW_TYPE) return 0
        else return 1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageAdapter.ImageViewHolder {
        context = parent.context
        if (VIEW_TYPE){
            return ImageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.rv_image_item, parent, false)
            )
        }else{
            return ImageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.rv_img_item_post,parent,false)
            )
        }

    }

    override fun getItemCount(): Int {
        return imgList.size
    }

    override fun onBindViewHolder(holder: ImageAdapter.ImageViewHolder, position: Int) {
        holder.bind()
    }

    fun setList(imgList: ArrayList<String>) {
        this.imgList.addAll(imgList)
        Log.d("postit",this.imgList.toString())
        notifyDataSetChanged()
    }
}