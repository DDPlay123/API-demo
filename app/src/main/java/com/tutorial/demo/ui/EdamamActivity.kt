package com.tutorial.demo.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.tutorial.demo.R
import com.tutorial.demo.databinding.ActivityEdamamBinding

// Nutrition Analysis API: https://developer.edamam.com/edamam-docs-nutrition-api
// Food Database API: https://developer.edamam.com/food-database-api-docs
// Recipe Search API: https://developer.edamam.com/edamam-docs-recipe-api
class EdamamActivity : BaseActivity() {
    private val binding: ActivityEdamamBinding by lazy {
        ActivityEdamamBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setActionBar()
        setListener()
    }

    private fun setActionBar() {
        binding.actionbar.apply {
            tvTitle.text = getString(R.string.actionbar_edamam)
            imgBack.setOnClickListener { finish() }
        }
    }

    private fun setListener() {
        binding.run {
            btnNutrition.setOnClickListener {
                openURL("https://developer.edamam.com/edamam-docs-nutrition-api")
            }

            btnFoodDatabase.setOnClickListener {
                openURL("https://developer.edamam.com/food-database-api-docs")
            }

            btnRecipeSearch.setOnClickListener {
                openURL("https://developer.edamam.com/edamam-docs-recipe-api")
            }
        }
    }

    private fun openURL(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
}