package com.example.postit.view.activity

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
    private var MORE_LOADING=true
    private var LOADING=true
    private lateinit var boardAdapter: BoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)
        init()
        boardViewModel.getBoardRes.observe(this, Observer { res ->
            if (res==null)MORE_LOADING=false
            boardIdxList.clear()
            for (i in res.findBoard) boardIdxList.add(i.boardId)
            boardAdapter.apply {
                setLoadingChk(false)
                setList(res.findBoard)
                LOADING=true
//                removeProgressBar()
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
        Log.d("board",boardIdxListVerString)
        boardViewModel.getBoard(boardIdxListVerString)
    }

    fun initRecyclerView() {
        boardAdapter = BoardAdapter()
        val layoutManager = LinearLayoutManager(this)
        rv_board.apply {
            this.layoutManager = layoutManager
            adapter = boardAdapter
        }
        rv_board.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager.findLastCompletelyVisibleItemPosition() == boardIdxList.size-1) {
                    if (MORE_LOADING&&LOADING){
                        LOADING=false
                        boardAdapter.setLoadingChk(true)
                        boardAdapter.showProgressBar()
//                        loadBoard()
                        Log.d("board","loading")
                    }
                    Log.d("board","load more")
                }
            }
        })
    }
}