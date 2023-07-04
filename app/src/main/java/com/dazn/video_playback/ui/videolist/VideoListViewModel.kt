package com.dazn.video_playback.ui.videolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dazn.video_playback.data.DataRepository
import com.dazn.video_playback.data.UIState
import com.dazn.video_playback.data.VideoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {
    private val _videoList = MutableLiveData<UIState<List<VideoItem>>>()
    val videoList: LiveData<UIState<List<VideoItem>>> get() = _videoList

    init {
        _videoList.value = UIState.Loading()
        viewModelScope.launch {
            dataRepository.getVideos().collect {
                _videoList.value = it
            }
        }
    }

    fun openPlayerActivity(videoItem: VideoItem) {

    }

}