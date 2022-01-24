package com.banana.telescope.rest.response

import com.google.gson.annotations.SerializedName

data class KakaoPlaceResponse(
    val meta: Meta,
    val documents: List<Document>
) {
    data class Meta(
        @SerializedName("same_name")
        val sameName: Keyword,
        @SerializedName("pageable_count")
        val pageableCount: Int,
        @SerializedName("total_count")
        val totalCount: Int,
        @SerializedName("is_end")
        val isEnd: Boolean
    ) {
        data class Keyword(
            val region: List<String>,
            @SerializedName("keyword")
            val keyword: String,
            @SerializedName("selected_region")
            val selectedRegion: String
        )
    }

    data class Document(
        @SerializedName("place_name")
        val name: String,
        val distance: String,
        @SerializedName("place_url")
        val url: String,
        @SerializedName("category_name")
        val category: String,
        val id: String,
        val phone: String,
        @SerializedName("address_name")
        val address: String,
        @SerializedName("road_address_name")
        val roadAddress: String,
        val x: String,
        val y: String,
        @SerializedName("category_group_code")
        val categoryGroupCode: String,
        @SerializedName("category_group_name")
        val categoryGroupName: String
    )
}
