package com.dazn.video_playback.ui.videolist

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dazn.video_playback.R
import com.dazn.video_playback.data.UIState
import com.dazn.video_playback.data.VideoItem
import com.dazn.video_playback.databinding.ActivityMainBinding
import com.dazn.video_playback.util.observe
import com.dazn.video_playback.util.toGone
import com.dazn.video_playback.util.toVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<VideoListViewModel>()
    private lateinit var adapter: VideoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        observers()
    }

    private fun observers() {
        observe(viewModel.videoList, ::handleVideoData)
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
