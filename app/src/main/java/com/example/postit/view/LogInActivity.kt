package com.example.postit.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.postit.HomeActivity
import com.example.postit.R
import com.example.postit.network.Pref.App
import com.example.postit.network.model.Req
import com.example.postit.repository.AppRepo
import com.example.postit.viewmodel.LoginVM
import com.example.postit.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*

class LogInActivity : AppCompatActivity() {
    private lateinit var VM: LoginVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        if (App.prefs.isAutoLoginChked) {
            login_chk_auto_login.isChecked=true
            autoLogin()
        }

        VM.loginRes.observe(this, Observer {
            hidePb()

            App.prefs.isAutoLoginChked = login_chk_auto_login.isChecked
            App.prefs.token = it.token
            startActivity(Intent(this, HomeActivity::class.java))
        })

        VM.autoLoginRes.observe(this, Observer {
            hidePb()

            if (it.result == 1) {
                App.prefs.token = it.token
                startActivity(Intent(this, HomeActivity::class.java))
            } else Toast.makeText(this, "로그인에 실패했습니다", Toast.LENGTH_SHORT).show()
        })
    }

    fun init() {
        val repo = AppRepo()
        val factory = ViewModelProviderFactory(repo)
        VM = ViewModelProvider(this, factory).get(LoginVM::class.java)
    }

    fun onLoginClick(view: View) {
        showPb()
        val id = login_edt_id.editText?.text.toString()
        val pw = login_edt_pw.editText?.text.toString()
        VM.login(Req.ReqSignIn(id, pw, 1))
    }

    fun onSignUpClick(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    fun autoLogin() {
        showPb()
        VM.autoLogin()
        Toast.makeText(this, "자동 로그인하였습니다", Toast.LENGTH_SHORT).show()
    }
    fun showPb(){
        login_pb.visibility=View.VISIBLE
    }
    fun hidePb(){
        login_pb.visibility=View.INVISIBLE
    }
}
