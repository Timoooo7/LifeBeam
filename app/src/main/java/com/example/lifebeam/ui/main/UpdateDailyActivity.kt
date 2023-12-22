package com.example.lifebeam.ui.main

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.lifebeam.R
import com.example.lifebeam.data.remote.request.ActivitiesItem
import com.example.lifebeam.data.remote.request.DietsItem
import com.example.lifebeam.data.remote.response.DailyResponse
import com.example.lifebeam.data.repository.UserModel
import com.example.lifebeam.databinding.ActivityUpdateDailyBinding
import com.example.lifebeam.databinding.DialogFormBinding
import com.example.lifebeam.ui.utils.ViewModelFactory
import kotlinx.coroutines.launch

class UpdateDailyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateDailyBinding
    private val dailyViewModel by viewModels<DailyLogbookViewModel>()
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityUpdateDailyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* No action bar */
        supportActionBar?.hide()

        val dialogBinding = DialogFormBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBinding.apply {
            dialog.setContentView(root)
            /* Set View */
            conInName.error = null
            inAmount.error = null
            /* Set con in name gone */
            conInName.visibility = View.GONE
            /* Set data for view */
            val isActivity = intent.getBooleanExtra("isActivity", true)
            val updateId = intent.getStringExtra("updateId")
            tvName.setText(intent.getStringExtra("updateName"))
            inAmount.setText(intent.getIntExtra("updateAmount", 0).toString())
            tvUnit.text = intent.getStringExtra("updateUnit")
            btnSubmit.text = getString(R.string.update)
            /* Prepare date */
            mainViewModel.getSession().observe(this@UpdateDailyActivity){
                lifecycleScope.launch {
                    val token = it.token
                    val daily_id = it.daily_id
                        val existList = dailyViewModel.getDailyLogbook(it.token,it.daily_id)

                    btnSubmit.setOnClickListener {
                        dailyViewModel.activitiesResponse.observe(this@UpdateDailyActivity){
                            showToast(it.message)
                        }
                        /* Check if user input is empty */
                        if(inAmount.text.toString().toInt() <= 0){
                            inAmount.error = getString(R.string.empty_field)
                        }else{
                            if(isActivity){
                                val existActivityList = existList.activities
                                Log.d("updateCheck", "$token dan $daily_id")
                                lifecycleScope.launch {
                                    var activityList = listOf(ActivitiesItem(inAmount.text.toString().toInt(),updateId.toString()))
                                    for(i in existActivityList.indices){
                                        activityList = activityList + listOf(ActivitiesItem(existActivityList[i].amount, existActivityList[i].exercise.id))
                                    }
                                    dailyViewModel.updateActivity(token, activityList , daily_id)
                                    dailyViewModel.getDailyLogbook(token, daily_id)
                                }
                            }else{
                                val existDietList = existList.diets
                                lifecycleScope.launch {
                                    var dietList = listOf(DietsItem(inAmount.text.toString().toInt(),updateId.toString()))
                                    for(i in existDietList.indices){
                                        dietList = dietList + listOf(DietsItem(existDietList[i].amount, existDietList[i].food.id))
                                    }
                                    dailyViewModel.updateDiet(token, dietList , daily_id)
                                    dailyViewModel.getDailyLogbook(token, daily_id)
                                }
                            }
                            dialog.dismiss()
                            val intent = Intent(this@UpdateDailyActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
            btnCancel.setOnClickListener{
                dialog.dismiss()
                val intent = Intent(this@UpdateDailyActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
        dialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}