package com.example.postit.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.postit.R
import com.example.postit.network.Pref.App
import com.example.postit.network.model.Req
import com.example.postit.repository.AppRepo
import com.example.postit.viewmodel.LoginVM
import com.example.postit.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*

class LogInActivity : AppCompatActivity() {
    private lateinit var VM:LoginVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        if (login_chk_auto_login.isChecked)
        VM.loginRes.observe(this, Observer {
            App.prefs.token=it.token
            Toast.makeText(this, "token : ${App.prefs.token}", Toast.LENGTH_SHORT).show()
        })
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
    }
    fun onSignUpClick(view:View){
        val intent=Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
    fun autoLogin(){

    }

}
