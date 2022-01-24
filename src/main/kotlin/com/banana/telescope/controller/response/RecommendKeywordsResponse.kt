package com.banana.telescope.controller.response

import com.banana.telescope.service.search.model.RecommendedKeyword

data class RecommendKeywordsResponse(
    val total: Int,
    val documents: List<RecommendedKeyword>
)