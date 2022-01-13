package com.banana.telescope.controller

import com.banana.telescope.exception.TelescopeRuntimeException
import com.banana.telescope.model.PlaceResponse
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
    fun search(@RequestParam("keyword", required = true) keyword: String): PlaceResponse {
        keyword.trimStart().trimEnd().let { it ->
            if (it.isEmpty()) {
                throw TelescopeRuntimeException.InvalidParameterException("'keyword' can not empty parameter.")
            }
            keywordSearchService.search(it).let { documentList ->
                return PlaceResponse(
                    total = documentList.size,
                    documents = documentList
                )
            }
        }
    }

    @GetMapping("/v1/place/search/recommend")
    fun recommend(): RecommendKeywordResponse {
        return keywordRecommendService.recommend()
    }
}