package com.dazn.video_playback.data

import kotlinx.coroutines.flow.Flow

interface DataRepositorySource {
    suspend fun getVideos(): Flow<UIState<List<VideoItem>>>
}