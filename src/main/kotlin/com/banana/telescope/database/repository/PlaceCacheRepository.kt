package com.banana.telescope.database.repository

import com.banana.telescope.database.entity.PlaceEntity
import org.springframework.data.repository.CrudRepository

interface PlaceCacheRepository : CrudRepository<PlaceEntity, String> {
}