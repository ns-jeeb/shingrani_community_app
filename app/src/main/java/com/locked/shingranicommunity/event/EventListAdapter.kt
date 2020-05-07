package com.locked.shingranicommunity.event

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.EventItemBinding
import com.locked.shingranicommunity.models.EventItem
import com.locked.shingranicommunity.models.Field
import com.locked.shingranicommunity.models.Item
import com.locked.shingranicommunity.models.User
import com.locked.shingranicommunity.utail.Utils
import kotlin.properties.Delegates

class EventListAdapter: RecyclerView.Adapter<EventListAdapter.EventViewHolder>(),OnSetUserPermission {
    lateinit var onInvitedListener: OnInvitedListener
    val list: MutableList<EventItem> = mutableListOf()
    private var isAdminUser by Delegates.notNull<Boolean>()
    private var currentUser: User?
    init {
        isAdminUser = false
        currentUser = null
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        list.let { holder.bind(it[position],position) }
    }
    fun setOnInvitedEvent(onInvitedListener: OnInvitedListener) {
        this.onInvitedListener = onInvitedListener
    }
    inner class EventViewHolder(val parent: ViewGroup) :
            RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.event_item,parent,false)),
            View.OnClickListener, MenuItem.OnMenuItemClickListener {

        internal var binding: EventItemBinding? = null
        init {
            binding = DataBindingUtil.bind(itemView)
        }
        private fun setFields(fields: MutableList<Field>){
            for (i in fields){
                when (i.name) {
                    "Accepted" -> {
                        i.value?.split(",")?.let { getAccepted(it) }
                    }
                    "Rejected" -> {
                        i.value?.split(",")?.let { getRejected(it) }
                    }
                    "name" -> {
                        binding?.txtEventName?.text = i.value
                    }
                    "address" -> {
//                        binding?.txtEventAddress?.setImageDrawable(R.drawable.location_icon)
                    }
                    "moreDetails" -> {
                        binding?.eventDescription?.text = i.value
                    }
                    "type" -> {
                        binding?.txtEventType?.text = i.value
                    }
                    "datetime" -> {
                        var date = Utils.formatStringDateTime(i.value!!)?.split(" : ")
                        binding?.txtEventDate?.text = date?.get(0)
                        binding?.txtEventTime?.text = "${date?.get(1)}"
                    }
                }
            }
        }

        fun bind(event: Item, position: Int) {
            for (i in list.indices) {
                binding?.eventUserName?.text = currentUser?.name
                setFields(list[i].fields)
            }
            binding?.imgHDot?.setOnClickListener(this)
        }
        @SuppressLint("SetTextI18n")
        private fun getAccepted(accepted: List<String>){
            for (element in accepted) {
                if (!element.isBlank()){
                    if (element == currentUser?._id){
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
                    if (element == currentUser?._id) {
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
                popupMenu.menuInflater.inflate(R.menu.popup_menu_event,popupMenu.menu)
                var menuItem = popupMenu.menu.findItem(R.id.popup_delete)
                menuItem.isVisible = !isAdminUser
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
                    list[adapterPosition].let {onInvitedListener.onAccepted(it,adapterPosition) }
                }
                R.id.popup_reject -> {
                    list[adapterPosition].let {onInvitedListener.onRejected(it) }
                }
                R.id.popup_delete -> {
                    list[adapterPosition].let {onInvitedListener.onDeleted(it,"Deleted") }
                }
            }
            return true
        }
    }
    override fun setAdmin(isAdmin: Boolean) {
        this.isAdminUser = isAdmin
    }

    override fun setCurrentUser(currentUser: User) {
        this.currentUser = currentUser
    }
}
interface OnSetUserPermission{
    fun setAdmin(isAdmin: Boolean)
    fun setCurrentUser(currentUser: User)
}
interface OnInvitedListener{
    fun onAccepted(eventitem: Item, position: Int)
    fun onRejected(eventitem : Item)
    fun onUpdate(eventitem : Item, update: String)
    fun onDeleted(eventitem : Item, deleted: String)
}