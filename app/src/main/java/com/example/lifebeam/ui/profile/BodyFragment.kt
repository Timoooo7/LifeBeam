package com.example.lifebeam.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.lifebeam.R
import com.example.lifebeam.databinding.FragmentBodyBinding
import com.example.lifebeam.databinding.FragmentProfileBinding
import com.example.lifebeam.ui.utils.ViewModelFactory
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.floor

class BodyFragment : Fragment() {
    private lateinit var binding: FragmentBodyBinding
    private var isNew: Boolean = true
    private val profileViewModel by viewModels<ProfileViewModel>{
        ViewModelFactory.getInstance(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBodyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { it ->
            /* get data from intent */
            val uid = it.getString("uid")
            val token = it.getString("token")
            val year = Calendar.getInstance().weekYear
            val week = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)/2
            val weekTime = "${year}-${floor(week.toDouble()).toInt()}"
            val biWeeklyId = "${uid}-$weekTime"

            /* Get data from server */
            if (token != null) {
                lifecycleScope.launch {
                    profileViewModel.getBiWeekly(token, biWeeklyId)
                }
            }
            binding.apply {
                profileViewModel.isLoading.observe(viewLifecycleOwner){
                    showLoading(it)
                }
                profileViewModel.biWeeklyResponse.observe(viewLifecycleOwner){ biweekly ->
                    if (biweekly != null){
                        isNew = false
                        inHeight.setText(biweekly.height)
                        inWeight.setText(biweekly.weight)
                        showToast(biweekly.message)
                    }else{
                        isNew = true
                    }
                }
                btnUpdate.setOnClickListener{
                    val inputHeight = inHeight.text.toString()
                    val inputWeight = inWeight.text.toString()
                    if(isNew){
                        lifecycleScope.launch {
                            profileViewModel.addBiWeekly(token.toString(),weekTime,inputHeight.toFloat(),inputWeight.toFloat())
                            /* Refresh data from server */
                            if (token != null) {
                                lifecycleScope.launch {
                                    profileViewModel.getBiWeekly(token, biWeeklyId)
                                }
                            }
                        }
                    }else{
                        lifecycleScope.launch {
                            profileViewModel.updateBiWeekly(token.toString(),"",inputHeight.toFloat(),inputWeight.toFloat(),biWeeklyId)
                            /* Get data from server */
                            if (token != null) {
                                lifecycleScope.launch {
                                    profileViewModel.getBiWeekly(token, biWeeklyId)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.conProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    companion object {
        const val ARG_POSITION = "position"
    }
}