package com.example.postit.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.widget.Toolbar
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.postit.R
import com.example.postit.network.model.UserXXX
import com.example.postit.view.fragment.MyProfileFragment
import com.example.postit.viewmodel.MyProfileViewModel
import kotlinx.android.synthetic.main.activity_my_profile.*

class MyProfileActivity : AppCompatActivity() {
    lateinit var myProfileViewModel: MyProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        myProfileViewModel = ViewModelProvider(this)[MyProfileViewModel::class.java]
        val user = intent.getSerializableExtra("user") as UserXXX
        myProfileViewModel.userXXX = user

        val navController = Navigation.findNavController(this, R.id.fragment_nav)
        val appBarConfiguration = AppBarConfiguration.Builder()
            .build()

        toolbar_my_profile.apply {
            setupWithNavController(navController, appBarConfiguration)
            setNavigationOnClickListener {

                finish()
                navController.navigateUp()
            }
        }
    }

    override fun onNavigateUp(): Boolean {
        if (myProfileViewModel.isMyProfile)finish()
        return super.onNavigateUp()
    }


}