package com.example.postit.view.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.postit.R
import com.example.postit.repository.AppRepo
import com.example.postit.viewmodel.BoardVM
import com.example.postit.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.change_user_name_fragment.*
import kotlinx.android.synthetic.main.my_profile_fragment.*

class ChangeUserNameFragment : Fragment() {
    lateinit var boardViewModel: BoardVM
    lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.change_user_name_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        navController=findNavController()
        btn_back_change_user_name.setOnClickListener {
            navController.navigateUp()
        }
        btn_change_user_name_change_user_name.setOnClickListener {
            changeUserName()
        }
        boardViewModel.changeUserNameRes.observe(requireActivity(), Observer {
            progress_bar_change_user_name.visibility=View.GONE
            btn_change_user_name_change_user_name.visibility=View.VISIBLE
            when(it.result){
                200-> Toast.makeText(requireContext(), "이름을 변경했습니다", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(requireContext(), "이름 변경에 실패했습니다", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun init() {
        viewModelInit()
    }

    private fun viewModelInit() {
        val repo=AppRepo()
        val factory=ViewModelProviderFactory(repo)
        boardViewModel=ViewModelProvider(requireActivity(),factory)[BoardVM::class.java]
    }


    private fun changeUserName() {
        val userName=edt_change_name.text.toString()
        boardViewModel.changeUserName(userName)
        btn_change_user_name_change_user_name.visibility=View.INVISIBLE
        progress_bar_change_user_name.visibility=View.VISIBLE
    }

}