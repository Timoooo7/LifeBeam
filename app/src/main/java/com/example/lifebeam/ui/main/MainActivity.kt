package com.example.lifebeam.ui.main

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.lifebeam.R
import com.example.lifebeam.data.local.model.UserModel
import com.example.lifebeam.databinding.ActionBarBinding
import com.example.lifebeam.ui.auth.LoginActivity
import com.example.lifebeam.databinding.ActivityMainBinding
import com.example.lifebeam.ui.profile.ProfileActivity
import com.example.lifebeam.ui.utils.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.time.format.DateTimeFormatter
import java.util.Calendar
import kotlin.math.floor

class MainActivity : AppCompatActivity() {
    private lateinit var mainActivityBinding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var _user= MutableLiveData<UserModel>()
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* Auth Check */
        auth = Firebase.auth
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            // Not signed in, launch the Login activity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        mainViewModel.getSession().observe(this){ user ->
            _user.value = user
        }

        /* Set view */
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        val actionBarBinding: ActionBarBinding =ActionBarBinding.inflate(layoutInflater)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.customView = actionBarBinding.root
        val typedValue = TypedValue()
        theme.resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true)
        val color = typedValue.data
        supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
        setContentView(mainActivityBinding.root)

        /* Set Action Bar */
        val week = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)/2
        val year = Calendar.getInstance().weekYear


        val timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd")

//        _user.observe(this){
//            mainActivityBinding.tvCoba.text = "${_user.value!!.uid}-${year}-${floor(week.toDouble()).toInt()} dan token = ${_user.value!!.token}"
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_logout -> {
                signOut()
                true
            }
            R.id.btn_account -> {
                val intent = Intent(this, ProfileActivity::class.java )
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signOut() {
        auth.signOut()
        Firebase
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}