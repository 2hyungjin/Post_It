package com.example.postit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postit.repository.AppRepo
import kotlinx.coroutines.launch

class BoardVM(val repo:AppRepo):ViewModel() {
    fun getBoard(id:List<Int>){
        viewModelScope.launch {
            repo.getBoard(id).let { res->
                if (res.isSuccessful){

                }else{

                }
            }

        }
    }
}