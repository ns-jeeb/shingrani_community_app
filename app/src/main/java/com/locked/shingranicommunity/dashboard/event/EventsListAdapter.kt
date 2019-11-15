package com.locked.shingranicommunity.dashboard.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.dashboard.data.Field
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.databinding.EventItemBinding

class EventsListAdapter(val mEvents:List<Item>?) : RecyclerView.Adapter<EventsListAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): EventViewHolder {
        return EventViewHolder(viewGroup)
    }

    override fun onBindViewHolder(announceViewHolder: EventViewHolder, i: Int) {

        mEvents?.let { announceViewHolder.bind(it.get(i),i) }
    }


    override fun getItemCount(): Int {
        if (!mEvents.isNullOrEmpty()){
            return mEvents.size
        }
        return 0
    }

    inner class EventViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.event_item,parent,false)) {

        internal var binding: EventItemBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
        }

        fun bind(event:Item,position: Int) {
            var name: String? =""
            var type: String? =""
            var address: String? =""
            var time: String? =""
            var isattend: String? =""

            for (i in event.fields?.indices!!){
                if (event.fields?.size!! > 3){
                    name = event.fields?.get(0)?.value
                    type = event.fields?.get(1)?.value
                    address = event.fields?.get(2)?.value
                    time = event.fields?.get(3)?.value
                    isattend = event.fields?.get(4)?.value
                }
            }
            binding!!.txtEventName.text = name
            binding!!.txtEventType.text = type
            binding!!.txtEventAddress.text = address
            binding!!.txtEventTime.text = time
//            binding!!.isAttend.text = isattend
        }
    }
}