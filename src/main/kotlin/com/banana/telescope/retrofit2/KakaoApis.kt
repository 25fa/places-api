package com.banana.telescope.retrofit2

import com.banana.telescope.model.KakaoPlaceResponse
import com.banana.telescope.model.KakaoTranscoordResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoApis {

    @GET("v2/local/search/keyword.json")
    fun search(
        @Header("Authorization") key: String,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<KakaoPlaceResponse>

    @GET("v2/local/geo/transcoord.json")
    fun transcoord(
        @Header("Authorization") key: String,
        @Query("x") x: Double,
        @Query("y") y: Double,
        @Query("input_coord") inputCoord: String,
        @Query("output_coord") outputCoord: String,
    ): Call<KakaoTranscoordResponse>
}