package com.banana.telescope.worker.rest.retrofit2

import com.banana.telescope.model.NaverPlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverApis {

    @GET("v1/search/local.json")
    fun search(
        @Header("X-Naver-Client-Id") id: String,
        @Header("X-Naver-Client-Secret") secret: String,
        @Query("query") query: String,
        @Query("display") display: Int,
        @Query("start") start: Int,
        @Query("sort") sort: String
    ): Call<NaverPlaceResponse>
}