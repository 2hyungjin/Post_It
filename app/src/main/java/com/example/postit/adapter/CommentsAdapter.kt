package com.example.postit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.postit.R
import com.example.postit.network.model.Comment

class CommentsAdapter() : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {
    lateinit var context:Context
    private val commentsList= arrayListOf<Comment>()
    inner class CommentsViewHolder(view : View): RecyclerView.ViewHolder(view){
        val imgProfile: ImageView =view.findViewById<ImageView>(R.id.img_rv_item_profile)
        val tvUserName: TextView =view.findViewById<TextView>(R.id.tv_rv_item_username_comments)
        val tvComment: TextView =view.findViewById<TextView>(R.id.tv_rv_item_comment_comments)
        fun bind(comment: Comment){
            if (comment.user.profile!=0){
                Glide.with(context)
                    .load(comment.user.profile)
                    .into(imgProfile)
            }
            tvUserName.text=comment.user.name
            tvComment.text=comment.comment
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        context=parent.context
        return CommentsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item_comment,parent,false))
    }

    override fun getItemCount(): Int {
        return commentsList.size
    }

    override fun onBindViewHolder(holder: CommentsAdapter.CommentsViewHolder, position: Int) {
        holder.bind(commentsList[position])
    }
}