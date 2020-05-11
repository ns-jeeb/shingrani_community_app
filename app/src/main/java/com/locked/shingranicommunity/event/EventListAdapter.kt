package com.locked.shingranicommunity.event

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.EventItemBinding
import com.locked.shingranicommunity.models.EventItem
import com.locked.shingranicommunity.utail.Utils
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class EventListAdapter@Inject constructor(val viewModel: EventListViewModel): RecyclerView.Adapter<EventListAdapter.EventViewHolder>() {
    val list: MutableList<EventItem> = mutableListOf()
    private var address:String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        viewModel.getItemViewModel(position)
        list.let { holder.bind(it[position]) }
    }
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    inner class EventViewHolder(val parent: ViewGroup) :
            RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.event_item,parent,false)),
            View.OnClickListener {

        internal var binding: EventItemBinding? = null
        init {
            binding = DataBindingUtil.bind(itemView)
        }
        private fun setFields(eventItem:  EventItem){
            Log.d("eventItem",eventItem.fields[adapterPosition].name)
            for (i in 0 until eventItem.fields.size){
                when (eventItem.fields[i].name) {
                    "Accepted" -> {
                        eventItem.accepted?.split(",")?.let { getAccepted(it) }
                    }
                    "Rejected" -> {
                        eventItem.rejected?.split(",")?.let { getRejected(it) }
                    }
                    "Name" -> {
                        binding?.txtEventName?.text = viewModel.getItemViewModel(adapterPosition)?.name?.value
                    }
                    "Address" -> {
                        address = eventItem.address
                    }
                    "Detail" -> {
                        binding?.eventDescription?.text = viewModel.getItemViewModel(adapterPosition)?.desc?.value
                    }
                    "Type" -> {
                        binding?.txtEventType?.text = eventItem.type
                    }
                    "Time" -> {
                        var date = Utils.formatStringDateTime(eventItem.time!!)?.split(" : ")
                        binding?.txtEventDate?.text = viewModel.getItemViewModel(adapterPosition)?.date?.value
                        binding?.txtEventTime?.text = viewModel.getItemViewModel(adapterPosition)?.time?.value
                    }
                }
            }

        }

        fun bind(eventItem: EventItem) {
            setFields(eventItem)
            binding?.imgDeleteItem?.setOnClickListener(this)
            binding?.imgAcceptedAttending?.setOnClickListener(this)
            binding?.imgRejectedAttending?.setOnClickListener(this)
            binding?.imgShareEvent?.setOnClickListener(this)
            binding?.imgEventLocation?.setOnClickListener(this)
        }
        private fun getAccepted(accepted: List<String>){
            for (element in accepted) {
                if (!element.isBlank()){
                    if (element == viewModel.currentUser()?._id){
                        binding?.txtMembersAttended?.text = "${accepted.size} incl you"
                        binding?.imgAcceptedAttending?.visibility = View.GONE
                        binding?.imgRejectedAttending?.visibility = View.VISIBLE
                    }else {
                        binding?.txtMembersAttended?.text = "${accepted.size} Member"
                    }
                }
            }
        }


        private fun getRejected(rejects: List<String>) {
            for (element in rejects) {
                if(!element.isBlank()){
                    if (element == viewModel.currentUser()?._id) {
                        binding?.imgAcceptedAttending?.visibility = View.VISIBLE
                        binding?.imgRejectedAttending?.visibility = View.GONE
                    }
                }
            }
        }
        override fun onClick(v: View?) {
            when (v?.id){
                R.id.img_accepted_attending ->{
                    list[adapterPosition].let {
                        viewModel.getItemViewModel(position)?.accept()
                    }
                }
                R.id.img_rejected_attending ->{
                    list[adapterPosition].let {
                        viewModel.getItemViewModel(position)?.reject()}
                }
                R.id.img_delete_item -> {
                    list[adapterPosition].let {
                        viewModel.getItemViewModel(position)?.delete()}
                }
                R.id.img_event_location ->{
                    list[adapterPosition].let {
                        viewModel.getItemViewModel(position)?.openMap(address!!)}
                }
            }
        }
    }
}