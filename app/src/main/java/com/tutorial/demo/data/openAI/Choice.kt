package com.tutorial.demo.data.openAI

data class Choice(
    val message: Messages,
    val finish_reason: String,
    val index: Int
)