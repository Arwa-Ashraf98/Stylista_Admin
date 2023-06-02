package com.mad43.staylistaadmin.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.mad43.staylistaadmin.R
import com.mad43.staylistaadmin.databinding.ActivityMainBinding
import com.mad43.staylistaadmin.main.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        mainViewModel = MainViewModel()
        checkNetwork()
    }

    private fun checkNetwork() {
        lifecycleScope.launchWhenCreated {
            mainViewModel.networkStatusFlow.collect { status ->
                if (status.equals("Available", true)) {
                    binding?.testViewNoConnection?.visibility = View.GONE
                    binding?.testViewNoConnection?.text = getString(R.string.available)
                    Log.e("TAG", status)
                } else {
                    binding?.testViewNoConnection?.text = getString(R.string.lost)
                    Log.e("TAG", status)
                    binding?.testViewNoConnection?.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}