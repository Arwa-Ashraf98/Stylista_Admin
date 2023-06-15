package com.mad43.staylistaadmin.splash.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mad43.staylistaadmin.R
import com.mad43.staylistaadmin.base.data.local.MySharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.Main) {
            delay(1000L)
            // TODO
            if (MySharedPreferences.getShopName()
                    .isNotEmpty() && MySharedPreferences.getShopPassword().isNotEmpty()
            ) {
                findNavController().navigate(R.id.action_splashFragment2_to_homeFragment2)
            } else {
                findNavController().navigate(R.id.action_splashFragment2_to_loginFragment)
            }

        }
    }


}