package com.example.postit.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.postit.R
import com.example.postit.network.model.Req
import com.example.postit.repository.AppRepo
import com.example.postit.viewmodel.LoginVM
import com.example.postit.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var VM:LoginVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }
    fun init(){
        val repo=AppRepo()
        val factory=ViewModelProviderFactory(repo)
        VM=ViewModelProvider(this,factory).get(LoginVM::class.java)
    }
    fun onSignInClick(){
        val id=login_edt_id.editText?.text.toString()
        val pw=login_edt_pw.editText?.text.toString()
        VM.login(Req.ReqSignIn(id,pw,1))
        VM.loginRes.observe(this, Observer {
            
        })
    }
}
