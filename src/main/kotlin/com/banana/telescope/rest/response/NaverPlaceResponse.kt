package com.banana.telescope.rest.response

import com.google.gson.annotations.SerializedName


data class NaverPlaceResponse(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<Item>
) {
    data class Item(
        @SerializedName("title")
        val name: String,
        @SerializedName("link")
        val url: String,
        val category: String,
        val description: String,
        @SerializedName("telephone")
        val phone: String,
        val address: String,
        val roadAddress: String,
        @SerializedName("mapx")
        val x: String,
        @SerializedName("mapy")
        val y: String
    )
}
