package com.dazn.video_playback.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DataRepository @Inject constructor(private val ioDispatcher: CoroutineContext) : DataRepositorySource {
    override suspend fun getVideos(): Flow<UIState<List<VideoItem>>> {

        return flow<UIState<List<VideoItem>>> {
            emit(UIState.Success(VideoData.videos))
        }.flowOn(ioDispatcher)

    }
}