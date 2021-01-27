package com.example.postit.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.postit.R
import com.example.postit.view.fragment.AddFragment
import com.example.postit.view.fragment.ProfiileFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


    }
    fun navInit(){
        home_nav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_add -> changeFrag(AddFragment())
                R.id.menu_profile ->changeFrag(ProfiileFragment())
            }
            true
        }
    }
    fun changeFrag(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.home_container,fragment)
            .commit()
    }
}