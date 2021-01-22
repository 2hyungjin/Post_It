package com.example.postit.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.postit.R
import com.example.postit.network.model.Req
import com.example.postit.repository.AppRepo
import com.example.postit.viewmodel.LoginVM
import com.example.postit.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*

class SignUpActivity : AppCompatActivity() {
    private var idChk = false
    private lateinit var id: String
    private lateinit var pw: String
    private lateinit var pw2: String
    private lateinit var name: String
    private lateinit var VM:LoginVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        init()
        VM.idChk.observe(this, androidx.lifecycle.Observer {
            if (it == 1) {
                idChk=true
                Toast.makeText(this, "사용 가능한 id 입니다.", Toast.LENGTH_SHORT).show()
                Log.d("TAG",idChk.toString())
            }
            else Toast.makeText(this, "사용 불가능한 id 입니다.", Toast.LENGTH_SHORT).show()
        })
        sign_up_edt_id.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("TAG",idChk.toString())
                idChk = false
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun init(){
        val repo= AppRepo()
        val factory= ViewModelProviderFactory(repo)
        VM=ViewModelProvider(this,factory).get(LoginVM::class.java)
    }

    fun onSignUpDoneClicked(view: View) {
        if (check()) {
            Log.d("TAG","info is available")
            val body=Req.ReqSignUp(id,pw,name)
            VM.signUp(body)
        }

    }

    fun check(): Boolean {
        id = sign_up_edt_id.editText?.text.toString()
        pw = sign_up_edt_pw.editText?.text.toString()
        pw2 = sign_up_edt_pw2.editText?.text.toString()
        Log.d("TAG", id.toString())
        Log.d("TAG", pw.toString())
        Log.d("TAG", pw2.toString())
        val chk = 4..15
        return if (pw != pw2) {
            Toast.makeText(this, "비밀번호가 서로 같지 않습니다", Toast.LENGTH_SHORT).show()
            false
        } else {
            return if (id.length in chk && pw.length in chk) true
            else {
                Toast.makeText(this, "ID 혹은 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    fun chkId(view: View) {
        id = sign_up_edt_id.editText?.text.toString()
        VM.chkId(id)
    }

}