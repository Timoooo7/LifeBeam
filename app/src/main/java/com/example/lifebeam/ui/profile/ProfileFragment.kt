package com.example.lifebeam.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.lifebeam.data.repository.UserModel
import com.example.lifebeam.databinding.FragmentProfileBinding
import com.example.lifebeam.ui.utils.ViewModelFactory
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private var isNew: Boolean = true
    private val profileViewModel by viewModels<ProfileViewModel>{
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { it ->
            /* get data from intent */
            val uid = it.getString("uid")
            val token = it.getString("token")
            val name = it.getString("name")
            val email = it.getString("email")
            /* Get data from server */
            if (token != null && uid != null) {
                lifecycleScope.launch {
                    profileViewModel.getUser(token,uid)
                }
            }
            binding.apply {
                /* Showing data */
                tvName.text = name
                tvEmail.text = email
                profileViewModel.isLoading.observe(viewLifecycleOwner){
                    showLoading(it)
                }
                profileViewModel.userResponse.observe(viewLifecycleOwner){ user ->
                    if(user!=null){
                        isNew = false
                        inAge.setText(user.age.toString())
                        val gender = user.gender
                        when (gender) {
                            "MALE" -> {
                                genderMale.isChecked = true
                                genderFemale.isChecked = false
                            }
                            "FEMALE" -> {
                                genderFemale.isChecked = true
                                genderMale.isChecked = false
                            }
                            else -> {
                                genderMale.isChecked = false
                                genderFemale.isChecked = false
                            }
                        }
                    }else{
                        isNew = true
                    }
                }
                /* Action */
                btnUpdate.setOnClickListener{
                    val inputAge = inAge.text.toString()
                    val inputGender = if(genderMale.isChecked){
                        "MALE"
                    }else{
                        "FEMALE"
                    }
                    if(isNew){
                        lifecycleScope.launch {
                            profileViewModel.addUser(token.toString(),inputAge.toInt(),inputGender)
                        }
                    }else{
                        lifecycleScope.launch {
                            profileViewModel.updateUser(token.toString(),inputAge.toInt(),inputGender,uid.toString())
                        }
                    }
                }
                profileViewModel.addRespopnse.observe(viewLifecycleOwner){
                    showToast(it)
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