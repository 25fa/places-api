package com.banana.telescope.controller

import com.banana.telescope.exception.TelescopeRuntimeException
import com.banana.telescope.service.keyword.model.PlaceDocument
import com.banana.telescope.service.keyword.KeywordRecommendService
import com.banana.telescope.service.keyword.KeywordSearchService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Assert

import org.junit.Test

class KeywordSearchControllerTest {
    @MockK
    lateinit var keywordRecommendService: KeywordRecommendService

    @MockK
    lateinit var keywordSearchService: KeywordSearchService

    @InjectMockKs
    lateinit var keywordSearchController: KeywordSearchController

    @MockK
    lateinit var mockDocument1: PlaceDocument

    @MockK
    lateinit var mockDocument2: PlaceDocument

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun test_search_success() {
        every {
            keywordSearchService.search("조선옥")
        } returns listOf(mockDocument1, mockDocument2)
        val result = keywordSearchController.search("조선옥")
        Assert.assertEquals(2, result.total)
    }

    @Test(expected = TelescopeRuntimeException.InvalidParameterException::class)
    fun test_search_invalidParam_throwException() {
        keywordSearchController.search(" ")
    }

    @Test
    fun test_search_trim_success() {
        every {
            keywordSearchService.search("조선옥")
        } returns listOf(mockDocument1, mockDocument2)
        val result = keywordSearchController.search("  조선옥  ")
        Assert.assertEquals(2, result.total)
    }

}