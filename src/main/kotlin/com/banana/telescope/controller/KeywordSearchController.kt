package com.banana.telescope.controller

import com.banana.telescope.exception.TelescopeRuntimeException
import com.banana.telescope.controller.response.PlacesResponse
import com.banana.telescope.controller.response.RecommendKeywordsResponse
import com.banana.telescope.service.search.KeywordRecommendService
import com.banana.telescope.service.search.KeywordSearchService
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
    fun search(@RequestParam("keyword", required = true) keyword: String): PlacesResponse {
        keyword.trimStart().trimEnd().let { it ->
            if (it.isEmpty()) {
                throw TelescopeRuntimeException.InvalidParameterException("'keyword' can not empty parameter.")
            }
            keywordSearchService.search(it).let { documentList ->
                return PlacesResponse(
                    total = documentList.size,
                    documents = documentList
                )
            }
        }
    }

    @GetMapping("/v1/place/search/recommend")
    fun recommend(): RecommendKeywordsResponse {
        val recommends =  keywordRecommendService.recommend()
        return RecommendKeywordsResponse(
            total = recommends.size,
            documents = recommends
        )
    }
}