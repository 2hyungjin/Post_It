package com.example.postit.view.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.postit.R
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_post.*
import java.io.File
import java.util.jar.Manifest

class PostActivity : AppCompatActivity() {
    lateinit var permissionListener: PermissionListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        init()
        btn_addImage_post.setOnClickListener { pickImage() }
    }
    fun init(){
        permissionListener = object :PermissionListener{
            override fun onPermissionGranted() {
                Toast.makeText(this@PostActivity, "권한이 허용되었습니다", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@PostActivity, "권한 거부 :"+deniedPermissions.toString(), Toast.LENGTH_SHORT).show()
            }

        }
    }
    fun pickImage() {
        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("권한")
            .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .check()
    }
}