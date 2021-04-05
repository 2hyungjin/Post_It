package com.example.postit.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postit.network.model.Res
import com.example.postit.repository.AppRepo
import kotlinx.coroutines.launch

class BoardVM(val repo:AppRepo):ViewModel() {
    val getBoardRes= MutableLiveData<Res.Board>()
    fun getBoard(id:String){
        viewModelScope.launch {
            repo.getBoard(id).let { res->
                if (res.isSuccessful){
                    getBoardRes.postValue(res.body())
                }else{
                    Log.d("BoardVM",res.body().toString())
                    getBoardRes.postValue(null)
                }
            }

        }
    }
}