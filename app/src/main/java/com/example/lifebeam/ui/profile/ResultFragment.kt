package com.example.lifebeam.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.lifebeam.R
import com.example.lifebeam.databinding.FragmentResultBinding
import com.example.lifebeam.ui.utils.ViewModelFactory
import kotlinx.coroutines.launch


class ResultFragment : Fragment() {
    private lateinit var binding:FragmentResultBinding
    private val profileViewModel by viewModels<ProfileViewModel>{
        ViewModelFactory.getInstance(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { bundle ->
            /* get data */
            profileViewModel.isLoading.observe(viewLifecycleOwner){
                showLoading(it)
            }
            lifecycleScope.launch {
                profileViewModel.getResult(bundle.getString("token").toString(),bundle.getString("uid").toString())
            }
            binding.apply {
                profileViewModel.resultResponse.observe(viewLifecycleOwner){
                    tvStatus.text = it.status
                    tvCalorie.text = it.calorieOffset.toString()
                    tvDayLeft.text = getString(R.string.day_left_to, it.dayLeft.toString(), it.dayLeftTo)
                    tvMsg.text = it.clientMessage
                    showToast(it.message)
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
}