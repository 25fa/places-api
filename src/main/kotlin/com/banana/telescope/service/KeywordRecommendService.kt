package com.banana.telescope.service

import com.banana.telescope.model.RecommendKeywordResponse
import com.banana.telescope.repository.RecommendRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class KeywordRecommendService(
    @Autowired
    private val recommendRepository: RecommendRepository
) {
    fun recommend(): RecommendKeywordResponse {
        val result = recommendRepository.selectTop10()
        return RecommendKeywordResponse(
            total = result.size,
            documents = result
        )
    }
}