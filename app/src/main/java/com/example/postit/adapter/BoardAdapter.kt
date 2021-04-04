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
    private var VIEW_TYPE = true
    private var IMAGE_CHK = true
    private val boardList = arrayListOf<Res.FindBoard>()
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        context = parent.context
        if (VIEW_TYPE) {
            if (IMAGE_CHK)
                return BoardViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.rv_board_item, parent, false)
                )
            else {
                return BoardViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.rv_board_item_no_image, parent, false)
                )
            }
        } else {
            return BoardViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.rv_item_loading, parent, false)
            )
        }
    }
    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        TODO("Not yet implemented")
    }



    inner class BoardViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvUserName = view.findViewById<TextView>(R.id.tv_rv_item_username)
        val tvContents = view.findViewById<TextView>(R.id.tv_rv_item_contents)
        val imgProfile = view.findViewById<ImageView>(R.id.img_rv_item_profile)
        val rvImage = view.findViewById<RecyclerView>(R.id.rv_item_rv)
        val tvLikeCount = view.findViewById<TextView>(R.id.tv_rv_item_like_count)

        fun bind(board: Res.FindBoard) {
            tvContents.text = board.contents
            tvUserName.text = board.user.userName
            tvLikeCount.text = board.likeNum.toString()

            if (VIEW_TYPE && IMAGE_CHK) {
                val snapHelper = PagerSnapHelper()
//                rvImage.apply {
//                    layoutManager = LinearLayoutManager(context).also {
//                        it.orientation = LinearLayoutManager.HORIZONTAL
//                    }
//                    adapter = ImageAdapter(board.images as List<String>)
//                }
//                snapHelper.attachToRecyclerView(rvImage)
                if (board.user.profile != 0) {
                    Glide.with(context)
                        .load(board.user.profile)
                        .into(imgProfile)
                }
            }
        }
    }
    inner class BoardViewHolderNoImages(val view: View):RecyclerView.ViewHolder(view){

    }


    override fun getItemCount(): Int {
        return boardList.size
    }




    fun setList(list: List<Res.FindBoard>) {
        for (board in list) {
            boardList.add(board)
        }
        notifyDataSetChanged()
    }

    fun removeProgressBar() {
        notifyItemRemoved(boardList.size)
        notifyDataSetChanged()
    }

    fun setViewType(chk: Boolean) {
        VIEW_TYPE = chk
    }

    fun setImages(chk: Boolean) {
        IMAGE_CHK = chk
    }



}