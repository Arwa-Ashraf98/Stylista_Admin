package com.mad43.staylistaadmin.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.mad43.staylistaadmin.R
import com.mad43.staylistaadmin.databinding.ActivityMainBinding
import com.mad43.staylistaadmin.main.viewmodel.MainViewModel
import com.mad43.staylistaadmin.utils.NetworkConnectivityObserver
import com.mad43.staylistaadmin.utils.NetworkStatus
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        checkNetwork()
    }

    private fun checkNetwork() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                mainViewModel.networkStatusFlow.collect { status ->
                    if (status.equals("Available", true)) {
                        binding?.testViewNoConnection?.visibility = View.GONE
                        Log.e("TAG", status)
                    } else {
                        binding?.testViewNoConnection?.visibility = View.VISIBLE
                        Log.e("TAG", status)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}