package com.example.postit.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.postit.R
import kotlinx.android.synthetic.main.fragment_add.*

class ImgRvAdapter : RecyclerView.Adapter<ImgRvAdapter.ImgViewHolder>() {
    private var imgList= ArrayList<Uri>()
    private lateinit var context: Context
    inner class ImgViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img=view.findViewById<ImageView>(R.id.rv_item_img)
        fun bind(uri: Uri){
            Glide.with(context)
                .load(uri)
                .centerCrop()
                .into(img)
            img.setOnClickListener {
                imgList.remove(uri)
                notifyDataSetChanged()
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImgRvAdapter.ImgViewHolder {
        context=parent.context
        return ImgViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item_add_img,parent,false))
    }

    override fun onBindViewHolder(holder: ImgRvAdapter.ImgViewHolder, position: Int) {
        holder.bind(imgList[position])
    }

    override fun getItemCount(): Int {
        return imgList.size
    }
    fun setList(list:ArrayList<Uri>){
        imgList=list
        notifyDataSetChanged()
    }
}




