package com.banana.telescope.worker

import com.banana.telescope.exception.TelescopeRuntimeException
import com.banana.telescope.model.KakaoPlaceResponse
import com.banana.telescope.model.PlaceDocument
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class KakaoPlaceGetter(
    @Autowired
    private val kakaoApiCaller: KakaoApiCaller,
) {

    fun get(keyword: String, size: Int): List<PlaceDocument> {
        try {
            val kakaoPlaceResponse = kakaoApiCaller.search(keyword, size)
            if (kakaoPlaceResponse != null) {
                return kakaoPlaceResponse.convert()
            }
            return listOf()
        } catch (e: TelescopeRuntimeException.RemoteServerDownException){
            throw e
        }

    }

    private fun KakaoPlaceResponse.convert(): List<PlaceDocument> {
        val documents = mutableListOf<PlaceDocument>()
        this.documents.forEach {
            documents.add(
                PlaceDocument(
                    name = it.name,
                    url = it.url,
                    phone = it.phone,
                    address = it.address,
                    roadAddress = it.roadAddress,
                    x = it.x.toDouble(),
                    y = it.y.toDouble()
                )
            )
        }
        return documents
    }
}