package com.banana.telescope.rest.response

import com.google.gson.annotations.SerializedName

data class KakaoTranscoordResponse(
    val meta: Meta,
    val documents: List<Document>
) {
    data class Meta(
        @SerializedName("total_count")
        val totalCount: Int
    )

    data class Document(
        val x: String,
        val y: String,
    )
}
