package com.banana.telescope.model

data class ErrorResponse(
    val status: Int,
    val error: String,
    //val code: String,
    val reason: String,
)