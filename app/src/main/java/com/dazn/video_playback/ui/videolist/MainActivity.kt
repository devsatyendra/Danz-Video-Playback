package com.dazn.video_playback.ui.videolist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dazn.video_playback.R
import com.dazn.video_playback.data.UIState
import com.dazn.video_playback.data.VideoItem
import com.dazn.video_playback.data.VideoList
import com.dazn.video_playback.databinding.ActivityMainBinding
import com.dazn.video_playback.ui.player.PlayerActivity
import com.dazn.video_playback.util.SingleEvent
import com.dazn.video_playback.util.observe
import com.dazn.video_playback.util.observeEvent
import com.dazn.video_playback.util.toGone
import com.dazn.video_playback.util.toVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<VideoListViewModel>()
    private lateinit var adapter: VideoListAdapter
    private lateinit var videosList: List<VideoItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        observers()
    }

    private fun observers() {
        observe(viewModel.videoList, ::handleVideoData)
        observeEvent(viewModel.openPlayer, ::handleOpenPlayer)
    }


    fun handleOpenPlayer(singleEvent: SingleEvent<Int>) {
        singleEvent.getContentIfNotHandled()?.let { position ->
            val intent = Intent(this, PlayerActivity::class.java)
            intent.apply {
                putExtra(PlayerActivity.VIDEO_POSITION, position)
                putExtra(PlayerActivity.VIDEO_LIST, VideoList(videosList))
            }
            startActivity(intent)
        }
    }

    fun handleVideoData(uiState: UIState<List<VideoItem>>) {
        when (uiState) {
            is UIState.Loading -> binding.loaderView.toVisible()
            is UIState.DataError -> showData(false)
            is UIState.Success -> {
                uiState.data?.let { bindListData(it) }
            }
        }
    }

    private fun bindListData(videos: List<VideoItem>) {
        if (!videos.isNullOrEmpty()) {
            videosList = videos
            adapter = VideoListAdapter(viewModel, videos)
            binding.rvVideos.adapter = adapter
            showData(true)
        } else showData(false)
    }

    private fun showData(showData: Boolean) {
        binding.loaderView.toGone()
        binding.txtNoData.visibility = if (showData) View.GONE else View.VISIBLE
        binding.rvVideos.visibility = if (showData) View.VISIBLE else View.GONE
    }
}
