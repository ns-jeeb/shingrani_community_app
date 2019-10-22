package com.locked.shingranicommunity.dashboard.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.dashboard.announncement.Announcement
import com.locked.shingranicommunity.databinding.EventItemBinding

class EventsListAdapter (val mEvents: MutableLiveData<List<Announcement>>?) : RecyclerView.Adapter<EventsListAdapter.EventViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): EventViewHolder {
        return EventViewHolder(viewGroup)
    }

    override fun onBindViewHolder(announceViewHolder: EventViewHolder, i: Int) {

        mEvents?.value?.get(i)?.let { announceViewHolder.bind(it) }
    }


    override fun getItemCount(): Int {
        return mEvents?.value?.size!!
    }

    inner class EventViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.event_item,parent,false)) {

        internal var binding: EventItemBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
        }

        fun bind(event: Announcement) {
            binding!!.txtEventName.text = "Wedding"
            binding!!.txtEventDate.text = "July 22"
            binding!!.txtEventTime.text = "6PM"
            binding!!.txtEventType.text = "Event"
            binding!!.txtEventAddress.text = "50 Esterbrooke Ave"
        }
    }
}