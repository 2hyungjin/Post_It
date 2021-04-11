package com.example.postit.view.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.postit.R
import com.example.postit.adapter.ImageAdapter
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedbottompicker.TedBottomPicker
import gun0912.tedbottompicker.TedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity() {
    lateinit var permissionListener: PermissionListener
    lateinit var imageAdapter: ImageAdapter
    val imgList = arrayListOf<String>()
    var MAX_COUNT = 5
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        init()
        btn_addImage_post.setOnClickListener { pickImage() }
    }

    fun init() {
        imageAdapter = ImageAdapter(imgList, false)
        rv_img_post.apply {
            layoutManager =
                LinearLayoutManager(this@PostActivity).also {
                    it.orientation = LinearLayoutManager.HORIZONTAL
                }
            adapter = imageAdapter
        }


        permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(this@PostActivity, "권한이 허용되었습니다", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(
                    this@PostActivity,
                    "권한 거부 :" + deniedPermissions.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun pickImage() {
        val selectedUriList = listOf<Uri>()
        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .check()
        TedBottomPicker.with(this)
            .setPeekHeight(1600)
            .setSelectMaxCount(MAX_COUNT)
            .showTitle(false)
            .setCompleteButtonText("Done")
            .setEmptySelectionText("No Select")
            .setSelectedUriList(selectedUriList)
            .showMultiImage(object : TedBottomSheetDialogFragment.OnMultiImageSelectedListener {
                override fun onImagesSelected(uriList: MutableList<Uri>?) {
                    Log.d("postit",uriList.toString())
                    val uriListString = arrayListOf<String>()
                    if (uriList != null) {
                        for (i in uriList){
                            uriListString.add(i.toString())
                        }
                    }
                    MAX_COUNT -= uriList?.size ?: 0
                    imageAdapter.setList(uriListString)
                }

            })
    }
}