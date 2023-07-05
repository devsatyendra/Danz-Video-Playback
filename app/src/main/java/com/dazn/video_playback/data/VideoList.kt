package com.dazn.video_playback.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoList(val list: List<VideoItem>) : Parcelable

@Parcelize
data class VideoItem(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("uri")
    val uri: String
) : Parcelable

object VideoData {
    var videos = listOf(
        VideoItem(
            "HD (MP4, H264)",
            "https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd"
        ),
        VideoItem(
            "UHD (MP4, H264)",
            "https://storage.googleapis.com/wvmedia/clear/h264/tears/tears_uhd.mpd"
        ),
        VideoItem(
            "HD (MP4, H265)",
            "https://storage.googleapis.com/wvmedia/clear/hevc/tears/tears.mpd"
        ),
        VideoItem(
            "UHD (MP4, H265)",
            "https://storage.googleapis.com/wvmedia/clear/hevc/tears/tears_uhd.mpd"
        ),
        VideoItem(
            "HD (WebM, VP9)",
            "https://storage.googleapis.com/wvmedia/clear/vp9/tears/tears.mpd"
        ),
        VideoItem(
            "UHD (WebM, VP9)",
            "https://storage.googleapis.com/wvmedia/clear/vp9/tears/tears_uhd.mpd"
        ),
    )
}
