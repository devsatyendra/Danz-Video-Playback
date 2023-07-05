package com.dazn.video_playback.ui.player

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.dazn.video_playback.R
import com.dazn.video_playback.data.VideoData
import com.dazn.video_playback.data.VideoItem
import com.dazn.video_playback.data.VideoList
import com.dazn.video_playback.util.EspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class PlayerActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private lateinit var videosList: List<VideoItem>
    val position = 0

    @Before
    fun setUp() {

        videosList = VideoData.videos

        val intent: Intent =
            Intent(ApplicationProvider.getApplicationContext(), PlayerActivity::class.java)
                .apply {
                    putExtra(PlayerActivity.VIDEO_POSITION, position)
                    putExtra(PlayerActivity.VIDEO_LIST, VideoList(videosList))
                }

        ActivityScenario.launch<PlayerActivity>(intent)

    }

    @Test
    fun testVideoDetails() {
        onView(withId(R.id.player_root)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}