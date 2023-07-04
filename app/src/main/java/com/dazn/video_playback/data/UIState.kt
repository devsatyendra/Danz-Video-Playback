package com.dazn.video_playback.data

// A generic class that contains data and status about loading this data.
sealed class UIState<T>(
    val data: T? = null,
    var errorCode: Int? = null
) {
    class Success<T>(data: T) : UIState<T>(data)
    class Loading<T>(data: T? = null) : UIState<T>(data)
    class DataError<T>(errorCode: Int?) : UIState<T>(null, errorCode)

    override fun toString(): String {
        return when (this) {
            is DataError -> "Error[exception=$errorCode]"
            is Loading -> "Loading"
            is Success -> "Success[data=$data]"
        }
    }
}