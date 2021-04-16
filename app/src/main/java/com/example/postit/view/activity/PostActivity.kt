package com.example.postit.view.activity

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.postit.R
import com.example.postit.adapter.ImageAdapter
import com.example.postit.repository.AppRepo
import com.example.postit.viewmodel.BoardVM
import com.example.postit.viewmodel.ViewModelProviderFactory
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedbottompicker.TedBottomPicker
import gun0912.tedbottompicker.TedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_post.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import java.time.LocalDateTime

class PostActivity : AppCompatActivity() {
    lateinit var postViewModel: BoardVM
    lateinit var permissionListener: PermissionListener
    lateinit var imageAdapter: ImageAdapter
    val uriListString = arrayListOf<String>()
    val imgList = arrayListOf<Uri>()
    var MAX_COUNT = 5

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        init()
        btn_addImage_post.setOnClickListener { pickImage() }
        btn_post_post.setOnClickListener { postBoard() }
        postViewModel.postBoardRes.observe(this,Observer{res->
            if (res==null){
                Toast.makeText(this, "업로드에 실패하였습니다", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "업로드에 성공하였습니다", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun init() {
        val repo = AppRepo()
        val factory = ViewModelProviderFactory(repo)
        postViewModel = ViewModelProvider(this, factory).get(BoardVM::class.java)

        imageAdapter = ImageAdapter(uriListString, false)
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
        MAX_COUNT -= imageAdapter.imgList.size
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
            .showMultiImage { uriList ->
                Log.d("postit", uriList.toString())
                if (uriList != null) {
                    for (i in uriList) {
                        uriListString.add(i.toString())
                        imgList.add(i)
                    }
                }
                imageAdapter.setList(uriListString)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun postBoard() {
        val files = arrayListOf<MultipartBody.Part>()
        for (file in imgList) {
            val fileBody = RequestBody.create(MediaType.parse("image/jpeg"), file.path)
            val fileName = "photo$file.jpg"
            val filePart = MultipartBody.Part.createFormData("files", fileName, fileBody)
            files.add(filePart)
        }

        val contentsBody =
            RequestBody.create(MediaType.parse("text/plain"), edt_contents_post.text.toString())
        val profileBody =
            RequestBody.create(MediaType.parse("text/plain"), "1")
        val showBody =
            RequestBody.create(MediaType.parse("text/plain"), "all")
        val dateBody=
            RequestBody.create(MediaType.parse("text/plain"),LocalDateTime.now().toString())
        val partMap=hashMapOf<String,RequestBody>(
            "contents" to contentsBody,
            "date" to dateBody,
            "profile" to profileBody,
            "show" to showBody
        )
        postViewModel.postBoard(partMap,files)

        Log.d("post123",LocalDateTime.now().toString())
    }
}