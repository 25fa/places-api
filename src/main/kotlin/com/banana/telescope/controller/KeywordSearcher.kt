package com.banana.telescope.controller

import com.banana.telescope.service.NaverPlaceRetriever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class KeywordSearcher(
) {
    @Autowired
    lateinit var naverPlaceRetriever: NaverPlaceRetriever

    @GetMapping("/v1/place/search")
    fun getTotalProductList() {
        val test = naverPlaceRetriever.retrieve()
        print(test)
    }
}