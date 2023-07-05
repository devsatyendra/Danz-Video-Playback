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

    fun initIntentData(videos: VideoList, position: Int) {
        _videosList.value = videos.list
        _videoItemPosition.value = position
    }

    fun onTracksChanged(position: Int){
        _videoItemPosition.value = position
    }
}