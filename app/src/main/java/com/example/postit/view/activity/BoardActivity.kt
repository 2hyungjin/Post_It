package com.example.postit.view.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ex.BoardAdapter
import com.example.postit.R
import com.example.postit.network.Pref.App
import com.example.postit.network.model.UserXXX
import com.example.postit.network.repository.AppRepo
import com.example.postit.viewmodel.BoardVM
import com.example.postit.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_board.*

class BoardActivity : AppCompatActivity() {
    lateinit var boardViewModel: BoardVM
    private val boardIdxList = arrayListOf<Int>(-1)
    private var MORE_LOADING = true
    private var LOADING = false
    lateinit var me: UserXXX
    private lateinit var boardAdapter: BoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)
        init()
        btn_post_board.setOnClickListener { intentToPost() }
        boardViewModel.postBoardRes.observe(this, Observer { res ->
            if (res != null) {
                Toast.makeText(this, "업로드에 성공하였습니다", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "업로드에 실패하였습니다", Toast.LENGTH_SHORT).show()
            }
        })
        boardViewModel.getBoardRes.observe(this, Observer { res ->
            if (res.findBoard.isEmpty()) {
                MORE_LOADING = false
                boardAdapter.removeProgressBar()
            } else {
                for (i in res.findBoard) boardIdxList.add(i.boardId)
                boardAdapter.apply {
                    setList(res.findBoard)
                    removeProgressBar()
                }
            }
            LOADING = false
        })
        boardViewModel.deleteBoardRes.observe(this, Observer { res ->
            if (res == null) {
                Toast.makeText(this, "게시글 삭제에 실패했습니다", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "게시글을 삭제했습니다", Toast.LENGTH_SHORT).show()
            }
        })
        btn_logo_board.setOnClickListener {
            resetBoardList()
            getMyProfile()
        }
        boardViewModel.getMyProfileRes.observe(this, Observer { res ->
            if (res.user.profile != null) {
                Glide.with(applicationContext)
                    .load(res.user.profile)
                    .circleCrop()
                    .into(btn_menu_my_profile)
            }
            tv_menu_user_name.text = res.user.name
            me = res.user
        })
        btn_menu_my_profile.setOnClickListener {
            intentToProfile(me.userId)
        }
        btn_menu_logout.setOnClickListener {
            App.prefs.token = null
            App.prefs.isAutoLoginChked = false
            intentToLogin()
        }

    }

    fun init() {
        val repo = AppRepo()
        val factory = ViewModelProviderFactory(repo)
        boardViewModel = ViewModelProvider(this, factory).get(BoardVM::class.java)

        initRecyclerView()

        setSupportActionBar(toolbar_comments)

        toolbar_comments.title = "Post IT"
        // 게시글 받아오기
        supportActionBar?.setDisplayShowHomeEnabled(false)
        loadBoard()
        getMyProfile()
    }

    fun loadBoard() {
        LOADING = true // 현재 로딩 중
        var boardIdxListVerString = "["
        var chkIdx = 0
        for (i in boardIdxList) {
            boardIdxListVerString += "$i"
            if (chkIdx != boardIdxList.lastIndex) {
                boardIdxListVerString += ","
            }
            chkIdx++
        }
        boardIdxListVerString += "]"
        boardViewModel.getBoard(boardIdxListVerString)
    }

    private fun initRecyclerView() {
        boardAdapter = BoardAdapter({
            likeBoard(it)
        }, {
            intentToComments(it)
        }, {
            intentToProfile(it)
        }, {
            deleteBoard(it)
        }
        )

        val layoutManager = LinearLayoutManager(this)

        rv_board.apply {
            this.layoutManager = layoutManager
            adapter = boardAdapter
        }
        rv_board.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            //activity
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // layoutManager가 생성됐고 리스트에서 마지막으로 보이는 position이 리스트의 마지막 인덱스와 같은지 검사한다 (나의 경우에는 리스트에 기본값이 있어서 -1을 하였다)
                if (layoutManager.findLastCompletelyVisibleItemPosition() == boardIdxList.lastIndex - 1) {
                    if (MORE_LOADING && !LOADING) // 추가로 로딩할 게시판이 있는지, 현재 로딩 중인지을 체크하는 변수
                    {
                        rv_board.post {
                            boardAdapter.showProgressBar() // recyclerView에 ProgressBar를 띄움
                            loadBoard() //추가 데이터 로드
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy)

            }

        })
    }

    private fun intentToPost() {
        val intent = Intent(this, PostActivity::class.java)
        startActivityForResult(intent, 8080)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 8080 && resultCode == Activity.RESULT_OK) {
            resetBoardList()
        }
    }

    private fun resetBoardList() {
        if (!LOADING){
            boardAdapter.resetBoards()
            boardIdxList.apply {
                clear()
                add(-1)
            }
            MORE_LOADING=true
            Log.d("board","reset-------------")
            loadBoard()
        }
    }


    private fun likeBoard(boardId: Int) {
        boardViewModel.likeBoard(boardId)
    }

    private fun intentToComments(boardId: Int) {
        val intent = Intent(this, CommentsActivity::class.java)
        intent.putExtra("boardId", boardId)
        intent.putExtra("user", me)

        startActivity(intent)
    }

    private fun intentToProfile(userId: Int) {
        if (userId != me.userId) {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        } else {
            val intent = Intent(this, MyProfileActivity::class.java)
            intent.putExtra("user", me)
            startActivity(intent)
        }

    }

    private fun deleteBoard(boardId: Int) {
        boardViewModel.deleteBoard(boardId)
        boardIdxList.remove(boardId)
    }

    private fun getMyProfile() {
        boardViewModel.getMyProfile()
    }

    private fun intentToLogin() {
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}

