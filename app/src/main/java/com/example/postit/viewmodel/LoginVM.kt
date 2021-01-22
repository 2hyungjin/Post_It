package com.example.postit.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postit.network.model.Req
import com.example.postit.network.model.Req.ReqSignIn
import com.example.postit.network.model.Res
import com.example.postit.repository.AppRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginVM(private val repo:AppRepo) : ViewModel() {
    val loginRes= MutableLiveData<Res.ResSignIn>()
    val singUpRes=MutableLiveData<Int>()
    val idChk=MutableLiveData<Int>()
    fun login(body:ReqSignIn){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.signIn(body).let {res->
                    if (res.isSuccessful){
                        Log.d("TAG","login suc : ${res.body()?.token.toString()}")
                    }
                    else{
                        Log.d("TAG","message : ${res.message()}")
                        Log.d("TAG","error : ${res.errorBody()}")
                    }
                }
            }
            catch (e:Throwable){
                Log.d("TAG",e.toString())
            }
        }
    }
    fun signUp(body:Req.ReqSignUp){
        viewModelScope.launch(Dispatchers.IO){
            try {
                repo.signUp(body).let {res->
                    if (res.isSuccessful){
                        Log.d("TAG","sign up suc : ${res.body()}")
                        singUpRes.postValue(res.body())
                    }
                    else{
                        Log.d("TAG","message : ${res.message()}")
                        Log.d("TAG","error : ${res.errorBody()}")
                    }
                }
            }
            catch (e:Throwable){
                Log.d("TAG",e.toString())
            }

        }
    }
    fun chkId(id:String){
        viewModelScope.launch(Dispatchers.IO){
            repo.chkId(id).let {res->
                if (res.isSuccessful)idChk.postValue(res.body())
                else {
                    Log.d("TAG","message : ${res.message()}")
                    Log.d("TAG","error : ${res.errorBody()}")
                }
            }
        }
    }
}