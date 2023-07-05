package com.dazn.video_playback.ui.videolist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dazn.video_playback.data.DataRepository
import com.dazn.video_playback.data.UIState
import com.dazn.video_playback.data.VideoData
import com.dazn.video_playback.data.VideoItem
import com.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class VideoListViewModelTest {
    //subject under test
    private lateinit var viewModel: VideoListViewModel

    //fake repository
    private val dataRepository: DataRepository = mockk()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    //Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Test
    fun `get video list`() {
        //let's do an answer for livedata
        val videoList = VideoData.videos

        //1-mock call
        coEvery { dataRepository.getVideos() } returns flow {
            emit(UIState.Success(videoList))
        }

        //2- call
        viewModel = VideoListViewModel(dataRepository)
        viewModel.videoList.observeForever { }

        //3-verify
        assertEquals(videoList, viewModel.videoList.value?.data)
    }

    @Test
    fun `test empty list`() {
        //let's do an answer for livedata
        val videoList = listOf<VideoItem>()

        //1- mock call
        coEvery { dataRepository.getVideos() } returns flow {
            emit(UIState.Success(videoList))
        }

        //2-call
        viewModel = VideoListViewModel(dataRepository)
        viewModel.videoList.observeForever { }

        //3- verify
        val isEmptyList = viewModel.videoList.value?.data.isNullOrEmpty()
        assertEquals(true, isEmptyList)
    }
}