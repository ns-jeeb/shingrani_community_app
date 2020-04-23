package com.locked.shingranicommunity.dashboard.event

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.locked.shingranicommunity.Constant_Utils
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.databinding.EventItemBinding
import com.locked.shingranicommunity.members.User
import com.locked.shingranicommunity.utail.Utils
import kotlin.properties.Delegates

class EventsListAdapter(val mEvents:List<Item>?,val currentUser: User,val hide: Boolean,val onItemClick: OnItemClickListener) : RecyclerView.Adapter<EventsListAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): EventViewHolder {
        return EventViewHolder(viewGroup)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(announceViewHolder: EventViewHolder, i: Int) {

        mEvents?.let { announceViewHolder.bind(it[i],i) }
    }
    fun setOnInvitedEvent(onInvitedListener: OnInvitedListener){
        this.onInvitedListener = onInvitedListener
    }
    override fun getItemCount(): Int {
        if (!mEvents.isNullOrEmpty()){
            return mEvents.size
        }
        return 0
    }
    lateinit var onInvitedListener: OnInvitedListener
    private var mHideItem by Delegates.notNull<Boolean>()

    inner class EventViewHolder(val parent: ViewGroup) :
        RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.event_item,parent,false)),
        View.OnClickListener, MenuItem.OnMenuItemClickListener {

        internal var binding: EventItemBinding? = null
        lateinit var context: Context
        init {
            binding = DataBindingUtil.bind(itemView)
            context = parent.context
            mHideItem = hide
        }
        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(event:Item, position: Int) {
            var name: String? =""
            var type: String? =""
            var address: String? =""
            var time: String? =""

            for (i in event.fields?.indices!!){
                if (event.template == Constant_Utils.EVENT_TIMPLATE_ID){
                    name = event.fields?.get(0)?.value
                    address = event.fields?.get(1)?.value
                    time = event.fields?.get(2)?.value
                    type = event.fields?.get(3)?.value
                }
            }
            if(event.creator != currentUser._id){
                event.fields?.get(5)!!.value?.split(",")?.let { getAccepted(it) }
                event.fields?.get(6)!!.value?.split(",")?.let { getRejected(it) }
            }else{
                binding?.txtMembersAttended?.text = "${event.fields?.get(5)!!.value?.split(",")?.size} Guests"
            }

            binding?.txtEventName?.text = name
            binding?.txtEventType?.text = type
            binding?.txtEventAddress?.text = address
//            this substring is just display for now we have make it dynamic
            var date =  Utils.formatStringDateTime(time!!)?.split( " : ")
            binding?.txtEventDate?.text = date?.get(0)
            binding?.txtEventTime?.text = "${date?.get(1)}"
            binding?.imgHDot?.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }
        @SuppressLint("SetTextI18n")
        private fun getAccepted(accepted: List<String>){
            for (element in accepted) {
                if (!element.isBlank()){
                    if (element == currentUser._id){
                        binding?.txtMembersAttended?.text = "${accepted.size} incl you"
                        binding?.isNotAttending?.visibility = View.GONE
                        binding?.isAttending?.visibility = View.VISIBLE
                        return
                    }else {
                        binding?.txtMembersAttended?.text = "${accepted.size} Member"
                        binding?.isAttending?.visibility = View.GONE
                        binding?.isNotAttending?.visibility = View.VISIBLE
                    }
                }
            }
        }

        @SuppressLint("SetTextI18n")
        private fun getRejected(rejects: List<String>) {
            for (element in rejects) {
                if(!element.isBlank()){
                    if (element == currentUser._id) {
                        binding?.isAttending?.visibility = View.GONE
                        binding?.isNotAttending?.visibility = View.VISIBLE
                        return
                    }else{
                        binding?.isNotAttending?.visibility = View.GONE
                        binding?.isAttending?.visibility = View.VISIBLE
                    }
                }
            }
        }

        override fun onClick(v: View?) {
            var popupMenu = PopupMenu(parent.context,binding?.imgHDot)
            if (v?.id == R.id.img_h_dot){
                popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)
                var menuItem = popupMenu.menu.findItem(R.id.popup_delete)
                menuItem.isVisible = !mHideItem
                popupMenu.menu.findItem(R.id.popup_reject).setOnMenuItemClickListener(this)
                popupMenu.menu.findItem(R.id.popup_accept).setOnMenuItemClickListener(this)
                popupMenu.menu.findItem(R.id.popup_delete).setOnMenuItemClickListener(this)

            }else if (v?.id == itemView.id){
                mEvents?.get(adapterPosition)?.let { onItemClick.onItemClick(adapterPosition, it) }
            }
            popupMenu.show()
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            var id = item?.itemId
            when (id) {
                R.id.popup_accept -> {
                    mEvents?.get(adapterPosition)?.let {onInvitedListener.onAccepted(it,adapterPosition) }
                }
                R.id.popup_reject -> {
                    mEvents?.get(adapterPosition)?.let {onInvitedListener.onRejected(it) }
                }
                R.id.popup_delete -> {
                    mEvents?.get(adapterPosition)?.let {onInvitedListener.onDeleted(it,"Deleted") }
                }
            }

            return true
        }
    }
}
interface OnInvitedListener{
    fun onAccepted(eventitem: Item,position: Int)
    fun onRejected(eventitem : Item)
    fun onUpdate(eventitem : Item,update: String)
    fun onDeleted(eventitem : Item,deleted: String)
}
interface OnItemClickListener{
    fun onItemClick(position: Int,item: Item)
}