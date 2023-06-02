package com.mad43.staylistaadmin.home.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mad43.staylistaadmin.R
import com.mad43.staylistaadmin.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClicks()
    }

    private fun onClicks(){
        binding.apply {
            btnProduct.setOnClickListener {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}