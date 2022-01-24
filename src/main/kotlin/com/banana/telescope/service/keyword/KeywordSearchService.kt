package com.banana.telescope.service.keyword

import com.banana.telescope.database.entity.PlaceEntity
import com.banana.telescope.database.repository.PlaceCacheRepository
import com.banana.telescope.database.repository.RecommendRepository
import com.banana.telescope.exception.TelescopeRuntimeException
import com.banana.telescope.service.keyword.model.PlaceDocument
import com.banana.telescope.service.keyword.response.PlaceDocumentsResponse
import com.banana.telescope.worker.DocumentCompareWorker
import com.banana.telescope.service.keyword.worker.KakaoPlaceGetter
import com.banana.telescope.service.keyword.worker.NaverPlaceGetter
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
        private val placeCacheRepository: PlaceCacheRepository,
        @Autowired
        private val documentCompareWorker: DocumentCompareWorker
) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun search(keyword: String): PlaceDocumentsResponse {
        recommendRepository.insertOrIncrease(keyword)
        val cachedPlaces = placeCacheRepository.findById(keyword).orElse(null)
        if (cachedPlaces != null) {
            return PlaceDocumentsResponse(
                    total = cachedPlaces.documents.size,
                    documents = cachedPlaces.documents)
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
                                documents = resultDocumentList.documents
                        )
                )
                return PlaceDocumentsResponse(
                        total = resultDocumentList.total,
                        documents = resultDocumentList.documents
                )
            } catch (e: TelescopeRuntimeException.RemoteServerDownException) {
                logger.warn("Kakao Api is down.")
                return PlaceDocumentsResponse(
                        total = naverDocumentList.size,
                        documents = naverDocumentList
                )
            }
        } catch (e: TelescopeRuntimeException.RemoteServerDownException) {
            try {
                logger.warn("Naver Api is down.")
                val kakaoDocumentList = kakaoPlaceGetter.get(keyword, BASE_COUNT)
                return PlaceDocumentsResponse(
                        total = kakaoDocumentList.size,
                        documents = kakaoDocumentList
                )
            } catch (e: TelescopeRuntimeException.RemoteServerDownException) {
                throw e
            }
        }

    }

    private fun mergePlaceDocument(
            source: List<PlaceDocument>,
            target: MutableList<PlaceDocument>
    ): PlaceDocumentsResponse {
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

        return PlaceDocumentsResponse(
                total = result.size,
                documents = result
        )
    }

    companion object {
        private const val BASE_COUNT = 10
    }
}