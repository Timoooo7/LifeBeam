package com.example.lifebeam.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lifebeam.R
import com.example.lifebeam.databinding.FragmentBodyBinding
import com.example.lifebeam.databinding.FragmentProfileBinding

class BodyFragment : Fragment() {
    private lateinit var binding: FragmentBodyBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBodyBinding.inflate(layoutInflater)
        return binding.root
    }
    companion object {
        const val ARG_POSITION = "position"
    }
}