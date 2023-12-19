package com.example.lifebeam.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lifebeam.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

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
            val position = it.getInt(ARG_POSITION, 1)
            /* get data from intent */
            val name = it.getString("name")
            val email = it.getString("email")

            /* Showing data */
            binding.apply {

            }
        }
    }

    companion object {
        const val ARG_POSITION = "position"
    }
}