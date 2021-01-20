package com.example.postit.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.postit.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var id:String
    private lateinit var pw:String
    private lateinit var pw2:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }

    fun onSignUpDoneClicked(view: View) {
        if (check()) {
            Toast.makeText(this, "suc", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    fun check(): Boolean {
        id = sign_up_edt_id.editText?.text.toString()
        pw = sign_up_edt_pw.editText?.text.toString()
        pw2 = sign_up_edt_pw2.editText?.text.toString()
        Log.d("TAG",id.toString())
        Log.d("TAG",pw.toString())
        Log.d("TAG",pw2.toString())
        val chk = 4..15
        return if (pw != pw2) {
            Toast.makeText(this, "비밀번호가 서로 같지 않습니다", Toast.LENGTH_SHORT).show()
            false
        }else{
            return if (id.length in chk && pw.length in chk) true
            else {
                Toast.makeText(this, "ID 혹은 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show()
                false
            }
        }

    }
}