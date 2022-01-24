package com.banana.telescope.service.search

import com.banana.telescope.service.search.worker.DocumentCompareWorker
import org.junit.Assert
import org.junit.Test

class DocumentCompareWorkerTest {
    private val documentCompareWorker = DocumentCompareWorker()

    @Test
    fun getTown_rightParameters_success() {
        val method = DocumentCompareWorker::class.java.getDeclaredMethod("getTown", String::class.java)
        method.isAccessible = true

        val result = method.invoke(documentCompareWorker, "세종특별자치시  연기면 세종리 1201").toString()

        Assert.assertEquals("세종리 1201", result)
    }

    @Test
    fun getRoad_rightParameters_success() {
        val method = DocumentCompareWorker::class.java.getDeclaredMethod("getRoad", String::class.java)
        method.isAccessible = true

        val result = method.invoke(documentCompareWorker, "세종특별자치시  연기면 호수공원길 155").toString()

        Assert.assertEquals("호수공원길 155", result)
    }

    @Test
    fun compareAddress_rightParameters_success() {
        val method = DocumentCompareWorker::class.java.getDeclaredMethod(
            "compareAddress",
            String::class.java,
            String::class.java
        )
        method.isAccessible = true

        val result: Boolean =
            method.invoke(documentCompareWorker, "세종특별자치시  연기면 세종리 1201", "세종특별자치시  연기면 세종리 1201") as Boolean

        Assert.assertEquals(true, result)
    }

    @Test
    fun compareRoadAddress_rightParameters_success() {
        val method = DocumentCompareWorker::class.java.getDeclaredMethod(
            "compareRoadAddress",
            String::class.java,
            String::class.java
        )
        method.isAccessible = true

        val result: Boolean =
            method.invoke(documentCompareWorker, "세종특별자치시  연기면 호수공원길 155", "세종특별자치시  연기면 호수공원길 155") as Boolean

        Assert.assertEquals(true, result)
    }

    @Test
    fun compareName_rightParameters_success() {
        val method = DocumentCompareWorker::class.java.getDeclaredMethod(
            "compareName",
            String::class.java,
            String::class.java
        )
        method.isAccessible = true

        val result: Boolean =
            method.invoke(documentCompareWorker, "조선옥", "조선옥") as Boolean

        Assert.assertEquals(true, result)
    }

    @Test
    fun comparePosition_rightParameters_success() {
        val method = DocumentCompareWorker::class.java.getDeclaredMethod(
            "comparePosition",
            Double::class.java,
            Double::class.java,
            Double::class.java,
            Double::class.java
        )
        method.isAccessible = true

        val result: Boolean =
            method.invoke(documentCompareWorker, 127.0012, 127.0013, 127.0011, 127.0012) as Boolean

        Assert.assertEquals(true, result)
    }
}