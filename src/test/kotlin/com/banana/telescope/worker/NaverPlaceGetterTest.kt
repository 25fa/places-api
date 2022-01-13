package com.banana.telescope.worker

import com.banana.telescope.model.KakaoTranscoordResponse
import com.banana.telescope.model.NaverPlaceResponse
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Assert
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class NaverPlaceGetterTest {
    @InjectMockKs
    lateinit var naverPlaceGetter: NaverPlaceGetter

    @MockK
    lateinit var kakaoApiCaller: KakaoApiCaller

    @MockK
    lateinit var naverApiCaller: NaverApiCaller

    @MockK
    lateinit var naverPlaceResponse: NaverPlaceResponse

    @MockK
    lateinit var item: NaverPlaceResponse.Item

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun test_get_success() {
        every {
            naverApiCaller.search("조선옥")
        } returns naverPlaceResponse

        every {
            naverPlaceResponse.items
        } returns listOf(item)

        every {
            item.x
        } returns "335151"

        every {
            item.y
        } returns "433300"

        every {
            kakaoApiCaller.transcoord(335151.0, 433300.0)
        } returns KakaoTranscoordResponse(
            documents = listOf(KakaoTranscoordResponse.Document("127.009", "127.009")),
            meta = KakaoTranscoordResponse.Meta(totalCount = 1)
        )

        every {
            item.name
        } returns "<b>조선옥</b>"

        every {
            item.url
        } returns "url"

        every {
            item.phone
        } returns "url"

        every {
            item.address
        } returns "세종특별자치시  연기면 세종리 1201"

        every {
            item.roadAddress
        } returns "세종특별자치시  연기면 호수공원길 155"

        val result = naverPlaceGetter.get("조선옥")

        Assert.assertEquals("세종 연기면 세종리 1201", result.get(0).address)
    }
}