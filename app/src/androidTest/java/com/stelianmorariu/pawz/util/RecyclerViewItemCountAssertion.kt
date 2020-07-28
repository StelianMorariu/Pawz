/*
 * Copyright (c) Stelian Morariu 2020.
 */
package com.stelianmorariu.pawz.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers

class RecyclerViewItemCountAssertion : ViewAssertion {
    private val matcher: Matcher<Int>

    constructor(expectedCount: Int) {
        matcher = Matchers.`is`(expectedCount)
    }

    constructor(matcher: Matcher<Int>) {
        this.matcher = matcher
    }

    override fun check(
        view: View,
        noViewFoundException: NoMatchingViewException?
    ) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        ViewMatchers.assertThat(adapter!!.itemCount, matcher)
    }
}