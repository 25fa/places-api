package com.banana.telescope.worker

import com.banana.telescope.model.BasePlaceResponse
import com.banana.telescope.model.NaverPlaceResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NaverPlaceGetter(
    @Autowired
    val kakaoApiCaller: KakaoApiCaller,
    @Autowired
    val naverApiCaller: NaverApiCaller
) {
    fun get(keyword: String): BasePlaceResponse {
        val naverPlaceResponse = naverApiCaller.search(keyword)
        if (naverPlaceResponse != null) {
            return naverPlaceResponse.convert()
        }
        //todo
        throw Exception()
    }

    private fun NaverPlaceResponse.convert() : BasePlaceResponse {
        val documents = mutableListOf<BasePlaceResponse.Document>()
        this.items.forEach {
            val coordinate = toWGS84(it.x.toDouble(), it.y.toDouble())
            documents.add(
                BasePlaceResponse.Document(
                name = it.name.reformatName(),
                url = it.url,
                phone = it.phone,
                address = it.address,
                roadAddress = it.roadAddress,
                x = coordinate.first,
                y = coordinate.second
            ))
        }

        return BasePlaceResponse(
            total = this.total,
            documents = documents
        )
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

    private fun String.reformatName() : String{
        return this.replace("<b>", "").replace("</b>", "")
    }

    companion object{
        private const val BASE_COUNT = 10
    }

}