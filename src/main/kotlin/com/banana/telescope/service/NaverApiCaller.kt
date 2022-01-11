package com.banana.telescope.service

import com.banana.telescope.model.NaverPlaceResponse
import com.banana.telescope.retrofit2.NaverApis
import com.banana.telescope.retrofit2.RetrofitClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.IOException
import javax.annotation.PostConstruct

@Service
class NaverApiCaller {
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

    fun search(keyword: String): NaverPlaceResponse? {
        val call = apis.search(id, secret, keyword, 5, 1, "random")
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