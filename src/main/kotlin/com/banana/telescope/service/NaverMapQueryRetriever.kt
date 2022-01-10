package com.banana.telescope.service

import com.banana.telescope.model.NaverPlaceQueryResponse
import com.banana.telescope.retrofit2.NaverApis
import com.banana.telescope.retrofit2.RetrofitClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.IOException
import javax.annotation.PostConstruct

@Service
class NaverMapQueryRetriever {
    @Value("\${retrofit.naver.url}")
    lateinit var url: String
    @Value("\${retrofit.naver.client.id}")
    lateinit var id: String
    @Value("\${retrofit.naver.client.secret}")
    lateinit var secret: String

    private lateinit var apis: NaverApis

    @PostConstruct
    fun init() {
        apis = RetrofitClientBuilder.build(url)
    }

    fun retrieve(): NaverPlaceQueryResponse? {
        val call = apis.local(id, secret, "조선옥", 10, 1, "random")
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: IOException) {
            print(e.message)
        }
        return null
    }
}