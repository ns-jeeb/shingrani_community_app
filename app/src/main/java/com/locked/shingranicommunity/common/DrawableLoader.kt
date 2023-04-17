package com.locked.shingranicommunity.common

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

class DrawableLoader(@DrawableRes val drawableId: Int): ImageLoader {
    override fun load(view: ImageView) {
        Glide.with(view)
            .load(drawableId)
            .into(view);
    }
}