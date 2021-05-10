package com.ex

import com.example.postit.adapter.ImageAdapter


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.postit.R
import com.example.postit.network.model.FindBoard

class BoardAdapter(
    private var likeBoard: (Int) -> Unit,
    private var intentToComments: (Int) -> Unit,
    private var intentToProfile: (Int) -> Unit,
    private var deleteBoard: (Int) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val boardList = arrayListOf<FindBoard?>()
    lateinit var context: Context

    override fun getItemViewType(position: Int): Int {
        val board = boardList[position]
        if (null != board?.images) {
            return 0
        } else if (null != board) {
            return 1
        } else {
            return -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        when (viewType) {
            0 -> {
                return BoardViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.rv_board_item, parent, false)
                )
            }
            1 -> {
                return BoardViewHolderNoImages(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.rv_board_item_no_image, parent, false)
                )
            }
            else -> {
                return LoadingViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.rv_item_loading, parent, false)
                )
            }
        }
    }


    inner class BoardViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvUserName = view.findViewById<TextView>(R.id.tv_rv_item_username)
        val tvContents = view.findViewById<TextView>(R.id.tv_rv_item_contents)
        val imgProfile = view.findViewById<ImageView>(R.id.img_rv_item_profile)
        val rvImage = view.findViewById<RecyclerView>(R.id.rv_item_rv)
        val tvLikeCount = view.findViewById<TextView>(R.id.tv_like_count_rv_item_image)
        val btnLike = view.findViewById<Button>(R.id.btn_like_rv_item_image)
        val btnComments = view.findViewById<Button>(R.id.btn_comment_rv_item_image)
        val btnMore = view.findViewById<ImageButton>(R.id.btn_more_rv_item)

        fun bind(board: FindBoard) {
            tvContents.text = board.contents
            tvUserName.text = board.user.userName
            tvLikeCount.text = board.likeNum.toString() + "명"
            btnLike.isSelected = board.like

            btnLike.setOnClickListener {
                likeBoard.invoke(board.boardId)
                if (!btnLike.isSelected) {
                    btnLike.isSelected = true
                    tvLikeCount.text = (board.likeNum + 1).toString() + "명"
                    board.likeNum++
                } else {
                    btnLike.isSelected = false
                    tvLikeCount.text = (board.likeNum - 1).toString() + "명"
                    board.likeNum--
                }
                board.like = !board.like
            }
            btnComments.setOnClickListener {
                intentToComments.invoke(board.boardId)
            }

            val snapHelper = PagerSnapHelper()
            rvImage.layoutManager = LinearLayoutManager(context).also {
                it.orientation = LinearLayoutManager.HORIZONTAL
            }
            val boardImageList = arrayListOf<String?>()
            val boardImages = board.images.toString().split(",")
            for (i in boardImages) boardImageList.add(i)
            rvImage.adapter = ImageAdapter(boardImageList, true)
            rvImage.onFlingListener = null
            snapHelper.attachToRecyclerView(rvImage)
            if (board.user.profile != 0) {
                Glide.with(context)
                    .load(board.user.profile)
                    .into(imgProfile)
            }
            imgProfile.setOnClickListener {
                intentToProfile.invoke(board.userId)
            }
            btnMore.setOnClickListener {
                val popUpMenu = PopupMenu(context, btnMore)
                MenuInflater(context).inflate(R.menu.menu_more, popUpMenu.menu)
                popUpMenu.setOnMenuItemClickListener { item ->
                    if (item.itemId == R.id.menu_delete) {
                        deleteBoard.invoke(board.boardId)
                        deleteBoard(board)
                    }
                    false
                }
                popUpMenu.show()
            }
            tvUserName.setOnClickListener { Log.d("board", board.boardId.toString()) }
        }
    }


    inner class BoardViewHolderNoImages(val view: View) : RecyclerView.ViewHolder(view) {
        val tvUserName = view.findViewById<TextView>(R.id.tv_rv_item_username)
        val tvContents = view.findViewById<TextView>(R.id.tv_rv_item_contents_no_image)
        val imgProfile = view.findViewById<ImageView>(R.id.img_rv_item_profile)
        val tvLikeCount = view.findViewById<TextView>(R.id.tv_like_count_rv_item_no_image)
        val btnLike = view.findViewById<Button>(R.id.btn_like_rv_item_no_image)
        val btnComments = view.findViewById<Button>(R.id.btn_comment_rv_item_no_image)
        val btnMore = view.findViewById<ImageButton>(R.id.btn_more_rv_item)

        fun bind(board: FindBoard) {
            tvContents.text = board.contents
            tvUserName.text = board.user.userName
            tvLikeCount.text = board.likeNum.toString() + "명"

            if (board.user.profile != 0) {
                Glide.with(context)
                    .load(board.user.profile)
                    .into(imgProfile)
            }
            btnLike.isSelected = board.like

            btnLike.setOnClickListener {
                likeBoard.invoke(board.boardId)
                if (!btnLike.isSelected) {
                    btnLike.isSelected = true
                    tvLikeCount.text = (board.likeNum + 1).toString() + "명"
                    board.likeNum++
                } else {
                    btnLike.isSelected = false
                    tvLikeCount.text = (board.likeNum - 1).toString() + "명"
                    board.likeNum--
                }
                board.like = !board.like
            }
            btnComments.setOnClickListener {
                intentToComments.invoke(board.boardId)
            }
            imgProfile.setOnClickListener {
                Log.d("profile", board.boardId.toString())
                intentToProfile.invoke(board.userId)
            }
            btnMore.setOnClickListener {
                val popUpMenu = PopupMenu(context, btnMore)
                MenuInflater(context).inflate(R.menu.menu_more, popUpMenu.menu)
                popUpMenu.setOnMenuItemClickListener { item ->
                    if (item.itemId == R.id.menu_delete) {
                        deleteBoard.invoke(board.boardId)
                        deleteBoard(board)
                    }
                    false
                }
                popUpMenu.show()
            }
            tvUserName.setOnClickListener { Log.d("board", board.boardId.toString()) }

        }
    }

    inner class LoadingViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val pb = view.findViewById<ProgressBar>(R.id.progressBar_bottom)
    }


    override fun getItemCount(): Int {
        return boardList.size
    }


    fun setList(list: List<FindBoard>) {
        for (board in list) {
            boardList.add(board)
        }
        notifyDataSetChanged()
    }

    fun removeProgressBar() {
        boardList.remove(null)
        notifyItemRemoved(boardList.lastIndex)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BoardViewHolder) {
            holder.bind(boardList[position]!!)
        } else if (holder is BoardViewHolderNoImages) {
            holder.bind(boardList[position]!!)
        }
    }

    fun showProgressBar() {
        boardList.add(null)
        notifyDataSetChanged()
    }

    fun resetBoards() {
        boardList.clear()
        notifyDataSetChanged()
    }

    fun deleteBoard(target: FindBoard) {
        Log.d("board", target.toString())
        if (target.canDelete) {
            boardList.remove(target)
            notifyDataSetChanged()
        } else {
            Toast.makeText(context, "권한이 없습니다", Toast.LENGTH_SHORT).show()
        }
    }


}

