package com.locked.shingranicommunity.event

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.locked.shingranicommunity.models.EventItem

class EventListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val list: MutableList<EventItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}