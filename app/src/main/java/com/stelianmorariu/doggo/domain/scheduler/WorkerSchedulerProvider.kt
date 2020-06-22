/*
 * Copyright (c) Stelian Morariu 2020.
 */
package com.stelianmorariu.doggo.domain.scheduler

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class WorkerSchedulerProvider : SchedulersProvider {

    private val poolExecutor = Executors.newScheduledThreadPool(4)

    private val dbPoolExecutor = Executors.newScheduledThreadPool(4)

    override fun pool(): Scheduler = Schedulers.from(poolExecutor)

    override fun db(): Scheduler = Schedulers.from(dbPoolExecutor)

    override fun io(): Scheduler = Schedulers.io()

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()

    override fun cpu(): Scheduler = Schedulers.computation()
}
