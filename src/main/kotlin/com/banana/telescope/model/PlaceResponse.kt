package com.banana.telescope.model

import com.banana.telescope.service.search.model.PlaceDocument

data class PlaceResponse(
    val total: Int,
    val documents: List<PlaceDocument>
)
