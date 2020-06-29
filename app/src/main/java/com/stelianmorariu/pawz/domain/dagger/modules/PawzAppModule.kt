/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.domain.dagger.modules

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.stelianmorariu.pawz.BuildConfig
import com.stelianmorariu.pawz.data.network.DogApiService
import com.stelianmorariu.pawz.domain.PawzConfig
import com.stelianmorariu.pawz.domain.retrofit.PawzCallAdapterFactory
import com.stelianmorariu.pawz.domain.retrofit.PawzConnectionChecker
import com.stelianmorariu.pawz.domain.scheduler.SchedulersProvider
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * This module is responsible for creating the dependencies the `app` module needs to provide.
 */
@Module
class PawzAppModule {


    /**
     * Provide a [Context] object across the whole app.
     */
    @Provides
    @Singleton
    fun provideContext(app: Application) = app.applicationContext


    /**
     * Provide a [PawzConfig] object across the whole app.
     */
    @Provides
    @Singleton
    fun provideConfiguration(): PawzConfig =
        PawzConfig(BuildConfig.LOGS_ENABLED, BuildConfig.DOG_API_URL)


    @Provides
    @Singleton
    fun provideDogApi(retrofit: Retrofit) = retrofit.create(DogApiService::class.java)

    @Provides
    @Singleton
    fun provideRetrofit(
        config: PawzConfig,
        client: OkHttpClient,
        callAdapterFactory: PawzCallAdapterFactory
    ) =
        Retrofit.Builder()
            .baseUrl(config.getSafeUrl())
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(callAdapterFactory)
            .build()

    @Provides
    @Singleton
    fun provideRetrofitClient(
        loggingInterceptor: HttpLoggingInterceptor
    ) =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            // add timeouts or certificate pinner
            .build()


    @Provides
    @Singleton
    fun provideRetrofitLoggingInterceptor(config: PawzConfig) =
        HttpLoggingInterceptor().apply {
            level = if (config.enableLogs) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    @Singleton
    fun provideConnectivityChecker(context: Context) = PawzConnectionChecker(context)

    @Provides
    @Singleton
    fun providePawzCallAdapterFactory(
        pawzConnectionChecker: PawzConnectionChecker,
        schedulersProvider: SchedulersProvider, gson: Gson
    ) =
        PawzCallAdapterFactory(pawzConnectionChecker, schedulersProvider, gson)

    @Provides
    @Singleton
    fun provideGson() = Gson()

}