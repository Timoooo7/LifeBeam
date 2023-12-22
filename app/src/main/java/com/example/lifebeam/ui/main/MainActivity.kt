package com.example.lifebeam.ui.main

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.lifebeam.R
import com.example.lifebeam.data.repository.UserModel
import com.example.lifebeam.databinding.ActionBarBinding
import com.example.lifebeam.databinding.ActivityMainBinding
import com.example.lifebeam.databinding.DialogConfirmationBinding
import com.example.lifebeam.ui.auth.LoginActivity
import com.example.lifebeam.ui.profile.ProfileActivity
import com.example.lifebeam.ui.utils.ViewModelFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar


class MainActivity : AppCompatActivity() {
    private lateinit var mainActivityBinding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: UserModel
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        /* calling splashscreen */
        Thread.sleep(1000)
        installSplashScreen()
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            /* Create your custom animation. */
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenView,
                View.TRANSLATION_Y,
                0f,
                -splashScreenView.height.toFloat()
            )
            slideUp.interpolator = AnticipateInterpolator()
            slideUp.duration = 1000L

            /* Call SplashScreenView.remove at the end of your custom animation. */
            slideUp.doOnEnd { splashScreenView.remove() }

            /* Run your animation. */
            slideUp.start()
        }

        super.onCreate(savedInstanceState)
        /* Auth Check */
        auth = Firebase.auth
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            /* Not signed in, launch the Login activity */
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        user = UserModel(
            firebaseUser.uid,
            firebaseUser.displayName.toString(),
            firebaseUser.email.toString(),
            firebaseUser.photoUrl.toString(),
            firebaseUser.phoneNumber.toString(),
            "",
            ""
        )
        val dateTime = if(intent.getStringExtra("dateTime")!=null){
            intent.getStringExtra("dateTime").toString()
        }else{
            "${LocalDate.now()}"
        }

                    Log.d("daily",dateTime)
        /* save session */
        firebaseUser.getIdToken(true)
            .addOnCompleteListener(OnCompleteListener<GetTokenResult?> { task ->
                if (task.isSuccessful) {
                    mainViewModel.saveSession(
                        UserModel(
                            user.uid,
                            user.name,
                            user.email,
                            user.photo,
                            user.phone,
                            task.result.token.toString(),
                            "${user.uid}-$dateTime"
                        )
                    )
                }
            })


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

        /* Set date picker */
        val datePicker = mainActivityBinding.datePicker
        val today = Calendar.getInstance()
        mainActivityBinding.btnSearch.setOnClickListener{
            val year = datePicker.year
            val month = datePicker.month + 1
            val day = datePicker.dayOfMonth
            val dateTime = "$year-${month}-${day}"
            /* update session */
            val daily_id = "${user.uid}-$dateTime"
            mainViewModel.updateSession(
                UserModel(
                    user.uid,
                    user.name,
                    user.email,
                    user.photo,
                    user.phone,
                    "",
                    "${daily_id}"
                )
            )
        }
        datePicker.maxDate = today.timeInMillis

        /* Set data */
        setDaily()
    }

    fun setDaily(){
        /* Set Daily Data */
        mainViewModel.getSession().observe(this){
            val daily_id = it.daily_id
            val adapter = MainSectionPagerAdapter(this, mapOf(
                "token" to (it.token),
                "uid" to (it.uid),
            ))
            val tabLayout: ViewPager2 = mainActivityBinding.dailyLogbookTabs
            val tabs: TabLayout = mainActivityBinding.tabs
            /* profile tab */
            tabLayout.adapter = adapter
            /* Tab Tittle */
            TabLayoutMediator(tabs, tabLayout) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_logout -> {
                val dialogBinding = DialogConfirmationBinding.inflate(layoutInflater)
                val dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialogBinding.apply {
                    dialog.setContentView(root)
                    tvMessage.text = getString(R.string.confirmation_message)
                    btnYes.setOnClickListener{
                        signOut()
                    }
                    btnNo.setOnClickListener{
                        dialog.dismiss()
                    }
                }
                dialog.show()
                true
            }
            R.id.btn_account -> {
                mainViewModel.getSession().observe(this){
                    val intent = Intent(this, ProfileActivity::class.java )
                    intent.putExtra("token", it.token)
                    startActivity(intent)
                    }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signOut() {
        auth.signOut()
        mainViewModel.logout()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
    companion object{
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_daily_1,
            R.string.tab_daily_2
        )
    }
}