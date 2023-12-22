package com.example.lifebeam.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.lifebeam.R
import com.example.lifebeam.databinding.ActivityProfileBinding
import com.example.lifebeam.ui.main.MainActivity
import com.example.lifebeam.ui.utils.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* No action bar */
        supportActionBar?.hide()
        binding.tabs

        /* Set Profile */
        profileViewModel.getSession().observe(this){ user ->
            val adapter = ProfileSectionPagerAdapter(this, data = mapOf(
                "uid" to user.uid,
                "token" to intent.getStringExtra("token").toString(),
                "name" to user.name,
                "email" to user.email,
            ))
            val tabLayout: ViewPager2 = binding.profileTabs
            val tabs: TabLayout = binding.tabs
            /* Image User */
            Glide.with(binding.imgUserPhoto)
                .load(user.photo)
                .circleCrop()
                .into(binding.imgUserPhoto)
            /* profile tab */
            tabLayout.adapter = adapter
            /* Tab Tittle */
            TabLayoutMediator(tabs, tabLayout) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    companion object{
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_profile_1,
            R.string.tab_profile_2,
            R.string.tab_profile_3
        )
    }
}