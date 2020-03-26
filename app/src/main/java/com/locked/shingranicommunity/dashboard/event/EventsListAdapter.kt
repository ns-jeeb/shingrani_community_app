package com.locked.shingranicommunity.dashboard.event

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.dashboard.event.fetch_event.OnAttendListener
import com.locked.shingranicommunity.databinding.EventItemBinding
import com.locked.shingranicommunity.members.ShingraniMember
import com.locked.shingranicommunity.members.User

class EventsListAdapter(val mEvents:List<Item>?,val currentUser: User,var onAttendListener : OnAttendListener) : RecyclerView.Adapter<EventsListAdapter.EventViewHolder>() {

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

    inner class EventViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.event_item,parent,false)),
        CompoundButton.OnCheckedChangeListener {

        internal var binding: EventItemBinding? = null
        lateinit var context: Context
        init {
            binding = DataBindingUtil.bind(itemView)
            context = parent.context
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
            binding?.isAttend?.setOnCheckedChangeListener(this)
            binding!!.txtEventName.text = name
            binding!!.txtEventType.text = type
            binding!!.txtEventAddress.text = address
            binding!!.txtEventTime.text = time
//            binding!!.isAttend.text = isattend
        }


        override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
            if (p1){
                mEvents?.get(adapterPosition)?.let { onAttendListener.onAttendingEvent(it) }
            }
        }
        fun updateEvent(currentUserId: String): String{
            return currentUser._id
        }
    }
}