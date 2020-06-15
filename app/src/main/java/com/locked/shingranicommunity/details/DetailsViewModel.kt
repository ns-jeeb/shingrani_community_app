package com.locked.shingranicommunity.details

import androidx.lifecycle.ViewModel
import com.locked.shingranicommunity.models.Item
import javax.inject.Inject

class DetailsViewModel@Inject constructor() : ViewModel() {
    private var item: Item? = null
    fun setItem(item: Item){
        this.item = item
    }
    fun getItem(): Item? {
        return this.item
    }
}
