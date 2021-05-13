package com.example.postit.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.postit.R
import com.example.postit.adapter.CommentsAdapter
import com.example.postit.network.model.Comment
import com.example.postit.network.model.Req
import com.example.postit.repository.AppRepo
import com.example.postit.viewmodel.BoardVM
import com.example.postit.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.activity_comments.toolbar_comments
import kotlinx.android.synthetic.main.activity_post.*

class CommentsActivity : AppCompatActivity() {
    lateinit var commentsAdapter: CommentsAdapter
    lateinit var commentsViewModel: BoardVM
    var boardId: Int = 0

    private val commentList = arrayListOf<Comment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        init()
        commentsViewModel.getCommentsRes.observe(this, Observer { res ->
            progress_bar_comments.visibility = View.GONE
            commentList.addAll(res.comments)
            commentsAdapter.setComments(commentList)
        })
        commentsViewModel.postCommentsRes.observe(this, Observer {
            commentList.clear()
            commentsAdapter.clearComments()
            loadComments()
            commentsAdapter.setComments(commentList)
        })
        btn_post_comment_comments.setOnClickListener {
            postComments()
        }
    }

    private fun init() {
        commentsAdapter = CommentsAdapter {
            intentToProfile(it)
        }
        boardId = intent.getIntExtra("boardId", 0)
        val repo = AppRepo()
        val factory = ViewModelProviderFactory(repo)
        commentsViewModel = ViewModelProvider(this, factory).get(BoardVM::class.java)
        rvInit()
        toolBarInit()
        loadComments()
    }

    private fun loadComments() {
        progress_bar_comments.visibility = View.VISIBLE
        commentsViewModel.getComments(boardId)
    }

    private fun rvInit() {
        rv_comments.apply {
            layoutManager = LinearLayoutManager(this@CommentsActivity)
            adapter = commentsAdapter
        }
    }

    private fun toolBarInit() {
        setSupportActionBar(toolbar_comments)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
    }

    private fun postComments() {
        val content = edt_comment_content_comments.text.toString()
        edt_comment_content_comments.text=null
        commentsViewModel.postComments(Req.ReqComments(content,boardId))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun intentToProfile(userId: Int){
        val intent=Intent(this,ProfileActivity::class.java)
        intent.putExtra("userId",userId)
        startActivity(intent)
    }
}