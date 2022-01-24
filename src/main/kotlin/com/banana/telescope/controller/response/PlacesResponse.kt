package com.banana.telescope.controller.response

import com.banana.telescope.service.search.model.PlaceDocument

data class PlacesResponse(
    val total: Int,
    val documents: List<PlaceDocument>
)
