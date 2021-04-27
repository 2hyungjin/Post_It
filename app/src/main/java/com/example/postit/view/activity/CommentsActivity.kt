package com.example.postit.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.postit.R
import com.example.postit.adapter.CommentsAdapter
import com.example.postit.network.model.Comment
import com.example.postit.repository.AppRepo
import com.example.postit.viewmodel.BoardVM
import com.example.postit.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_board.*
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.activity_comments.toolbar_comments

class CommentsActivity : AppCompatActivity() {
    lateinit var commentsAdapter: CommentsAdapter
    lateinit var commentsViewModel: BoardVM
    private val commentList= arrayListOf<Comment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        init()
        commentsViewModel.commentsRes.observe(this, Observer {res->
            progress_bar_comments.visibility=View.GONE
            commentList.addAll(res.comments)
            commentsAdapter.setComments(commentList)
        })
    }

    fun init() {
        commentsAdapter = CommentsAdapter {
            intentToProfile(it)
        }
        val repo = AppRepo()
        val factory = ViewModelProviderFactory(repo)
        commentsViewModel = ViewModelProvider(this, factory).get(BoardVM::class.java)
        rvInit()
        toolBarInit()
        loadComments()
    }
    fun loadComments(){
        progress_bar_comments.visibility=View.VISIBLE
        val boardId=intent.getIntExtra("boardId",0)
        commentsViewModel.getComments(boardId)
    }

    fun rvInit() {
        rv_comments.apply {
            layoutManager = LinearLayoutManager(this@CommentsActivity)
            adapter = commentsAdapter
        }
    }

    fun intentToProfile(userId: Int) {
        Log.d("comments", "intent to profile")
    }

    fun toolBarInit(){
        setSupportActionBar(toolbar_comments)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}