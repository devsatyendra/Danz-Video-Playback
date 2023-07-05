package com.dazn.video_playback.ui.player

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dazn.video_playback.data.VideoData
import com.dazn.video_playback.data.VideoList
import com.util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PlayerViewModelTest {
    //subject under test
    private lateinit var viewModel: PlayerViewModel

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    //Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = PlayerViewModel()
    }

    @Test
    fun `test initIntentData`() {
        //get data
        val videoList = VideoList(VideoData.videos)
        val position = 0

        //call
        viewModel.initIntentData(videoList, position)
        viewModel.videosList.observeForever { }
        viewModel.videoItemPosition.observeForever { }

        //verify
        val data = viewModel.videosList.value
        val getPosition = viewModel.videoItemPosition.value
        assertEquals(videoList.list, data)
        assertEquals(position, getPosition)
    }

    @Test
    fun `test onTracksChanged`() {
        val position = 3

        //call
        viewModel.onTracksChanged(position)
        viewModel.videoItemPosition.observeForever { }

        //verify
        val getPosition = viewModel.videoItemPosition.value
        assertEquals(position, getPosition)
    }

    @Test
    fun `test BackwardCount`() {
        val initialCount = 1
        viewModel._backwardCount.value = initialCount

        //call
        viewModel.updateBackwardCount()
        viewModel.backwardCount.observeForever { }

        //verify
        val count = viewModel.backwardCount.value
        assertEquals(initialCount + 1, count)
    }

    @Test
    fun `test ForwardCount`() {
        val initialCount = 1
        viewModel._forwardCount.value = initialCount

        //call
        viewModel.updateForwardCount()
        viewModel.forwardCount.observeForever { }

        //verify
        val count = viewModel.forwardCount.value
        assertEquals(initialCount + 1, count)
    }

    @Test
    fun `test PauseCount`() {
        val initialCount = 1
        viewModel._pauseCount.value = initialCount

        //call
        viewModel.updatePauseCount()
        viewModel.pauseCount.observeForever { }

        //verify
        val count = viewModel.pauseCount.value
        assertEquals(initialCount + 1, count)
    }
}