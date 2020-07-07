/*
 * Copyright (c) Stelian Morariu 2020.
 */
package com.stelianmorariu.pawz.domain.scheduler

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class TestSchedulerProvider : SchedulersProvider {

    override fun pool(): Scheduler = Schedulers.trampoline()

    override fun db(): Scheduler = Schedulers.trampoline()

    override fun io(): Scheduler = Schedulers.trampoline()

    override fun ui(): Scheduler = Schedulers.trampoline()

    override fun cpu(): Scheduler = Schedulers.trampoline()
}
