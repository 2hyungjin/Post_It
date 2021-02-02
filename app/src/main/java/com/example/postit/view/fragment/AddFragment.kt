package com.example.postit.view.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.postit.R
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.fragment_add.*
import java.util.ArrayList
import java.util.jar.Manifest
import kotlin.math.log

class AddFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_btn_add_image.setOnClickListener {
            TedPermission.with(context)
                .setPermissionListener(permissionListener)
                .setPermissions()
                .check()
        }
    }
    fun selectPic(){
        val intent =Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent,1001)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Glide.with(this)
            .load(data?.data)
            .into(imageView4)
        Log.d("tag",data?.data.toString())
    }
    val permissionListener = object :PermissionListener{
        override fun onPermissionGranted() {
            selectPic()
        }

        override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
            for (i in deniedPermissions!!) {
                Toast.makeText(context,"$i 에 대해 권한을 거부하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    }
}