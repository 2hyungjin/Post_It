package com.example.postit.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.postit.R
import com.example.postit.adapter.BoardAdapter
import com.example.postit.repository.AppRepo
import com.example.postit.viewmodel.BoardVM
import com.example.postit.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var profileViewModel: BoardVM
    private val boardIds = arrayListOf<Int>(-1)
    private lateinit var boardAdapter: BoardAdapter
    private var LOADING_CHK: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        init()
        profileViewModel.getProfileRes.observe(this, Observer { profile ->
            Log.d("profile", profile.toString())
//            tv_user_name_profile.text = profile.user.name ?: "알 수 없음"
            if (profile.user.profile != 0) {
                Glide.with(this)
                    .load(profile.user.profile)
                    .into(img_profile_profile)
            }
            LOADING_CHK = true

        })
    }

    private fun init() {
        initViewModel()
        initToolBar()
        initAdapter()
        initRecyclerView()
        getProfile()
    }

    private fun initToolBar() {
        setSupportActionBar(toolbar_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
    }

    private fun initViewModel() {
        val repo = AppRepo()
        val factory = ViewModelProviderFactory(repo)
        profileViewModel = ViewModelProvider(this, factory).get(BoardVM::class.java)
    }

    private fun initAdapter() {
        boardAdapter = BoardAdapter({
            likeBoard(it)
        }, {
            intentToComments(it)
        }, {
            //nothing to do
        })
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this@ProfileActivity)
        rv_board_profile.apply {
            layoutManager = linearLayoutManager
            adapter = boardAdapter
        }
        rv_board_profile.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == boardIds.lastIndex - 1) {
                    if (LOADING_CHK) {
                        getProfile()
                        LOADING_CHK = false
                    }
                }
            }
        })
    }

    private fun getProfile() {
        Log.d("profile", "getting profile ..")
        val userId = intent.getIntExtra("userId", -1)
        profileViewModel.getProfile(userId, boardIds as List<Int>)
    }

    private fun likeBoard(boardId: Int) {
        profileViewModel.likeBoard(boardId)
    }

    private fun intentToComments(boardId: Int) {
        val intent = Intent(this@ProfileActivity, CommentsActivity::class.java)
        intent.putExtra("boardId", boardId)
        startActivity(intent)
    }
}