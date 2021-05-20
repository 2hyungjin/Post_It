package com.example.postit.view.fragment

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ex.BoardAdapter
import com.example.postit.R
import com.example.postit.network.model.UserXXX
import com.example.postit.network.repository.AppRepo
import com.example.postit.view.activity.CommentsActivity
import com.example.postit.view.activity.PostActivity
import com.example.postit.viewmodel.BoardVM
import com.example.postit.viewmodel.MyProfileViewModel
import com.example.postit.viewmodel.ViewModelProviderFactory
import com.opensooq.supernova.gligar.GligarPicker
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.my_profile_fragment.*
import okhttp3.RequestBody

class MyProfileFragment : Fragment() {
    lateinit var boardViewModel: BoardVM
    lateinit var myProfileViewModel: MyProfileViewModel
    private val boardIdxList = arrayListOf<Int>(-1)
    private lateinit var boardAdapter: BoardAdapter
    private var MORE_LOADING = true
    private var LOADING = false
    private lateinit var me: UserXXX
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        navController = findNavController()
        boardViewModel.getProfileRes.observe(requireActivity(), Observer { profile ->
            if (profile.findBoard.isEmpty()) {
                MORE_LOADING = false
                view_empty_board_my_profile.visibility = View.VISIBLE
            } else {
                LOADING = true
                for (i in profile.findBoard) boardIdxList.add(i.boardId)
                boardAdapter.setList(profile.findBoard)
            }
        })
        boardViewModel.deleteBoardRes.observe(requireActivity(), Observer { res ->
            if (res == null) {
                Toast.makeText(requireContext(), "게시글 삭제에 실패했습니다", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "게시글을 삭제했습니다", Toast.LENGTH_SHORT).show()
            }
        })
        btn_change_user_name_my_profile.setOnClickListener {
            navigateToChangeUserName()
        }
        btn_back_my_profile.setOnClickListener {
            activity?.finish()
        }
        btn_change_password_my_profile.setOnClickListener {
            navigateToChangePassword()
        }
        btn_change_profile_my_profile.setOnClickListener {

        }
    }


    fun init() {
        initViewModel()
        initProfile()
        initRecyclerView()
        loadBoard()
    }

    private fun initProfile() {
        me = myProfileViewModel.userXXX
        tv_user_name_my_profile.text = me.name
        if (me.profile != 0) {
            Glide.with(this)
                .load(me.profile)
                .into(img_profile_profile)
        }
    }

    private fun initViewModel() {
        val repo = AppRepo()
        val factory = ViewModelProviderFactory(repo)
        boardViewModel = ViewModelProvider(requireActivity(), factory)[BoardVM::class.java]
        myProfileViewModel = ViewModelProvider(requireActivity())[MyProfileViewModel::class.java]
    }

    private fun initRecyclerView() {
        boardAdapter = BoardAdapter({
            likeBoard(it)
        }, {
            intentToComments(it)
        }, {
        }, {
            deleteBoard(it)
        }
        )

        val layoutManager = LinearLayoutManager(requireContext())

        rv_my_profile.apply {
            this.layoutManager = layoutManager
            adapter = boardAdapter
        }
        rv_my_profile.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            //activity
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // layoutManager가 생성됐고 리스트에서 마지막으로 보이는 position이 리스트의 마지막 인덱스와 같은지 검사한다 (나의 경우에는 리스트에 기본값이 있어서 -1을 하였다)
                if (layoutManager.findLastCompletelyVisibleItemPosition() == boardIdxList.lastIndex - 1) {
                    if (MORE_LOADING && !LOADING) // 추가로 로딩할 게시판이 있는지, 현재 로딩 중인지을 체크하는 변수
                    {
                        LOADING = true // 현재 로딩 중
                        rv_my_profile.post {
                            boardAdapter.showProgressBar() // recyclerView에 ProgressBar를 띄움
                            loadBoard() //추가 데이터 로드
                        }
                    }
                }
            }

        })
    }

    fun loadBoard() {
        Log.d("myProfile", boardIdxList.toString())

        var boardListString: String = "["
        for (i in boardIdxList) {
            boardListString += i
            if (i != boardIdxList[boardIdxList.lastIndex]) boardListString += ","
        }
        boardListString += "]"
        boardViewModel.getProfile(me.userId, boardListString)
    }

    private fun intentToPost() {
        val intent = Intent(requireContext(), PostActivity::class.java)
        startActivityForResult(intent, 8080)
    }

    private fun likeBoard(boardId: Int) {
        boardViewModel.likeBoard(boardId)
    }

    private fun intentToComments(boardId: Int) {
        val intent = Intent(requireContext(), CommentsActivity::class.java)
        intent.putExtra("boardId", boardId)
        intent.putExtra("user", me)
        startActivity(intent)
    }


    private fun deleteBoard(boardId: Int) {
        boardViewModel.deleteBoard(boardId)
        boardIdxList.remove(boardId)
    }

    private fun navigateToChangeUserName() {
        boardIdxList.clear()
        boardIdxList.add(-1)
        navController.navigate(R.id.action_myProfileFragment_to_changeUserNameFragment)
    }

    private fun navigateToChangePassword() {
        boardIdxList.clear()
        boardIdxList.add(-1)
        navController.navigate(R.id.action_myProfileFragment_to_changePasswordFragment)
    }

    private fun pickImage() {
        GligarPicker().requestCode(1001).withActivity(requireActivity())
            .limit(1).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != AppCompatActivity.RESULT_OK) {
            return
        }
        when (requestCode) {
            1001 -> {
                val profile = data?.extras?.getString(GligarPicker.IMAGES_RESULT)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun changeProfile(profile: String) {
        val body = HashMap<String, RequestBody>()

//        boardViewModel.changeUserProfile()
    }

}