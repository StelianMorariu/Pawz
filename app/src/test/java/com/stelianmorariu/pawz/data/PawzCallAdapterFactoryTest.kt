/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.data

import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.stelianmorariu.pawz.data.network.DogApiResponseDto
import com.stelianmorariu.pawz.data.network.DogApiService
import com.stelianmorariu.pawz.domain.FakeConnectionChecker
import com.stelianmorariu.pawz.domain.errors.PawzGenericError
import com.stelianmorariu.pawz.domain.errors.PawzNoInternetError
import com.stelianmorariu.pawz.domain.errors.PawzServerError
import com.stelianmorariu.pawz.domain.retrofit.PawzCallAdapterFactory
import com.stelianmorariu.pawz.domain.scheduler.TestSchedulerProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStreamReader
import java.net.HttpURLConnection


class PawzCallAdapterFactoryTest {

    private var mockWebServer = MockWebServer()
    private var connectionChecker: FakeConnectionChecker = mock()
    private var schedulerProvider = TestSchedulerProvider()

    private lateinit var apiService: DogApiService

    @Before
    fun setup() {
        mockWebServer.start()

        // create API service with mock server URL
        createApiService(mockWebServer.url("/").toString())
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `check that factory returns breeds list`() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(readJsonFromResource(all_breeds_success_json))
        mockWebServer.enqueue(response)

        // Act
        val result = apiService.getAllBreads()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .test()

        result.assertComplete()
        result.assertNoErrors()

        result.assertValueCount(1)
        assert(result.values()[0].message.isNotEmpty())
        MatcherAssert.assertThat(
            result.values()[0].status,
            CoreMatchers.equalTo(DogApiResponseDto.STATUS_SUCCESS)
        )

    }

    @Test
    fun `check that factory returns server error`() {

        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
            .setBody(readJsonFromResource(server_error__json))


        whenever(connectionChecker.isConnected())
            .thenReturn(false)

        mockWebServer.enqueue(response)


        val result = apiService.getAllBreads()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .test()

        result.assertError(PawzServerError::class.java)

    }


    @Test
    fun `check that factory returns no internet error for ConnectException when no internet`() {

        val response = MockResponse()
            .setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)


        whenever(connectionChecker.isConnected())
            .thenReturn(false)

        mockWebServer.enqueue(response)

        val result = apiService.getAllBreads()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .test()

        result.assertError(PawzNoInternetError::class.java)

    }


    @Test
    fun `check that factory returns generic error for ConnectException when we have internet`() {

        val response = MockResponse()
            .setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)


        whenever(connectionChecker.isConnected())
            .thenReturn(true)

        mockWebServer.enqueue(response)


        val result = apiService.getAllBreads()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .test()

        result.assertError(PawzGenericError::class.java)

    }

    @Test
    fun `check that factory returns no internet error for UnknownHost when no internet`() {
        // recreate APi and point to a non-existing host
        createApiService("http://whatsyouthost.c/")

        val response = MockResponse()

        whenever(connectionChecker.isConnected())
            .thenReturn(false)

        mockWebServer.enqueue(response)

        val result = apiService.getAllBreads()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .test()

        result.assertError(PawzNoInternetError::class.java)

    }

    @Test
    fun `check that factory returns generic error for UnknownHost when we have internet`() {
        // recreate APi and point to a non-existing host
        createApiService("http://whatsyouthost.c/")

        val response = MockResponse()

        whenever(connectionChecker.isConnected())
            .thenReturn(true)

        mockWebServer.enqueue(response)


        val result = apiService.getAllBreads()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .test()

        result.assertError(PawzGenericError::class.java)

    }

    private fun createApiService(url: String) {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(
                PawzCallAdapterFactory(
                    connectionChecker,
                    schedulerProvider,
                    Gson()
                )
            )
            .build()
            .create(
                DogApiService::
                class.java
            )
    }

    private fun readJsonFromResource(path: String): String {
        var content = ""
        val reader = InputStreamReader(this.javaClass.classLoader!!.getResourceAsStream(path))
        content = reader.readText()
        reader.close()
        return content
    }

    companion object {
        private const val all_breeds_success_json = "all_breeds_success.json"
        private const val server_error__json = "server_error_response.json"
    }

}