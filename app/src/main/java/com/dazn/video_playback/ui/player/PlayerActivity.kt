package com.dazn.video_playback.ui.player

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.exoplayer.ExoPlayer
import com.dazn.video_playback.R
import com.dazn.video_playback.data.VideoItem
import com.dazn.video_playback.databinding.ActivityPlayerBinding
import com.dazn.video_playback.util.observe
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "PlayerActivity"

@AndroidEntryPoint
class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private val viewModel by viewModels<PlayerViewModel>()
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private val playbackStateListener: Player.Listener = playbackStateListener()
    private var player: Player? = null

    private var playWhenReady = true
    private var mediaItemIndex = 0
    private var playbackPosition = 0L
    private lateinit var videoList: List<VideoItem>

    companion object {
        val VIDEO_POSITION: String = "video_position"
        val VIDEO_LIST: String = "video_list"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player)
        firebaseAnalytics = Firebase.analytics
        viewModel.initIntentData(
            intent.getParcelableExtra(VIDEO_LIST)!!,
            intent.getIntExtra(VIDEO_POSITION, 0)
        )

        observers()
    }

    private fun observers() {
        observe(viewModel.videosList, ::handleVideoList)
        observe(viewModel.videoItemPosition, ::updateUI)
        observe(viewModel.forwardCount, ::handleForwardCountUpdate)
        observe(viewModel.backwardCount, ::handleBackwardCountUpdate)
        observe(viewModel.pauseCount, ::handlePauseCountUpdate)
    }

    private fun handleBackwardCountUpdate(count: Int) {
        binding.txtBackwardCount?.text = count.toString()
    }

    private fun handlePauseCountUpdate(count: Int) {
        binding.txtPauseCount?.text = count.toString()
    }

    private fun handleForwardCountUpdate(count: Int) {
        binding.txtForwardCount?.text = count.toString()
    }

    private fun handleVideoList(videos: List<VideoItem>) {
        this.videoList = videos
    }

    private fun updateUI(position: Int) {
        this.mediaItemIndex = position
        if (player == null && Build.VERSION.SDK_INT > 23 && ::videoList.isInitialized)
            initializePlayer()
        binding.videoItem = videoList.get(position)
    }

    public override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT > 23 && ::videoList.isInitialized)
            initializePlayer()
    }

    public override fun onResume() {
        super.onResume()
        if (::videoList.isInitialized) {
            hideSystemUi()
            if (Build.VERSION.SDK_INT <= 23 || player == null)
                initializePlayer()

        }
    }

    public override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        // ExoPlayer implements the Player interface
        player = ExoPlayer.Builder(this)
            .build()
            .also { exoPlayer ->
                binding.videoView.player = exoPlayer
                // Update the track selection parameters to only pick standard definition tracks
                exoPlayer.trackSelectionParameters = exoPlayer.trackSelectionParameters
                    .buildUpon()
                    .setMaxVideoSizeSd()
                    .build()

                val mediaItems = mutableListOf<MediaItem>()

                videoList.forEach { videoItem ->
                    val mediaItem = MediaItem.Builder()
                        .setUri(videoItem.uri)
                        .setMimeType(MimeTypes.APPLICATION_MPD)
                        .build()
                    mediaItems.add(mediaItem)
                }

                exoPlayer.setMediaItems(
                    mediaItems,
                    mediaItemIndex,
                    playbackPosition
                )
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.addListener(playbackStateListener)
                exoPlayer.prepare()
            }
    }

    private fun releasePlayer() {
        player?.let { player ->
            playbackPosition = player.currentPosition
            mediaItemIndex = player.currentMediaItemIndex
            playWhenReady = player.playWhenReady
            player.removeListener(playbackStateListener)
            player.release()
        }
        player = null
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.videoView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun playbackStateListener() = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            val stateString: String = when (playbackState) {
                ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
                ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING -"
                ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY     -"
                ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED     -"
                else -> "UNKNOWN_STATE             -"
            }
            Log.d(TAG, "changed state to $stateString")
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (!isPlaying) {
                viewModel.updatePauseCount()
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
                    param(FirebaseAnalytics.Param.ITEM_ID, 1)
                    param(FirebaseAnalytics.Param.ITEM_NAME, "Pause Event")
                    param(FirebaseAnalytics.Param.CONTENT_TYPE, "text")
                }
            }
        }

        override fun onTracksChanged(tracks: Tracks) {
            super.onTracksChanged(tracks)
            player?.currentMediaItemIndex?.let {
                if (it > mediaItemIndex) {
                    viewModel.updateForwardCount()
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
                        param(FirebaseAnalytics.Param.ITEM_ID, 2)
                        param(FirebaseAnalytics.Param.ITEM_NAME, "Forward Event")
                        param(FirebaseAnalytics.Param.CONTENT_TYPE, "text")
                    }
                } else {
                    viewModel.updateBackwardCount()
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
                        param(FirebaseAnalytics.Param.ITEM_ID, 3)
                        param(FirebaseAnalytics.Param.ITEM_NAME, "Backward Event")
                        param(FirebaseAnalytics.Param.CONTENT_TYPE, "text")
                    }
                }
            }
            player?.currentMediaItemIndex?.let { viewModel.onTracksChanged(it) }
        }
    }

}
