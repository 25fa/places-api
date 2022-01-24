package com.banana.telescope.service.search.response

import com.banana.telescope.service.search.model.RecommendedKeyword

data class RecommendKeywordsResponse(
    val total: Int,
    val documents: List<RecommendedKeyword>
)