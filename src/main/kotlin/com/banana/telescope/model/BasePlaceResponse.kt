package com.banana.telescope.model

import com.google.gson.annotations.SerializedName

data class BasePlaceResponse(
    val total: Int,
    val documents: List<Document>
) {
    data class Document(
        val name: String,
        val url: String,
        val phone: String,
        val address: String,
        val roadAddress: String,
        val x: Double,
        val y: Double
    )
}
