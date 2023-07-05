package com.dazn.video_playback.ui.videolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dazn.video_playback.data.VideoItem
import com.dazn.video_playback.databinding.ListItemVideoBinding

class VideoListAdapter(
    val viewModel: VideoListViewModel,
    val videos: List<VideoItem>
) :
    RecyclerView.Adapter<VideoViewHolder>() {

    private val onItemClickListener: RecyclerItemListener = object : RecyclerItemListener {
        override fun onItemClickListener(position: Int) {
            viewModel.openPlayerActivity(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val itemBinding =
            ListItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return videos.size ?: 0
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(onItemClickListener, videos.get(position), position)
    }

}

class VideoViewHolder(val binding: ListItemVideoBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(recyclerItemListener: RecyclerItemListener, videoItem: VideoItem, position: Int) {
        binding.videoItem = videoItem
        binding.root.setOnClickListener {
            recyclerItemListener.onItemClickListener(position)
        }
    }

}

interface RecyclerItemListener {
    fun onItemClickListener(position: Int)
}