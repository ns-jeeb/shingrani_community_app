package com.locked.shingranicommunity.common

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

class DrawableLoader(@DrawableRes val drawableId: Int): ImageLoader {
    override fun load(view: ImageView) {
        view.setImageDrawable(ContextCompat.getDrawable(view.context, drawableId))
    }
}