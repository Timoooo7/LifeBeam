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
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifebeam.R
import com.example.lifebeam.data.remote.request.ActivitiesItem
import com.example.lifebeam.data.remote.request.DietsItem
import com.example.lifebeam.data.repository.UserModel
import com.example.lifebeam.databinding.DialogFormBinding
import com.example.lifebeam.databinding.FragmentDietBinding
import com.example.lifebeam.ui.utils.ViewModelFactory
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar


class DietFragment : Fragment() {
    private lateinit var binding: FragmentDietBinding
    private val dailyViewModel by viewModels<DailyLogbookViewModel>()
    private val existDietList= MutableLiveData<List<String>>()
    private var token = ""
    private var dateTime = LocalDate.now()
    private var daily_id = ""
    private lateinit var existList:List<com.example.lifebeam.data.remote.response.DietsItem>
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDietBinding.inflate(layoutInflater)
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
                mainViewModel.getSession().observe(viewLifecycleOwner){
                    token = it.token
                    daily_id = it.daily_id
                    getData()
                }
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

    private fun getData(){
        /* Model initiation */
        lifecycleScope.launch {
            existList = dailyViewModel.getDailyLogbook(token, daily_id).diets
        }
        /* Showing data */
        binding.apply {
            dailyViewModel.response.observe(viewLifecycleOwner){ daily ->
                if(daily!=null){
                    existDietList.value = daily.diets.map { it.food.id }
                    val adapter = DietsAdapter(daily.diets)
                    val layoutManager = LinearLayoutManager(requireActivity())
                    binding.rvDiets.layoutManager = layoutManager
                    binding.rvDiets.adapter = adapter
                }
            }
        }
    }

    private fun showDialogForm(){
        val dialogBinding = DialogFormBinding.inflate(layoutInflater)
        val dialog = Dialog(requireActivity())
        lifecycleScope.launch {
            dailyViewModel.getFoodList(token)
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBinding.apply {
            dailyViewModel.listFoodResponse.observe(viewLifecycleOwner){ food ->
                /* Set View */
                conInName.error = null
                inAmount.error = null
                /* Set con tv gone */
                conTvName.visibility = View.GONE
                /* Set data for option */
                val diets = food.foods.filterNot{ diet -> existDietList.value!!.contains(diet.id)}.map { it.name }
                dialog.setContentView(root)
                conInName.hint = getString(R.string.diets)
                val adapterItems = ArrayAdapter(requireActivity(),R.layout.item_list, diets)
                var selectedId = ""
                tvAutoComplete.setAdapter(adapterItems)
                tvAutoComplete.onItemClickListener = AdapterView.OnItemClickListener{ adapterView, view, i, l ->
                    val id = adapterView.getItemAtPosition(i)
                    val selectedItem = food.foods.first() { it.name == id.toString() }
                    tvUnit.text = ""
                    selectedId = selectedItem.id
                }
                btnSubmit.setOnClickListener {
                    dailyViewModel.dietResponse.observe(viewLifecycleOwner){
                        showToast(it.message)
                    }
                    if(selectedId ==""){
                        conInName.error = getString(R.string.empty_selection)
                    }else if(inAmount.text.toString().toInt()<=0){
                        inAmount.error = getString(R.string.empty_field)
                    }else{
                        /* check is exist */
                        if(!existDietList.value.isNullOrEmpty()){
                            lifecycleScope.launch {
                                var dietList = listOf(DietsItem(inAmount.text.toString().toInt(),selectedId))
                                for(i in existList.indices){
                                    dietList = dietList + listOf(DietsItem(existList[i].amount, existList[i].food.id))
                                }
                                dailyViewModel.updateDiet(
                                    token, dietList, daily_id
                                )
                                dailyViewModel.getDailyLogbook(token, daily_id)
                            }
                            dialog.dismiss()
                        }else{
                            lifecycleScope.launch {
                                dailyViewModel.addDiet(token, listOf(
                                    DietsItem(inAmount.text.toString().toInt(),selectedId)
                                ),"$dateTime")
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