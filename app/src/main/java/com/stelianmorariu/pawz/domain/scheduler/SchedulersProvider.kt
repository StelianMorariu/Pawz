/*
 * Copyright (c) Stelian Morariu 2020.
 */
package com.stelianmorariu.pawz.domain.scheduler

import io.reactivex.Scheduler

interface SchedulersProvider {

    fun io(): Scheduler

    fun ui(): Scheduler

    fun cpu(): Scheduler

    fun db(): Scheduler

    fun pool(): Scheduler
}
