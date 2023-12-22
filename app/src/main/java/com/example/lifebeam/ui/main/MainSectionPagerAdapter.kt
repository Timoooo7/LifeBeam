package com.example.lifebeam.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainSectionPagerAdapter(activity: AppCompatActivity, data: Map<String,String>) : FragmentStateAdapter(activity) {
    private val dataUser = data
    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            val fragment = ActivitiesFragment()
            fragment.arguments = Bundle().apply {
                putString("token", dataUser["token"])
                putString("uid", dataUser["uid"])
            }
            return fragment
        } else {
            val fragment = DietFragment()
            fragment.arguments = Bundle().apply {
                putString("token", dataUser["token"])
                putString("uid", dataUser["uid"])
            }
            return fragment
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}