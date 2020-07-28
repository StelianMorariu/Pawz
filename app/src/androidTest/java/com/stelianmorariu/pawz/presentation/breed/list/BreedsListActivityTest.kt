/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.breed.list

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import com.stelianmorariu.pawz.R
import com.stelianmorariu.pawz.TestPawzApp
import com.stelianmorariu.pawz.domain.FakeDogApiService
import com.stelianmorariu.pawz.util.EspressoIdlingResource
import com.stelianmorariu.pawz.util.RecyclerViewItemCountAssertion
import org.hamcrest.Matchers.greaterThan
import org.junit.After
import org.junit.Before
import org.junit.Test


@LargeTest
class BreedsListActivityTest {

    lateinit var fakeDogApiService: FakeDogApiService

    @Before
    fun setUp() {
        val app = ApplicationProvider.getApplicationContext<Context>() as TestPawzApp
        fakeDogApiService = app.daggerAppComponent.dogApiService() as FakeDogApiService

        fakeDogApiService.throwNoDataError = false
        fakeDogApiService.throwNoInternetError = false
        fakeDogApiService.throwServerError = false

    }

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun breeds_are_displayed_on_screen() {
        val activityScenario = ActivityScenario.launch(BreedsListActivity::class.java)


        // check that breeds are displayed on the screen
        Espresso.onView(withId(R.id.breedsRecyclerView))
            .check(ViewAssertions.matches(isDisplayed()))

        // check that we have items in the recycler view
        Espresso.onView(withId(R.id.breedsRecyclerView))
            .check(RecyclerViewItemCountAssertion(greaterThan(0)))
    }

    @Test
    fun empty_state_is_displayed_on_screen() {

        fakeDogApiService.throwNoDataError = true

        val activityScenario = ActivityScenario.launch(BreedsListActivity::class.java)

        // check that empty view state is displayed
        Espresso.onView(withId(R.id.errorLayout))
            .check(ViewAssertions.matches(isDisplayed()))

        Espresso.onView(withId(R.id.errorMessageTv))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.err_empty_state_msg)))

    }


    @Test
    fun no_internet_is_displayed_on_screen_when_no_internet() {

        fakeDogApiService.throwNoInternetError = true

        val activityScenario = ActivityScenario.launch(BreedsListActivity::class.java)

        // check that empty view state is displayed
        Espresso.onView(withId(R.id.errorLayout))
            .check(ViewAssertions.matches(isDisplayed()))

        Espresso.onView(withId(R.id.errorMessageTv))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.err_no_internet_msg)))
    }

}