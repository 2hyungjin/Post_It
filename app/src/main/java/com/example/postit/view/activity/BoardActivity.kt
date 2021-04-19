package com.example.postit.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.postit.R
import com.example.postit.adapter.BoardAdapter
import com.example.postit.repository.AppRepo
import com.example.postit.viewmodel.BoardVM
import com.example.postit.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_board.*

class BoardActivity : AppCompatActivity() {
    lateinit var boardViewModel: BoardVM
    private val boardIdxList = arrayListOf<Int>(-1)
    private var MORE_LOADING = true
    private var LOADING = false
    private lateinit var boardAdapter: BoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        init()
        btn_post_board.setOnClickListener { intentToPost() }

        boardViewModel.getBoardRes.observe(this, Observer { res ->
            if (res == null) {
                MORE_LOADING = false
            } else {
                for (i in res.findBoard) boardIdxList.add(i.boardId)
                boardAdapter.apply {
                    setList(res.findBoard)
                    LOADING = false
                    removeProgressBar()
                }
            }
        })

    }

    fun init() {
        val repo = AppRepo()
        val factory = ViewModelProviderFactory(repo)
        boardViewModel = ViewModelProvider(this, factory).get(BoardVM::class.java)

        initRecyclerView()

        // 게시글 받아오기
        loadBoard()
    }

    fun loadBoard() {
//        var boardIdxListVerString = "["
//        var chkIdx = 0
//        for (i in boardIdxList) {
//            boardIdxListVerString += "$i"
//            if (chkIdx != boardIdxList.lastIndex) {
//                boardIdxListVerString += ","
//            }
//            chkIdx++
//        }
//        boardIdxListVerString += "]"
//        Log.d("board", boardIdxListVerString)
//        boardViewModel.getBoard(boardIdxListVerString)
    }

    fun initRecyclerView() {
        boardAdapter = BoardAdapter()
        val layoutManager = LinearLayoutManager(this)

        rv_board.apply {

            this.layoutManager = layoutManager
            adapter = boardAdapter
        }
        rv_board.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            //activity
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // layoutManager가 생성됐고 리스트에서 마지막으로 보이는 position이 리스트의 마지막 인덱스와 같은지 검사한다 (나의 경우에는 리스트에 기본값이 있어서 -1을 하였다)
                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == boardIdxList.lastIndex - 1) {
                    if (MORE_LOADING && !LOADING) // 추가로 로딩할 게시판이 있는지, 현재 로딩 중인지을 체크하는 변수
                    {
                        LOADING = true // 현재 로딩 중
                        rv_board.post {
                            boardAdapter.showProgressBar() // recyclerView에 ProgressBar를 띄움
                            loadBoard() //추가 데이터 로드
                        }
                    }
                }
            }
        })
    }

    fun intentToPost() {
        val intent = Intent(this, PostActivity::class.java)
        startActivity(intent)
    }
}

