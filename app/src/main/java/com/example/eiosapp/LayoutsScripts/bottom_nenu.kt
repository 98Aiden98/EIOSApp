package com.example.eiosapp.LayoutsScripts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.eiosapp.R
import com.example.eiosapp.TokenPackage.SharedPrefManager
import com.example.eiosapp.databinding.ActivityBottomNenuBinding

class bottom_nenu : AppCompatActivity() {

    private lateinit var binding : ActivityBottomNenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNenuBinding.inflate(layoutInflater)
        //setContentView(R.layout.fragment_profile1)
        setContentView(binding.root)
        replaceFragment(profile1())
        SharedPrefManager.refreshDataUsingRefreshToken()

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){

                R.id.profile -> replaceFragment(profile1())
                R.id.marks -> replaceFragment(marks())
                R.id.schedule -> replaceFragment(Schedule())
                R.id.mail -> replaceFragment(mail())
                R.id.chat -> replaceFragment(chat())

                else ->{



                }

            }

            true

        }


    }

    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()


    }
}