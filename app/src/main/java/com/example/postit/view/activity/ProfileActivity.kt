package com.example.postit.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ex.BoardAdapter
import com.example.postit.R
import com.example.postit.repository.AppRepo
import com.example.postit.viewmodel.BoardVM
import com.example.postit.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.my_profile_fragment.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var profileViewModel: BoardVM
    private val boardIds = arrayListOf<Int>(-1)
    private lateinit var boardAdapter: BoardAdapter
    private var LOADING_CHK: Boolean = true
    private var isLoaded: Boolean = false
    private var MORE_LOADING_CHK: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        init()
        profileViewModel.getProfileRes.observe(this, Observer { profile ->
            Log.d("profile", boardIds.toString())
            tv_user_name_profile.text = profile.user.name
            isLoaded = true
            if (profile.user.profile != 0) {
                Glide.with(this)
                    .load(profile.user.profile)
                    .into(img_profile_profile)
            }


            if (profile.findBoard.isEmpty()) MORE_LOADING_CHK = false
            else {
                LOADING_CHK = true
                for (i in profile.findBoard) boardIds.add(i.boardId)
                boardAdapter.setList(profile.findBoard)
            }
        })
        profileViewModel.deleteBoardRes.observe(this, Observer { res ->
            if (res == null) {
                Toast.makeText(this, "게시글 삭제에 실패했습니다", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "게시글을 삭제했습니다", Toast.LENGTH_SHORT).show()
            }
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
        }, {
            deleteBoard(it)
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
                    if (LOADING_CHK && MORE_LOADING_CHK) {
                        Log.d("profile", "loading..")
                        getProfile()
                        LOADING_CHK = false
                    }
                }
            }
        })
    }

    private fun getProfile() {
        var boardListString: String = "["
        for (i in boardIds) {
            boardListString += i
            if (i != boardIds[boardIds.lastIndex]) boardListString += ","
        }
        boardListString += "]"
        val userId = intent.getIntExtra("userId", -1)
        profileViewModel.getProfile(userId, boardListString)
    }

    private fun likeBoard(boardId: Int) {
        profileViewModel.likeBoard(boardId)
    }

    private fun intentToComments(boardId: Int) {
        val intent = Intent(this@ProfileActivity, CommentsActivity::class.java)
        intent.putExtra("boardId", boardId)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    private fun deleteBoard(boardId: Int) {
    }
}