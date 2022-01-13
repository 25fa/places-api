package com.banana.telescope.repository

import com.banana.telescope.entity.PlaceEntity
import org.springframework.data.repository.CrudRepository

interface PlaceCacheRepository : CrudRepository<PlaceEntity, String> {
}