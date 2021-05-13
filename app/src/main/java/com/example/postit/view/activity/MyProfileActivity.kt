package com.example.postit.view.activity

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
import com.example.postit.viewmodel.MyProfileViewModel
import kotlinx.android.synthetic.main.activity_my_profile.*

class MyProfileActivity : AppCompatActivity() {
    lateinit var myProfileViewModel:MyProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        myProfileViewModel=ViewModelProvider(this)[MyProfileViewModel::class.java]
        val user=intent.getSerializableExtra("user") as UserXXX
        myProfileViewModel.userXXX=user

        val navController= Navigation.findNavController(this,R.id.fragment_nav)
        val appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        toolbar_my_profile.setupWithNavController(navController,appBarConfiguration)


        setSupportActionBar(toolbar_my_profile).let {
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home)finish()
        return super.onOptionsItemSelected(item)
    }

}