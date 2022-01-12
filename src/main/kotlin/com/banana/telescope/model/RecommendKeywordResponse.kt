package com.banana.telescope.model

data class RecommendKeywordResponse(
    val total: Int,
    val documents: List<RecommendedKeyword>
)