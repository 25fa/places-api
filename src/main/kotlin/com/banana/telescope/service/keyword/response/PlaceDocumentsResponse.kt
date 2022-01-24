package com.banana.telescope.service.keyword.response

import com.banana.telescope.service.keyword.model.PlaceDocument

data class PlaceDocumentsResponse(
    val total: Int,
    val documents: List<PlaceDocument>
)