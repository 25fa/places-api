package com.banana.telescope.database.repository

import com.banana.telescope.service.keyword.model.RecommendedKeyword
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository

@Repository
class RecommendRepository(
    @Autowired
    private val redisTemplate: StringRedisTemplate
) {
    fun selectTop10(): List<RecommendedKeyword> {
        val result = mutableListOf<RecommendedKeyword>()
        val zSetOperations = redisTemplate.opsForZSet()
        zSetOperations.reverseRangeWithScores(REDIS_KEY, 0, 9)?.forEach {
            result.add(
                RecommendedKeyword(
                    keyword = it.value!!,
                    score = it.score!!.toInt()
                )
            )
        }
        return result
    }

    fun insertOrIncrease(keyword: String) {
        val zSetOperations = redisTemplate.opsForZSet()
        zSetOperations.incrementScore(REDIS_KEY, keyword, DELTA)
    }

    companion object {
        private const val REDIS_KEY = "recommended_keyword"
        private const val DELTA = 1.0

    }
}