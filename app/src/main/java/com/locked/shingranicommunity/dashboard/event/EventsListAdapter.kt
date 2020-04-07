package com.locked.shingranicommunity.dashboard.event

import android.content.Context
import android.view.*
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.databinding.EventItemBinding
import com.locked.shingranicommunity.members.User
import kotlin.properties.Delegates

class EventsListAdapter(val mEvents:List<Item>?,val currentUser: User,val hide: Boolean) : RecyclerView.Adapter<EventsListAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): EventViewHolder {
        return EventViewHolder(viewGroup)
    }

    override fun onBindViewHolder(announceViewHolder: EventViewHolder, i: Int) {

        mEvents?.let { announceViewHolder.bind(it.get(i),i) }
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

        fun bind(event:Item,position: Int) {
            var name: String? =""
            var type: String? =""
            var address: String? =""
            var time: String? =""
            var isattend: String? =""

            for (i in event.fields?.indices!!){
                if (event.fields?.size!! > 3){
                    name = event.fields?.get(0)?.value
                    address = event.fields?.get(1)?.value
                    time = event.fields?.get(2)?.value
                    type = event.fields?.get(3)?.value
                    isattend = event.fields?.get(4)?.value
                    if(event.fields?.get(i)!!.name == "Accepted" && event.fields?.get(i)!!.value == currentUser._id){
                        binding?.isAttend?.isChecked = true
                    }
                }
            }

            binding!!.txtEventName.text = name
            binding!!.txtEventType.text = type
            binding!!.txtEventAddress.text = address
//            this substring is just display for now we have make it dynamic
            var date = time?.split(" ")
            binding!!.txtEventDate.text = date?.get(0)
            binding!!.txtEventTime.text = date?.get(2)
//            binding!!.isAttend.text = isattend
            binding!!.imgHDot.setOnClickListener(this)
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

            }
            popupMenu.show()
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            var id = item?.itemId
            when (id) {
                R.id.popup_accept -> {
                    mEvents?.get(adapterPosition)?.let {onInvitedListener.onAccepted(it,"Accepted") }
                }
                R.id.popup_reject -> {
                    mEvents?.get(adapterPosition)?.let {onInvitedListener.onRejected(it,"Rejected") }
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
    fun onAccepted(eventitem: Item,accepted: String)
    fun onRejected(eventitem : Item,rejected: String)
    fun onDeleted(eventitem : Item,deleted: String)
}