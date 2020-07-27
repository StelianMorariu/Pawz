/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner


class PawzTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, TestPawzApp::class.java.name, context)
    }
}
