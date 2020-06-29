/*
 * Copyright (c) Stelian Morariu 2020.
 */


package com.stelianmorariu.pawz.domain.retrofit

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.stelianmorariu.pawz.data.network.DogApiResponseDto
import com.stelianmorariu.pawz.domain.errors.PawzError
import com.stelianmorariu.pawz.domain.errors.PawzGenericError
import com.stelianmorariu.pawz.domain.errors.PawzNoInternetError
import com.stelianmorariu.pawz.domain.errors.PawzServerError
import com.stelianmorariu.pawz.domain.scheduler.SchedulersProvider
import io.reactivex.*
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import timber.log.Timber
import java.lang.reflect.Type
import java.net.UnknownHostException

/**
 * Custom call adapter factory that intercepts errors and converts
 * them to [PawzError] before sending them upstream.
 */
class PawzCallAdapterFactory(
    private val pawzConnectionChecker: PawzConnectionChecker,
    private val schedulersProvider: SchedulersProvider,
    private val gson: Gson
) :
    CallAdapter.Factory() {

    private val rxJava2CallAdapterFactory by lazy {
        RxJava2CallAdapterFactory.createWithScheduler(schedulersProvider.io())
    }

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *> {
        val wrapped = rxJava2CallAdapterFactory.get(
            returnType,
            annotations, retrofit
        ) as CallAdapter<out Any, *>

        return RxCallAdapterWrapper(retrofit, pawzConnectionChecker, wrapped, gson)
    }

    private class RxCallAdapterWrapper<R>(
        val retrofit: Retrofit,
        val connectionChecker: PawzConnectionChecker,
        val wrappedCallAdapter: CallAdapter<R, *>,
        val gson: Gson
    ) : CallAdapter<R, Any> {

        override fun responseType(): Type = wrappedCallAdapter.responseType()

        override fun adapt(call: Call<R>): Any =
            when (val adapted = wrappedCallAdapter.adapt(call)) {
                is Observable<*> -> adapted.onErrorResumeNext { t: Throwable ->
                    Observable.error(getAsPawzRetrofitException(t))
                }
                is Single<*> -> adapted.onErrorResumeNext {
                    Single.error(getAsPawzRetrofitException(it))
                }
                is Maybe<*> -> adapted.onErrorResumeNext { t: Throwable ->
                    Maybe.error(getAsPawzRetrofitException(t))
                }
                is Completable -> adapted.onErrorResumeNext {
                    Completable.error(getAsPawzRetrofitException(it))
                }
                is Flowable<*> -> adapted.onErrorResumeNext { t: Throwable ->
                    Flowable.error(getAsPawzRetrofitException(t))
                }
                else -> adapted
            }

        private fun getAsPawzRetrofitException(throwable: Throwable): PawzError =
            when (throwable) {
                // todo : gson parsing exceptions
                is UnknownHostException -> getServerOrInternetError(throwable)
                is HttpException -> getCheckedServerException(throwable)
                else -> PawzGenericError(throwable)
            }

        private fun getCheckedServerException(throwable: HttpException): PawzError {
            return try {
                val response = throwable.response()
                val responseType = object : TypeToken<DogApiResponseDto<String>>() {}.type
                val serverResponse: DogApiResponseDto<String> =
                    gson.fromJson(response.errorBody()?.string() ?: "", responseType)
                PawzServerError(serverResponse.message, serverResponse.code, throwable)
            } catch (e: Exception) {
                Timber.e(e)
                PawzGenericError(throwable)
            }

        }

        private fun getServerOrInternetError(throwable: UnknownHostException): PawzError {
            return if (connectionChecker.isConnected()) {
                PawzGenericError(throwable)
            } else {
                PawzNoInternetError(throwable)
            }
        }


    }
}
