package com.banana.telescope.worker

import com.banana.telescope.model.BasePlaceResponse
import com.banana.telescope.model.KakaoPlaceResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class KakaoPlaceGetter(
    @Autowired
    val kakaoApiCaller: KakaoApiCaller,
) {
    fun get(keyword: String, size: Int): BasePlaceResponse {
        val kakaoPlaceResponse = kakaoApiCaller.search(keyword, size)
        if (kakaoPlaceResponse != null) {
            return kakaoPlaceResponse.convert()
        }
        //todo
        throw Exception()
    }

    private fun KakaoPlaceResponse.convert() : BasePlaceResponse {
        val documents = mutableListOf<BasePlaceResponse.Document>()
        this.documents.forEach {
            documents.add(
                BasePlaceResponse.Document(
                name = it.name,
                url = it.url,
                phone = it.phone,
                address = it.address,
                roadAddress = it.roadAddress,
                x = it.x.toDouble(),
                y = it.y.toDouble()
            ))
        }

        return BasePlaceResponse(
            total = this.meta.totalCount,
            documents = documents
        )
    }
}