package com.example.postit.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toolbar
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.postit.R
import com.example.postit.network.model.UserXXX
import com.example.postit.view.fragment.MyProfileFragment
import com.example.postit.viewmodel.MyProfileViewModel
import com.opensooq.supernova.gligar.GligarPicker
import kotlinx.android.synthetic.main.activity_my_profile.*

class MyProfileActivity : AppCompatActivity() {
    lateinit var myProfileViewModel: MyProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        myProfileViewModel = ViewModelProvider(this)[MyProfileViewModel::class.java]
        val user = intent.getSerializableExtra("user") as UserXXX
        myProfileViewModel.userXXX = user
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != AppCompatActivity.RESULT_OK) {
            return
        }
        when (requestCode) {
            1001 -> {
                val profile = data?.extras?.getStringArray(GligarPicker.IMAGES_RESULT)// return list of selected images paths.
                if (profile != null) {
                    myProfileViewModel.profile.postValue(profile[0])
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}