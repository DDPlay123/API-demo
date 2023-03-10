package com.tutorial.demo.ui

import android.os.Bundle
import com.tutorial.demo.databinding.ActivityMainBinding
import com.tutorial.demo.utils.requestLocationPermission

class MainActivity : BaseActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setListener()
    }

    private fun setListener() {
        binding.run {
            btnOpenAI.setOnClickListener {
                start(OpenAIActivity::class.java)
            }

            btnEdamam.setOnClickListener {
                start(EdamamActivity::class.java)
            }

            btnGooglePlaces.setOnClickListener {
                if (!requestLocationPermission()) return@setOnClickListener
                start(PlacesActivity::class.java)
            }
        }
    }
}