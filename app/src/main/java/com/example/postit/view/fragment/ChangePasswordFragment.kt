package com.example.postit.view.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.postit.R
import com.example.postit.network.model.Res
import com.example.postit.network.repository.AppRepo
import com.example.postit.viewmodel.BoardVM
import com.example.postit.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.change_password_fragment.*

class ChangePasswordFragment : Fragment() {
    private lateinit var boardViewModel: BoardVM
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.change_password_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        boardViewModel.changeUserPasswordRes.observe(requireActivity(), Observer {
            when {
                it == null -> {
                    Toast.makeText(requireActivity(), "비밀번호 변경에 실패했습니다", Toast.LENGTH_SHORT).show()
                }
                it.result == 1 -> {
                    Toast.makeText(requireActivity(), "비밀번호를 변경했습니다", Toast.LENGTH_SHORT).show()
                    navController.navigateUp()
                    boardViewModel.changeUserPasswordRes.postValue(Res.Res(0,""))
                }
                it.result == 0 -> {
                }
            }
        })
        btn_change_password_change_user_name.setOnClickListener { changePassword() }
        btn_back_change_user_name.setOnClickListener { navController.navigateUp() }

    }

    private fun init() {
        navController = findNavController()
        viewModelInit()
    }

    private fun viewModelInit() {
        val repo = AppRepo()
        val factory = ViewModelProviderFactory(repo)
        boardViewModel = ViewModelProvider(requireActivity(), factory)[BoardVM::class.java]
    }

    private fun changePassword() {
        val password = edt_password_change_password.text.toString()
        val changePassword = edt_change_password_change_password.text.toString()
        if (password != null && changePassword != null) {
            boardViewModel.changeUserPassword(password, changePassword)
        } else {
            Toast.makeText(requireContext(), "입력형식을 맞춰주세요", Toast.LENGTH_SHORT).show()
        }

    }


}