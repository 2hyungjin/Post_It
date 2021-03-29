package com.example.postit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.postit.R
import com.example.postit.network.model.Res

class BoardAdapter : RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {
    val boardList = arrayListOf<Res.FindBoard>()
    lateinit var context: Context

    inner class BoardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUserName = view.findViewById<TextView>(R.id.tv_rv_item_username)
        val tvLikeCount = view.findViewById<TextView>(R.id.tv_rv_item_like_count)
        val rvImage = view.findViewById<RecyclerView>(R.id.rv_item_rv)
        val imgProfile = view.findViewById<ImageView>(R.id.img_rv_item_profile)

        fun bind(board: Res.FindBoard) {
            tvUserName.text = board.user.userName
            tvLikeCount.text = board.likeNum.toString()
            if (board.user.profile != 0) {
                Glide.with(context)
                    .load(board.user.profile)
                    .into(imgProfile)
            }
            if (board.images != null) {
                val snapHelper = PagerSnapHelper()

                rvImage.apply {
                    layoutManager = LinearLayoutManager(context).also {
                        it.orientation = LinearLayoutManager.HORIZONTAL
                    }
                    adapter = ImageAdapter(board.images as List<String>)
                }

                snapHelper.attachToRecyclerView(rvImage)
            }


        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BoardAdapter.BoardViewHolder {
        context = parent.context
        return BoardViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rv_board_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

    override fun onBindViewHolder(holder: BoardAdapter.BoardViewHolder, position: Int) {
        holder.bind(boardList[position])
    }

    fun getList(list: List<Res.FindBoard>) {
        for (board in list) {
            boardList.add(board)
        }
        notifyDataSetChanged()
    }
}