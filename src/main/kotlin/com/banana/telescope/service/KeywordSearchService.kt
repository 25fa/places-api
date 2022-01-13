package com.banana.telescope.service

import com.banana.telescope.entity.PlaceEntity
import com.banana.telescope.exception.TelescopeRuntimeException
import com.banana.telescope.model.PlaceDocument
import com.banana.telescope.repository.PlaceCacheRepository
import com.banana.telescope.repository.RecommendRepository
import com.banana.telescope.worker.DocumentCompareWorker
import com.banana.telescope.worker.KakaoPlaceGetter
import com.banana.telescope.worker.NaverPlaceGetter
import com.banana.telescope.worker.SimilarityWorker
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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
    private val documentCompareWorker = DocumentCompareWorker(SimilarityWorker())
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun search(keyword: String): List<PlaceDocument> {
        recommendRepository.insertOrIncrease(keyword)
        val cachedPlaces = placeCacheRepository.findById(keyword).orElse(null)
        if (cachedPlaces != null) {
            return cachedPlaces.documents
        }

        try {
            val naverDocumentList = naverPlaceGetter.get(keyword)
            try {
                val remainCount = BASE_COUNT - naverDocumentList.size
                val kakaoDocumentList = kakaoPlaceGetter.get(keyword, remainCount)
                val resultDocumentList = mergePlaceDocument(kakaoDocumentList, naverDocumentList as MutableList)

                placeCacheRepository.save(
                    PlaceEntity(
                        keyword = keyword,
                        documents = resultDocumentList
                    )
                )
                return resultDocumentList
            } catch (e: TelescopeRuntimeException.RemoteServerDownException) {
                logger.warn("Kakao Api is down.")
                return naverDocumentList
            }
        } catch (e: TelescopeRuntimeException.RemoteServerDownException) {
            try {
                logger.warn("Naver Api is down.")
                return kakaoPlaceGetter.get(keyword, BASE_COUNT)
            } catch (e: TelescopeRuntimeException.RemoteServerDownException) {
                throw e
            }
        }

    }

    private fun mergePlaceDocument(
        source: List<PlaceDocument>,
        target: MutableList<PlaceDocument>
    ): List<PlaceDocument> {
        val result = mutableListOf<PlaceDocument>()
        val remain = mutableListOf<PlaceDocument>()

        source.forEach { sourceDocument ->
            target.find { targetDocument ->
                documentCompareWorker.compare(targetDocument, sourceDocument)
            }?.let {
                result.add(sourceDocument)
                target.remove(it)
            } ?: let {
                remain.add(sourceDocument)
            }
        }
        result.addAll(remain)
        result.addAll(target)

        return result
    }

    companion object {
        private const val BASE_COUNT = 10
    }
}