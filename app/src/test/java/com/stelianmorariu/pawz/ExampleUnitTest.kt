/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun jsonSerialize() {
        val myMap: Map<String, List<String>> = mapOf(
            "test" to listOf("a", "b"),
            "dog" to emptyList(),
            "bulldog" to listOf("french", "imaginary")
        )

        val gson = Gson()

        val jsonString: String = gson.toJson(myMap)

        assertEquals(jsonString, expectedSerialization)
    }

    private val expectedSerialization =
        "{\"test\":[\"a\",\"b\"],\"dog\":[],\"bulldog\":[\"french\",\"imaginary\"]}"
}