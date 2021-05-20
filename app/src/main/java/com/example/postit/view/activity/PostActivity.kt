package com.example.postit.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.postit.R
import com.example.postit.adapter.ImageAdapter
import com.example.postit.network.repository.AppRepo
import com.example.postit.viewmodel.BoardVM
import com.example.postit.viewmodel.ViewModelProviderFactory
import com.opensooq.supernova.gligar.GligarPicker
import kotlinx.android.synthetic.main.activity_post.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.time.LocalDateTime

class PostActivity : AppCompatActivity() {
    lateinit var postViewModel: BoardVM
    lateinit var imageAdapter: ImageAdapter
    val imgList = arrayListOf<String?>()
    var MAX_COUNT = 5

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        init()
        btn_addImage_post.setOnClickListener { pickImage() }
        btn_post_post.setOnClickListener { postBoard() }
        postViewModel.postBoardRes.observe(this, Observer { res ->
            if (res == null) {
                Toast.makeText(this, "업로드에 실패하였습니다", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "업로드에 성공하였습니다", Toast.LENGTH_SHORT).show()
                pb_board.visibility = View.GONE
                setResult(Activity.RESULT_OK);
                finish();
            }
        })
    }

    fun init() {
        val repo = AppRepo()
        val factory = ViewModelProviderFactory(repo)
        postViewModel = ViewModelProvider(this, factory).get(BoardVM::class.java)

        imageAdapter = ImageAdapter(imgList, false)
        rv_img_post.apply {
            layoutManager =
                LinearLayoutManager(this@PostActivity).also {
                    it.orientation = LinearLayoutManager.HORIZONTAL
                }
            adapter = imageAdapter
        }
    }

    fun pickImage() {
        GligarPicker().requestCode(1001).withActivity(this)
            .limit(MAX_COUNT - imageAdapter.imgList.size).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }
        when (requestCode) {
            1001 -> {
                val imagesList = data?.extras?.getStringArray(GligarPicker.IMAGES_RESULT)
                if (imagesList != null) {
                    imgList.addAll(imagesList)

                    imageAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun postBoard() {
        val files = arrayListOf<MultipartBody.Part>()

        if (imgList.isNotEmpty()) {
            for (file in imgList) {
                val fileBody = RequestBody.create(MediaType.parse("image/jpeg"), File(file))
                val filePart = MultipartBody.Part.createFormData("files", File(file).name, fileBody)
                files.add(filePart)
            }
        }

        val contentsBody =
            RequestBody.create(MediaType.parse("text/plain"), edt_contents_post.text.toString())
        val showBody =
            RequestBody.create(MediaType.parse("text/plain"), "all")
        val dateBody =
            RequestBody.create(MediaType.parse("text/plain"), LocalDateTime.now().toString())
        val partMap = hashMapOf<String, RequestBody>(
            "contents" to contentsBody,
            "date" to dateBody,
            "show" to showBody
        )
        postViewModel.postBoard(partMap, files)
        pb_board.visibility = View.VISIBLE
    }
}
