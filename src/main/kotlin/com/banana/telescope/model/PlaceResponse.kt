package com.banana.telescope.model

data class PlaceResponse(
    val total: Int,
    val documents: List<PlaceDocument>
)
