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
import com.example.postit.network.model.Res
import com.example.postit.repository.AppRepo
import com.example.postit.viewmodel.BoardVM
import com.example.postit.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_board.*

class BoardActivity : AppCompatActivity() {
    lateinit var boardViewModel: BoardVM
    private val boardIdxList = arrayListOf<Int>(-1, -2)

    private lateinit var boardAdapter: BoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)
        init()
        boardViewModel.getBoardRes.observe(this, Observer { res ->
            boardIdxList.clear()
            for (i in res.findBoard) boardIdxList.add(i.boardId)
            boardAdapter.apply {
                setViewType(true)
                setList(res.findBoard as List<Res.FindBoard>)
                removeProgressBar()
            }
        })

    }

    fun init() {
        val repo = AppRepo()
        val factory = ViewModelProviderFactory(repo)
        boardViewModel = ViewModelProvider(this, factory).get(BoardVM::class.java)

        initScrollListener()

        // 게시글 받아오기
        loadBoard()
    }

    fun loadBoard() {
        var boardIdxListVerString = "["
        var chkIdx = 0
        for (i in boardIdxList) {
            boardIdxListVerString += "$i"
            if (chkIdx != boardIdxList.lastIndex) {
                Log.d("Board", boardIdxList.lastIndex.toString())
                boardIdxListVerString += ","
            }
            chkIdx++
        }
        boardIdxListVerString += "]"
        boardViewModel.getBoard(boardIdxListVerString)
    }

    fun initScrollListener() {
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
//                    loadBoard()
                    Log.d("board","load more")
                    boardAdapter.setViewType(false)
                }
            }
        })
    }
}