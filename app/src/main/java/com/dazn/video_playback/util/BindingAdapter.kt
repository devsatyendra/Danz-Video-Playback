package com.dazn.video_playback.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.dazn.video_playback.R
import com.squareup.picasso.Picasso

@BindingAdapter("android:src")
fun setImage(imageView: ImageView, url: String) {
    Picasso.get().load(url).placeholder(R.drawable.video_placeholder_image).into(imageView)
}
