package com.banana.telescope.exception

data class ErrorResponse(
    val status: Int,
    val error: String,
    //val code: String,
    val reason: String,
)