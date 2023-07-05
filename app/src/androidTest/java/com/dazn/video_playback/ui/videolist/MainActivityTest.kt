package com.dazn.video_playback.ui.videolist

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.dazn.video_playback.R
import com.dazn.video_playback.util.EspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val mIdlingResource: IdlingResource? = null

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        if (mIdlingResource != null)
            IdlingRegistry.getInstance().unregister()
    }

    @Test
    fun testDisplayVideoList() {
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.rv_videos)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.loader_view)).check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
        onView(withId(R.id.txt_no_data)).check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
    }

    @Test
    fun testItemClick() {
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.rv_videos)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        onView(withId(R.id.player_root)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}