package com.example.postit.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
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
    private val chk = 4..15
    private lateinit var id: EditText
    private lateinit var pw: EditText
    private lateinit var pw2: EditText
    private lateinit var name: EditText
    private lateinit var VM: LoginVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        init()
        VM.idChk.observe(this, androidx.lifecycle.Observer {
            if (it.result == 1) {
                idChk = true
                Toast.makeText(this, "사용 가능한 id 입니다.", Toast.LENGTH_SHORT).show()
                Log.d("TAG", idChk.toString())
            } else Toast.makeText(this, "사용 불가능한 id 입니다.", Toast.LENGTH_SHORT).show()
        })
        VM.singUpRes.observe(this, androidx.lifecycle.Observer {
            if (it.result==1)
            sign_up_pb.visibility=View.INVISIBLE
        })
        sign_up_edt_id.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                Log.d("TAG", idChk.toString())
                idChk = false
            }
        })

        sign_up_btn_done.setOnClickListener {
            if (check()) {
                Log.d("TAG","info is available")
                if (idChk){
                    val gender=if(sign_up_radio_female.isChecked)1 else 0
                    val body=Req.ReqSignUp(id.text.toString(),pw.text.toString(),name.text.toString(),gender)
                    Log.d("TAG","body : $body")
                    VM.signUp(body)
                    sign_up_pb.visibility=View.VISIBLE
                }
                else Toast.makeText(this, "아이디 중복 검사를 해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun init() {
        id = sign_up_edt_id.editText!!
        pw = sign_up_edt_pw.editText!!
        pw2 = sign_up_edt_pw2.editText!!
        name=sign_up_edt_name.editText!!
        val repo = AppRepo()
        val factory = ViewModelProviderFactory(repo)
        VM = ViewModelProvider(this, factory).get(LoginVM::class.java)
    }



    fun check(): Boolean {
        Log.d("TAG", id.toString())
        Log.d("TAG", pw.toString())
        Log.d("TAG", pw2.toString())
        return if (pw.text.toString() != pw2.text.toString()) {
            Toast.makeText(this, "비밀번호가 서로 같지 않습니다", Toast.LENGTH_SHORT).show()
            false
        } else {
            return if (id.text.toString().length in chk && pw.text.toString().length in chk) true
            else {
                Toast.makeText(this, "ID 혹은 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    fun chkId(view: View) {
        if (id.text.toString().length in chk) VM.chkId(id.text.toString())
        else Toast.makeText(this, "형식에 맞게 입력해주세요", Toast.LENGTH_SHORT).show()
    }

}