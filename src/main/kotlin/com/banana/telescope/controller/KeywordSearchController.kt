package com.banana.telescope.controller

import com.banana.telescope.service.KeywordSearchService
import com.banana.telescope.worker.KakaoApiCaller
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class KeywordSearchController(
) {
    @Autowired
    lateinit var keywordSearchService: KeywordSearchService

    @GetMapping("/v1/place/search")
    fun search() {
        val test = keywordSearchService.search("조선옥")
        print(test)
    }
}