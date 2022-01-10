package com.banana.telescope.controller

import com.banana.telescope.service.NaverMapQueryRetriever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class KeywordSearcher(
) {
    @Autowired
    lateinit var naverMapQueryRetriever: NaverMapQueryRetriever

    @GetMapping("/v1/place/search")
    fun getTotalProductList() {
        val test = naverMapQueryRetriever.retrieve()
        print(test)
    }
}