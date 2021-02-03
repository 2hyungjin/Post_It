package com.example.postit.view.fragment

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.example.postit.R
import kotlinx.android.synthetic.main.fragment_add.*
import java.io.File


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
            selectPic()
        }

    }

    fun selectPic() {
        ImagePicker.create(this)
            .limit(5)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode,resultCode,data)){
            val image=ImagePicker.getFirstImageOrNull(data)
            Glide.with(this)
                .load(image.uri)
                .into(imageView4)
            Log.d("TAG",image.uri.toString())
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

}