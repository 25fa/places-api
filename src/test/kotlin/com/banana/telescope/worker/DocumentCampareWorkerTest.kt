package com.banana.telescope.worker

import com.banana.telescope.model.PlaceDocument
import com.banana.telescope.worker.DocumentCompareWorker
import com.banana.telescope.worker.SimilarityWorker
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Assert
import org.junit.Test

class DocumentCampareWorkerTest {
    @InjectMockKs
    lateinit var documentCompareWorker: DocumentCompareWorker

    @MockK
    lateinit var similarityWorker: SimilarityWorker

    @MockK
    lateinit var mockDocument1: PlaceDocument

    @MockK
    lateinit var mockDocument2: PlaceDocument

    init {
        MockKAnnotations.init(this)

        every {
            mockDocument1.name
        } returns "조선옥"

        every {
            mockDocument2.name
        } returns "조선옥"

        every {
            mockDocument1.address
        } returns "서울 중구 을지로3가 228"

        every {
            mockDocument2.address
        } returns "서울 중구 을지로3가 228"

        every {
            mockDocument1.roadAddress
        } returns "서울 중구 을지로15길 8"

        every {
            mockDocument2.roadAddress
        } returns "서울 중구 을지로15길 8"

        every {
            mockDocument1.x
        } returns 123.001

        every {
            mockDocument1.y
        } returns 123.001

        every {
            mockDocument2.x
        } returns 123.001
        every {
            mockDocument2.y
        } returns 123.001
    }

    @Test
    fun test_compare_true_success() {
        every {
            similarityWorker.similarity("조선옥", "조선옥")
        } returns 0.9

        val result = documentCompareWorker.compare(mockDocument1, mockDocument2)
        Assert.assertEquals(true, result)
    }

    @Test
    fun test_compare_flase_success() {
        every {
            similarityWorker.similarity("조선옥", "조선옥")
        } returns 0.5

        val result = documentCompareWorker.compare(mockDocument1, mockDocument2)
        Assert.assertEquals(false, result)
    }
}