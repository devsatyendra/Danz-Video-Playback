package com.dazn.video_playback.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dazn.video_playback.R


@BindingAdapter("image")
fun setImage(view: ImageView, url: String) {
    Glide.with(view.context)
        .load(url)
        .placeholder(R.drawable.video_placeholder_image)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(view)
}
