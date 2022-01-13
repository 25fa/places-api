package com.banana.telescope.worker

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import org.junit.Assert
import org.junit.Test

class SimilarityWorkerTest {
    @InjectMockKs
    lateinit var similarityWorker: SimilarityWorker

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun test_compare_true_success() {
        val result = similarityWorker.similarity("조선옥", "조선옥")
        Assert.assertEquals(true, 1.0 == result)
    }

    @Test
    fun test_compare_false1_success() {
        val result = similarityWorker.similarity("조선옥", "조선판옥")
        Assert.assertEquals(false, 1.0 == result)
    }

    @Test
    fun test_compare_false2_success() {
        val result = similarityWorker.similarity("조선옥", "조선옥 본점")
        Assert.assertEquals(false, 1.0 == result)
    }
}