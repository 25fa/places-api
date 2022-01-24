package com.banana.telescope.service.search

import com.banana.telescope.database.repository.RecommendRepository
import com.banana.telescope.service.search.model.RecommendedKeyword
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class KeywordRecommendService(
    @Autowired
    private val recommendRepository: RecommendRepository
) {
    fun recommend(): List<RecommendedKeyword> {
        return recommendRepository.selectTop10()
    }
}