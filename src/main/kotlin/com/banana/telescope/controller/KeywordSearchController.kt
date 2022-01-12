package com.banana.telescope.controller

import com.banana.telescope.model.BasePlaceResponse
import com.banana.telescope.model.RecommendKeywordResponse
import com.banana.telescope.service.KeywordRecommendService
import com.banana.telescope.service.KeywordSearchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class KeywordSearchController(
    @Autowired
    val keywordSearchService: KeywordSearchService,
    @Autowired
    val keywordRecommendService: KeywordRecommendService
) {


    @GetMapping("/v1/place/search")
    fun search(@RequestParam("keyword") keyword: String): BasePlaceResponse {
        return keywordSearchService.search(keyword)
    }

    @GetMapping("/v1/place/search/recommend")
    fun recommend(): RecommendKeywordResponse {
        return keywordRecommendService.recommend()
    }
}