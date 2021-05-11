package com.example.postit.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.postit.R

class MyProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        val navController=findNavController(R.id.nav_host_fragment)
        val user=intent.getSerializableExtra("user")
        val bundle=Bundle()
        bundle.putSerializable("user",user)
        navController.setGraph(navController.graph,bundle)
    }
}