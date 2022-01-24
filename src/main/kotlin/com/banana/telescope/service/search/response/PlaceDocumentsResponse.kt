package com.banana.telescope.service.search.response

import com.banana.telescope.service.search.model.PlaceDocument

data class PlaceDocumentsResponse(
    val total: Int,
    val documents: List<PlaceDocument>
)