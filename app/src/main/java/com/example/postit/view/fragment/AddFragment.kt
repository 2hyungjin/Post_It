package com.example.postit.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.esafirm.imagepicker.features.ImagePicker
import com.example.postit.R
import com.example.postit.adapter.ImgRvAdapter
import kotlinx.android.synthetic.main.fragment_add.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

val arr= arrayListOf<String>("all","me","friend")
class AddFragment : Fragment() {
    val imgList= arrayListOf<Uri>()
    lateinit var rvAdapter: ImgRvAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initV()
        add_btn_add_img.setOnClickListener {
            if(imgList.size>=5) Toast.makeText(context, "사진은 5개만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
            else selectPic()
        }
    }

    private fun initV(){
        rvAdapter=ImgRvAdapter()

        val arrayAdapter=ArrayAdapter(context!!,android.R.layout.simple_spinner_item, arr)
        add_spinner.adapter=arrayAdapter

        add_rv.apply {
            adapter=rvAdapter
            layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        }
    }
    fun selectPic() {
        ImagePicker.create(this)
            .limit(5-imgList.size)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode,resultCode,data)){
            val images=ImagePicker.getImages(data)
            for (i in images){
                imgList.add(i.uri)
            }
            rvAdapter.setList(imgList)

//            val file=File(image.uri.path!!)
//            val reqBody=RequestBody.create(MediaType.parse("image/jpeg"),file)
//            val fileP=MultipartBody.Part.createFormData("files",file.name,reqBody)
//            Log.d("TAG",fileP.toString())
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

}