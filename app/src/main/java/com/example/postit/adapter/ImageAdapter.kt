package com.example.postit.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.postit.R

class ImageAdapter(val imgList: ArrayList<String?>, val VIEW_TYPE: Boolean) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    lateinit var context: Context

    inner class ImageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        lateinit var imgItem: ImageView
        fun bind(img: String) {
            if (VIEW_TYPE) imgItem = view.findViewById(R.id.img_rv_item_image)
            else {
                val btnRemove=view.findViewById<ImageButton>(R.id.btn_rv_item_remove)
                imgItem = view.findViewById(R.id.img_rv_item_img_post)
                btnRemove.setOnClickListener {
                    deleteAtList(img)
                    Toast.makeText(context, "이미지가 삭제되었습니다", Toast.LENGTH_SHORT).show()
                }
            }
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
        if (VIEW_TYPE) {
            return ImageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.rv_image_item, parent, false)
            )
        } else {
            return ImageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.rv_img_item_post, parent, false)
            )
        }

    }

    override fun getItemCount(): Int {
        return imgList.size
    }

    override fun onBindViewHolder(holder: ImageAdapter.ImageViewHolder, position: Int) {
        holder.bind(imgList[position]!!)
    }

    fun setList(imgList: ArrayList<String>) {
        this.imgList.addAll(imgList)
        Log.d("post123", this.imgList.toString())
        notifyDataSetChanged()
    }

    fun deleteAtList(img: String) {
        imgList.remove(img)
        notifyDataSetChanged()
    }
}