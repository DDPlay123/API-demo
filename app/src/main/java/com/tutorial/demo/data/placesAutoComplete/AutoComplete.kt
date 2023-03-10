package com.tutorial.demo.data.placesAutoComplete

data class AutoComplete(
    val predictions: List<Prediction>,
    val status: String
)