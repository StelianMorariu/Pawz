/*
 * Copyright (c) Stelian Morariu 2020.
 */

package com.stelianmorariu.pawz.presentation.breed.gallery

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.stelianmorariu.pawz.R
import com.stelianmorariu.pawz.TestPawzApp
import com.stelianmorariu.pawz.domain.FakeDogApiService
import com.stelianmorariu.pawz.domain.model.DogBreed
import com.stelianmorariu.pawz.util.EspressoIdlingResource
import com.stelianmorariu.pawz.util.RecyclerViewItemCountAssertion
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@MediumTest
class BreedGalleryActivityTest {

    lateinit var fakeDogApiService: FakeDogApiService

    private val dogBreed = DogBreed("akita", breed = "akita")

    @Before
    fun setUp() {
        val app = getApplicationContext<Context>() as TestPawzApp
        fakeDogApiService = app.daggerAppComponent.dogApiService() as FakeDogApiService

        fakeDogApiService.throwNoDataError = false
        fakeDogApiService.throwNoInternetError = false
        fakeDogApiService.throwServerError = false
        fakeDogApiService.throwGenericError = false

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
    fun check_that_activity_finishes_without_dog_breed() {
        val activityScenario = ActivityScenario.launch(BreedGalleryActivity::class.java)

        assertEquals(activityScenario.state, Lifecycle.State.DESTROYED)
    }

    @Test
    fun breed_images_are_displayed_on_screen() {
        val intent = Intent(getApplicationContext(), BreedGalleryActivity::class.java)
            .putExtra(BreedGalleryActivity.BREED, dogBreed)
        val activityScenario = ActivityScenario.launch<BreedGalleryActivity>(intent)

        // check that breeds are displayed on the screen
        Espresso.onView(ViewMatchers.withId(R.id.breedImageRecyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // check that we have items in the recycler view
        Espresso.onView(ViewMatchers.withId(R.id.breedImageRecyclerView))
            .check(RecyclerViewItemCountAssertion(Matchers.greaterThan(0)))
    }

    @Test
    fun empty_state_is_displayed_on_screen() {
        fakeDogApiService.throwNoDataError = true

        val intent = Intent(getApplicationContext(), BreedGalleryActivity::class.java)
            .putExtra(BreedGalleryActivity.BREED, dogBreed)
        val activityScenario = ActivityScenario.launch<BreedGalleryActivity>(intent)

        // check that empty view state is displayed
        Espresso.onView(ViewMatchers.withId(R.id.errorLayout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.errorMessageTv))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.err_empty_state_msg)))

    }


    @Test
    fun no_internet_is_displayed_on_screen_when_no_internet() {
        fakeDogApiService.throwNoInternetError = true

        val intent = Intent(getApplicationContext(), BreedGalleryActivity::class.java)
            .putExtra(BreedGalleryActivity.BREED, dogBreed)
        val activityScenario = ActivityScenario.launch<BreedGalleryActivity>(intent)

        // check that empty view state is displayed
        Espresso.onView(ViewMatchers.withId(R.id.errorLayout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.errorMessageTv))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.err_no_internet_msg)))
    }


    @Test
    fun server_error_is_displayed_on_screen() {
        fakeDogApiService.throwServerError = true

        val intent = Intent(getApplicationContext(), BreedGalleryActivity::class.java)
            .putExtra(BreedGalleryActivity.BREED, dogBreed)
        val activityScenario = ActivityScenario.launch<BreedGalleryActivity>(intent)

        // check that empty view state is displayed
        Espresso.onView(ViewMatchers.withId(R.id.errorLayout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // server error message is "Server error"
        Espresso.onView(ViewMatchers.withId(R.id.errorMessageTv))
            .check(ViewAssertions.matches(ViewMatchers.withText("Server error")))
    }

    @Test
    fun generic_error_is_displayed_on_screen() {
        fakeDogApiService.throwGenericError = true

        val intent = Intent(getApplicationContext(), BreedGalleryActivity::class.java)
            .putExtra(BreedGalleryActivity.BREED, dogBreed)
        val activityScenario = ActivityScenario.launch<BreedGalleryActivity>(intent)

        // check that empty view state is displayed
        Espresso.onView(ViewMatchers.withId(R.id.errorLayout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // server error message is "Server error"
        Espresso.onView(ViewMatchers.withId(R.id.errorMessageTv))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.err_default_msg)))
    }
}