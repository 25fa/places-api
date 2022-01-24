package com.banana.telescope.database.entity

import com.banana.telescope.service.search.model.PlaceDocument
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash(value = "place_cache", timeToLive = 180)
data class PlaceEntity(
    @Id
    val keyword: String,
    val documents: List<PlaceDocument>
)
