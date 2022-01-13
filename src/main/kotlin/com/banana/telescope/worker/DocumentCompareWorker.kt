package com.banana.telescope.worker

import com.banana.telescope.model.PlaceDocument
import java.util.regex.Pattern
import kotlin.math.abs

class DocumentCompareWorker(
    private val similarityWorker: SimilarityWorker
) {
    fun compare(source: PlaceDocument, target: PlaceDocument): Boolean {
        if (source.name.compareName(target.name)) {
            if (source.address.compareAddress(target.address)) {
                return comparePosition(source.x, source.y, target.x, target.y)
            } else {
                if (source.roadAddress.compareRoadAddress(target.roadAddress)) {
                    return comparePosition(source.x, source.y, target.x, target.y)
                }
            }
        }

        return false
    }

    private fun String.compareRoadAddress(target: String): Boolean {
        this.getRoad()?.let { sourceAddress ->
            target.getRoad()?.let { targetAddress ->
                if (sourceAddress == targetAddress) {
                    return true
                }
            }
        }
        return false
    }

    private fun String.compareAddress(target: String): Boolean {
        this.getTown()?.let { sourceAddress ->
            target.getTown()?.let { targetAddress ->
                if (sourceAddress == targetAddress) {
                    return true
                }
            }
        }
        return false
    }

    private fun String.getRoad(): String? {
        val regex = "([가-힣0-9]{1,8}([길로]) ([0-9-]{0,5})?)"
        val matcher = Pattern.compile(regex).matcher(this)
        while (matcher.find()) {
            return matcher.group(1)
        }
        return null
    }

    private fun String.getTown(): String? {
        val regex = "([가-힣0-9]{1,8}([동리]) ([0-9-]{0,5})?)"
        val matcher = Pattern.compile(regex).matcher(this)
        while (matcher.find()) {
            return matcher.group(1)
        }
        return null
    }


    private fun String.compareName(target: String): Boolean {
        if (similarityWorker.similarity(this, target) > NAME_SIMILARITY_RATE) {
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

    companion object {
        private const val NAME_SIMILARITY_RATE = 0.6
        private const val MARGIN_OF_ERROR = 0.0003
    }
}