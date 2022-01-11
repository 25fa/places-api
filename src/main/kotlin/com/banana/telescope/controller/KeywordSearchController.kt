package com.banana.telescope.controller

import com.banana.telescope.worker.KakaoApiCaller
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class KeywordSearchController(
) {
    @Autowired
    lateinit var naverPlaceRetriever: KakaoApiCaller

    @GetMapping("/v1/place/search")
    fun getTotalProductList() {
        val test = naverPlaceRetriever.retrieve()
        print(test)
    }
}