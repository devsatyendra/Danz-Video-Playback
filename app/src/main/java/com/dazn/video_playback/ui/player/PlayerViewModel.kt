package com.dazn.video_playback.ui.player

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dazn.video_playback.data.VideoItem
import com.dazn.video_playback.data.VideoList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor() : ViewModel() {

    private val _videoItemPosition = MutableLiveData<Int>()
    val videoItemPosition: LiveData<Int> get() = _videoItemPosition

    private val _videosList = MutableLiveData<List<VideoItem>>()
    val videosList: LiveData<List<VideoItem>> get() = _videosList

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val _forwardCount = MutableLiveData<Int>(0)
    val forwardCount: LiveData<Int> get() = _forwardCount

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
     val _backwardCount = MutableLiveData<Int>(0)
    val backwardCount: LiveData<Int> get() = _backwardCount

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
     val _pauseCount = MutableLiveData<Int>(0)
    val pauseCount: LiveData<Int> get() = _pauseCount


    fun initIntentData(videos: VideoList, position: Int) {
        _videosList.value = videos.list
        _videoItemPosition.value = position
    }

    fun onTracksChanged(position: Int) {
        _videoItemPosition.value = position
    }

    fun updateForwardCount() {
        _forwardCount.value = _forwardCount.value as Int + 1
    }

    fun updateBackwardCount() {
        _backwardCount.value = _backwardCount.value as Int + 1
    }

    fun updatePauseCount() {
        _pauseCount.value = _pauseCount.value as Int + 1
    }
}