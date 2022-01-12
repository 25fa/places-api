package com.banana.telescope.service

import com.banana.telescope.repository.RedisRepository
import com.banana.telescope.model.RecommendKeywordResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class KeywordRecommendService(
    @Autowired
    private val redisRepository: RedisRepository
) {
    fun recommend(): RecommendKeywordResponse {
        val result = redisRepository.selectTop10()
        return RecommendKeywordResponse(
            total = result.size,
            documents = result
        )
    }
}