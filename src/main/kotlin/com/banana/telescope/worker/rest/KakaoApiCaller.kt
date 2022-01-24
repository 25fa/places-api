package com.banana.telescope.worker.rest

import com.banana.telescope.exception.TelescopeRuntimeException
import com.banana.telescope.model.KakaoPlaceResponse
import com.banana.telescope.model.KakaoTranscoordResponse
import com.banana.telescope.worker.rest.retrofit2.KakaoApis
import com.banana.telescope.worker.rest.retrofit2.RetrofitClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class KakaoApiCaller {
    @Value("\${retrofit.kakao.url}")
    private lateinit var url: String

    @Value("\${retrofit.kakao.client.key}")
    private lateinit var key: String

    private lateinit var apis: KakaoApis
    private lateinit var apiKey: String

    @PostConstruct
    fun init() {
        apis = RetrofitClientBuilder.build(url)
        apiKey = "KakaoAK $key"
    }

    fun search(keyword: String, size: Int): KakaoPlaceResponse? {
        val call = apis.search(apiKey, keyword, 1, size)
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            throw TelescopeRuntimeException.RemoteServerDownException()
        }
        return null
    }

    fun transcoord(x: Double, y: Double): KakaoTranscoordResponse? {
        val call = apis.transcoord(apiKey, x, y, "KTM", "WGS84")
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            throw TelescopeRuntimeException.RemoteServerDownException()
        }
        return null
    }
}