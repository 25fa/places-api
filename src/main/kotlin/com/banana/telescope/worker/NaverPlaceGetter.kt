package com.banana.telescope.worker

import com.banana.telescope.model.NaverPlaceResponse
import com.banana.telescope.model.PlaceDocument
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NaverPlaceGetter(
    @Autowired
    private val kakaoApiCaller: KakaoApiCaller,
    @Autowired
    private val naverApiCaller: NaverApiCaller
) {
    fun get(keyword: String): List<PlaceDocument> {
        val naverPlaceResponse = naverApiCaller.search(keyword)
        if (naverPlaceResponse != null) {
            return naverPlaceResponse.convert()
        }
        //todo
        throw Exception()
    }

    private fun NaverPlaceResponse.convert(): List<PlaceDocument> {
        val documents = mutableListOf<PlaceDocument>()
        this.items.forEach {
            val coordinate = toWGS84(it.x.toDouble(), it.y.toDouble())
            documents.add(
                PlaceDocument(
                    name = it.name.reformatName(),
                    url = it.url,
                    phone = it.phone,
                    address = reformatState(it.address),
                    roadAddress = reformatState(it.roadAddress),
                    x = coordinate.first,
                    y = coordinate.second
                )
            )
        }
        return documents
    }

    private fun toWGS84(x: Double, y: Double): Pair<Double, Double> {
        kakaoApiCaller.transcoord(x, y)?.let { response ->
            response.documents[0].let {
                return Pair(it.x.toDouble(), it.y.toDouble())
            }
        }
        //todo
        throw Exception()
    }

    private fun reformatState(address: String): String {
        return buildString {
            address.split(" ").forEachIndexed { index, value ->
                if (index == 0) {
                    append(value.replace("(특별|자치|광역|시|도|청|라|상)".toRegex(), ""))
                } else {
                    append(" $value")
                }
            }
        }
    }

    private fun String.reformatName(): String {
        return this.replace("<b>", "").replace("</b>", "")
    }
}