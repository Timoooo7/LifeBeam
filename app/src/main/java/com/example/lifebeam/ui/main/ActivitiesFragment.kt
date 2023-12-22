package com.example.lifebeam.ui.main

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import com.example.lifebeam.databinding.FragmentActivitiesBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifebeam.R
import com.example.lifebeam.data.remote.request.ActivitiesItem
import com.example.lifebeam.data.remote.response.Exercise
import com.example.lifebeam.data.remote.response.ExercisesItem
import com.example.lifebeam.data.repository.UserModel
import com.example.lifebeam.databinding.DialogFormBinding
import com.example.lifebeam.ui.utils.ViewModelFactory
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar

class ActivitiesFragment : Fragment() {
    private lateinit var binding: FragmentActivitiesBinding
    private val dailyViewModel by viewModels<DailyLogbookViewModel>()
    private val existActivityList= MutableLiveData<List<String>>()
    private var token = ""
    private var dateTime = LocalDate.now()
    private var daily_id = ""
    private var existList = listOf(com.example.lifebeam.data.remote.response.ActivitiesItem(0,
        Exercise("",10F,"","")
    ))
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivitiesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { bundle ->
            /* Loading initiation */
            dailyViewModel.isLoading.observe(viewLifecycleOwner){
                showLoading(it)
                hideFab(it)
            }
            /* Set view */
            mainViewModel.getSession().observe(viewLifecycleOwner){
                token = it.token
                daily_id = it.daily_id
                getData()
            }

            dailyViewModel.errResponse.observe(viewLifecycleOwner){
                if(it!=null){
                    showToast(it)
                }
            }
            binding.fabAdd.setOnClickListener {
                showDialogForm()
            }
        }
    }

    fun getData(){
        /* Model initiation */
        lifecycleScope.launch {
            existList = dailyViewModel.getDailyLogbook(token, daily_id).activities
        }
        /* Showing data */
        dailyViewModel.response.observe(viewLifecycleOwner){daily ->
            if(daily!=null){
                existActivityList.value = daily.activities.map { it.exercise.id }
                val adapter = ActivitiesAdapter(daily.activities)
                val layoutManager = LinearLayoutManager(requireActivity())
                binding.rvActivities.layoutManager = layoutManager
                binding.rvActivities.adapter = adapter
            }
        }
    }

    private fun showDialogForm(){
        val dialogBinding = DialogFormBinding.inflate(layoutInflater)
        val dialog = Dialog(requireActivity())
        lifecycleScope.launch {
            dailyViewModel.getExerciseList(token)
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBinding.apply {
            dailyViewModel.listExerciseResponse.observe(viewLifecycleOwner){ exercise ->
                /* Set View */
                conInName.error = null
                inAmount.error = null
                /* Set tv layout gone */
                conTvName.visibility = View.GONE
                /* Set data for option */
                val activities = exercise.exercises.filterNot{ activity -> existActivityList.value!!.contains(activity.id)}.map { it.name }
                dialog.setContentView(root)
                conInName.hint = getString(R.string.activities)
                val adapterItems = ArrayAdapter(requireActivity(),R.layout.item_list, activities)
                var selectedId = ""
                tvAutoComplete.setAdapter(adapterItems)
                tvAutoComplete.onItemClickListener = AdapterView.OnItemClickListener{ adapterView, view, i, l ->
                    val id = adapterView.getItemAtPosition(i)
                    val selectedItem = exercise.exercises.first{ it.name == id.toString() }
                    tvUnit.text = selectedItem.unit
                    selectedId = selectedItem.id
                }
                btnSubmit.setOnClickListener {
                    dailyViewModel.activitiesResponse.observe(viewLifecycleOwner){
                        showToast(it.message)
                    }
                    if(selectedId =="" ){
                        conInName.error = getString(R.string.empty_selection)
                    }else if(inAmount.text.toString().toInt()<=0){
                        inAmount.error = getString(R.string.empty_field)
                    }else{
                        /* check is exist */
                        if(!existActivityList.value.isNullOrEmpty()){
                            lifecycleScope.launch {
                                var activityList = listOf(ActivitiesItem(inAmount.text.toString().toInt(),selectedId))
                                for(i in existList.indices){
                                    Log.d("list", existList[i].exercise.name)
                                    activityList = activityList + listOf(ActivitiesItem(existList[i].amount, existList[i].exercise.id))
                                }
                                dailyViewModel.updateActivity(token, activityList , daily_id)
                                dailyViewModel.getDailyLogbook(token, daily_id)
                            }
                            dialog.dismiss()
                        }else{
                            lifecycleScope.launch {
                                dailyViewModel.addActivity(token, listOf(ActivitiesItem(inAmount.text.toString().toInt(),selectedId)),dateTime.toString())
                                dailyViewModel.getDailyLogbook(token, daily_id)
                            }
                            dialog.dismiss()
                        }
                    }
                }
                btnCancel.setOnClickListener{
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }
    private fun showLoading(isLoading: Boolean) {
        binding.conProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun hideFab(isLoading: Boolean) {
        binding.fabAdd.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}