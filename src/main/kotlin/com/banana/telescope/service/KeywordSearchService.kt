package com.banana.telescope.service

import com.banana.telescope.entity.PlaceEntity
import com.banana.telescope.exception.TelescopeRuntimeException
import com.banana.telescope.model.PlaceDocument
import com.banana.telescope.repository.PlaceCacheRepository
import com.banana.telescope.repository.RecommendRepository
import com.banana.telescope.worker.KakaoPlaceGetter
import com.banana.telescope.worker.NaverPlaceGetter
import com.banana.telescope.worker.SimilarityWorker
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.regex.Pattern
import kotlin.math.abs

@Service
class KeywordSearchService(
    @Autowired
    private val kakaoPlaceGetter: KakaoPlaceGetter,
    @Autowired
    private val naverPlaceGetter: NaverPlaceGetter,
    @Autowired
    private val recommendRepository: RecommendRepository,
    @Autowired
    private val placeCacheRepository: PlaceCacheRepository
) {
    private val similarityWorker = SimilarityWorker()
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun search(keyword: String): List<PlaceDocument> {
        recommendRepository.insertOrIncrease(keyword)
        val cachedPlaces = placeCacheRepository.findById(keyword).orElse(null)
        if(cachedPlaces != null){
            return cachedPlaces.documents
        }

        try {
            val naverDocumentList = naverPlaceGetter.get(keyword) as MutableList
            val remainCount = BASE_COUNT - naverDocumentList.size
            val resultDocumentList = mutableListOf<PlaceDocument>()
            val kakaoDocumentList = mutableListOf<PlaceDocument>()
            try {
                kakaoPlaceGetter.get(keyword, remainCount).forEach { kakaoDocument ->
                    naverDocumentList.find { naverDocument ->
                        naverDocument.compare(kakaoDocument)
                    }?.let {
                        resultDocumentList.add(kakaoDocument)
                        naverDocumentList.remove(it)
                    } ?: let {
                        kakaoDocumentList.add(kakaoDocument)
                    }
                }
                resultDocumentList.addAll(kakaoDocumentList)
                resultDocumentList.addAll(naverDocumentList)
                placeCacheRepository.save(PlaceEntity(
                    keyword = keyword,
                    documents = resultDocumentList
                ))
                return resultDocumentList
            } catch (e: TelescopeRuntimeException.RemoteServerDownException){
                logger.warn("Kakao Api is down.")
                return naverDocumentList
            }
        } catch (e: TelescopeRuntimeException.RemoteServerDownException){
            try {
                logger.warn("Naver Api is down.")
                return kakaoPlaceGetter.get(keyword, BASE_COUNT)
            } catch (e: TelescopeRuntimeException.RemoteServerDownException){
                throw e
            }
        }

    }

    private fun PlaceDocument.compare(target: PlaceDocument): Boolean {
        if (this.name.compareName(target.name)) {
            if (this.address.compareAddress(target.address)) {
                return comparePosition(this.x, this.y, target.x, target.y)
            } else {
                if (this.roadAddress.compareRoadAddress(target.roadAddress)) {
                    return comparePosition(this.x, this.y, target.x, target.y)
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
        private const val BASE_COUNT = 10
        private const val NAME_SIMILARITY_RATE = 0.6
        private const val MARGIN_OF_ERROR = 0.0003
    }
}