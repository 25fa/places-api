package com.banana.telescope.worker

import com.banana.telescope.model.PlaceDocument
import org.springframework.stereotype.Service
import java.util.regex.Pattern
import kotlin.math.abs

@Service
class DocumentCompareWorker {
    fun compare(source: PlaceDocument, target: PlaceDocument): Boolean {
        if (compareName(source.name, target.name)) {
            if (compareAddress(source.address, target.address)) {
                return comparePosition(source.x, source.y, target.x, target.y)
            } else {
                if (compareRoadAddress(source.roadAddress, target.roadAddress)) {
                    return comparePosition(source.x, source.y, target.x, target.y)
                }
            }
        }
        return false
    }

    private fun compareRoadAddress(source: String, target: String): Boolean {
        getRoad(source)?.let { sourceRoad ->
            getRoad(target)?.let { targetRoad ->
                if (sourceRoad == targetRoad) {
                    return true
                }
            }
        }
        return false
    }

    private fun compareAddress(source: String, target: String): Boolean {
        getTown(source)?.let { sourceTown ->
            getTown(target)?.let { targetTown ->
                if (sourceTown == targetTown) {
                    return true
                }
            }
        }
        return false
    }

    private fun compareName(source: String, target: String): Boolean {
        if (SimilarityWorker.similarity(source, target) > NAME_SIMILARITY_RATE) {
            return true
        }
        return false
    }

    private fun comparePosition(sourceX: Double, sourceY: Double, targetX: Double, targetY: Double): Boolean {
        if (abs(sourceX - targetX) < MARGIN_OF_ERROR) {
            if (abs(sourceY - targetY) < MARGIN_OF_ERROR) {
                return true
            }
        }
        return false
    }

    private fun getRoad(address: String): String? {
        val regex = "([가-힣0-9]{1,8}([길로]) ([0-9-]{0,5})?)"
        val matcher = Pattern.compile(regex).matcher(address)
        while (matcher.find()) {
            return matcher.group(1)
        }
        return null
    }

    private fun getTown(address: String): String? {
        val regex = "([가-힣0-9]{1,8}([동리]) ([0-9-]{0,5})?)"
        val matcher = Pattern.compile(regex).matcher(address)
        while (matcher.find()) {
            return matcher.group(1)
        }
        return null
    }

    companion object {
        private const val NAME_SIMILARITY_RATE = 0.6
        private const val MARGIN_OF_ERROR = 0.0003
    }
}