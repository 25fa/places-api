package com.banana.telescope.model

data class PlaceDocument(
    val name: String,
    val url: String,
    val phone: String,
    val address: String,
    val roadAddress: String,
    val x: Double,
    val y: Double
)

