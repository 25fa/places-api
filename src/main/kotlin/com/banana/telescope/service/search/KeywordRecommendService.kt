package com.banana.telescope.service.search

import com.banana.telescope.service.search.response.RecommendKeywordsResponse
import com.banana.telescope.database.repository.RecommendRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class KeywordRecommendService(
    @Autowired
    private val recommendRepository: RecommendRepository
) {
    fun recommend(): RecommendKeywordsResponse {
        val result = recommendRepository.selectTop10()
        return RecommendKeywordsResponse(
            total = result.size,
            documents = result
        )
    }
}