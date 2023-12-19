package com.example.lifebeam.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfileSectionPagerAdapter(activity: AppCompatActivity, data: Map<String,String>) : FragmentStateAdapter(activity) {
    private val dataUser = data
    override fun createFragment(position: Int): Fragment {
        if(position == 0){
            val fragment = ProfileFragment()
            fragment.arguments = Bundle().apply {
                putInt(ProfileFragment.ARG_POSITION, position+1)
                putString("name", dataUser["name"])
                putString("email", dataUser["email"])
            }
            return fragment
        }else{
            val fragment = BodyFragment()
            fragment.arguments = Bundle().apply {
                putInt(BodyFragment.ARG_POSITION, position+1)
            }
            return fragment
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}