package com.banana.telescope.service.keyword.response

import com.banana.telescope.service.keyword.model.RecommendedKeyword

data class RecommendKeywordsResponse(
    val total: Int,
    val documents: List<RecommendedKeyword>
)