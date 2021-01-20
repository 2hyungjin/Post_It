package com.example.postit.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
    fun onSignInClick(view:View){
        val id=login_edt_id.editText?.text.toString()
        val pw=login_edt_pw.editText?.text.toString()
        VM.login(Req.ReqSignIn(id,pw,1))
        VM.loginRes.observe(this, Observer {

        })
    }
    fun onSignUpClick(view:View){
        val intent=Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

}
