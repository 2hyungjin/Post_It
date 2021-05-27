package com.example.postit.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import java.io.File
import java.time.LocalDateTime

class MyProfileFragment : Fragment() {
    lateinit var boardViewModel: BoardVM
    lateinit var myProfileViewModel: MyProfileViewModel
    private val boardIdxList = arrayListOf<Int>(-1)
    private lateinit var boardAdapter: BoardAdapter
    private var MORE_LOADING = true
    private var LOADING = false
    private lateinit var me: UserXXX
    private lateinit var navController: NavController
    private var newProfile: String? = null

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        navController = findNavController()
        boardViewModel.getProfileRes.observe(viewLifecycleOwner, Observer { profile ->
            boardAdapter.removeProgressBar()
            if (profile.findBoard.isEmpty() && boardIdxList.size == 1) {
                MORE_LOADING = false
                view_empty_board_my_profile.visibility = View.VISIBLE
            } else if (profile.findBoard.isEmpty()) {
                MORE_LOADING = false
            } else if (profile.findBoard.isNotEmpty()) {
                LOADING = false
                view_empty_board_my_profile.visibility = View.INVISIBLE
                if (profile.user.profile != null && newProfile == null) {
                    Glide.with(this)
                        .load(me.profile)
                        .circleCrop()
                        .into(img_profile_my_profile)
                }
                for (i in profile.findBoard) boardIdxList.add(i.boardId)
                Log.d("changeUserProfile", boardIdxList.toString())
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
        myProfileViewModel.profile.observe(requireActivity(), Observer {
            changeProfile(it)
            progress_bar_my_profile.visibility = View.VISIBLE
            newProfile = it
        })
        boardViewModel.changeUserProfileRes.observe(requireActivity(), Observer {
            progress_bar_my_profile.visibility = View.INVISIBLE
            if (it == null) {

            } else if (it.result == 1) {
                Glide.with(this)
                    .load(newProfile)
                    .circleCrop()
                    .into(img_profile_my_profile)
                resetBoards()
                boardViewModel.changeUserProfileRes.postValue(null)
                Toast.makeText(requireContext(), "프로필 사진을 변경했습니다", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "프로필 사진 변경에 실패했습니다", Toast.LENGTH_SHORT).show()
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
            pickImage()
        }
    }


    fun init() {
        Log.d("changeUserProfile", "init..")
        initViewModel()
        initProfile()
        initRecyclerView()
        loadBoard2()
    }

    private fun initProfile() {
        me = myProfileViewModel.userXXX
        tv_user_name_my_profile.text = me.name
        if (me.profile != null && newProfile == null) {
            Glide.with(this)
                .load(me.profile)
                .circleCrop()
                .into(img_profile_my_profile)
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
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (layoutManager.findLastCompletelyVisibleItemPosition() == boardIdxList.lastIndex - 1) {
                    if (MORE_LOADING && !LOADING) // 추가로 로딩할 게시판이 있는지, 현재 로딩 중인지을 체크하는 변수
                    {
                        Log.d("changeUserProfile", "loading")
                        LOADING = true // 현재 로딩 중
                        rv_my_profile.post {
                            boardAdapter.showProgressBar() // recyclerView에 ProgressBar를 띄움
                            loadBoard() //추가 데이터 로드
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }

        })
    }

    fun loadBoard() {
        var boardListString: String = "["
        for (i in boardIdxList) {
            boardListString += i
            if (i != boardIdxList[boardIdxList.lastIndex]) boardListString += ","
        }
        boardListString += "]"
        boardViewModel.getProfile(me.userId, boardListString)
    }

    fun loadBoard2() {
        var boardListString: String = "["
        for (i in boardIdxList) {
            boardListString += i
            if (i != boardIdxList[boardIdxList.lastIndex]) boardListString += ","
        }
        boardListString += "]"
        boardViewModel.getProfile(me.userId, boardListString)
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
        resetBoards()
        navController.navigate(R.id.action_myProfileFragment_to_changeUserNameFragment)
    }

    private fun navigateToChangePassword() {
        resetBoards()
        navController.navigate(R.id.action_myProfileFragment_to_changePasswordFragment)
    }

    private fun pickImage() {
        GligarPicker().requestCode(1001).withActivity(requireActivity())
            .limit(1).show()
    }

    private fun resetBoards() {
        Log.d("changeUserProfile", "navigate-------------")
        boardAdapter.resetBoards()
        boardIdxList.clear()
        Log.d("changeUserProfile", "rmBoard ${boardIdxList.toString()}")
        boardIdxList.add(-1)
        MORE_LOADING = true
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun changeProfile(profile: String) {
        val dateBody =
            RequestBody.create(MediaType.parse("text/plain"), LocalDateTime.now().toString())
        val contentBody =
            RequestBody.create(MediaType.parse("text/plain"), "${me.name}님이 프로필 사진을 변경했습니다")
        val showBody = RequestBody.create(MediaType.parse("text/plain"), "all")
        val body = hashMapOf<String, RequestBody>(
            "contents" to contentBody,
            "date" to dateBody,
            "show" to showBody
        )

        val file = File(profile)
        val fileBody = RequestBody.create(MediaType.parse("image/jpeg"), file)
        val filePart = MultipartBody.Part.createFormData("files", file.name, fileBody)

        boardViewModel.changeUserProfile(body, filePart)
    }


}