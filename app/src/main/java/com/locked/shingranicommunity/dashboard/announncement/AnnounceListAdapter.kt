package com.locked.shingranicommunity.dashboard.announncement

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.dashboard.data.Field
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.dashboard.event.OnInvitedListener
import com.locked.shingranicommunity.databinding.AnnouncementItemBinding

class AnnounceListAdapter ( val mAnnouncements: List<Item>?) : RecyclerView.Adapter<AnnounceListAdapter.AnnounceViewHolder>() {

    lateinit var onInvitedListener: OnInvitedListener
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): AnnounceViewHolder {
        return AnnounceViewHolder(viewGroup)
    }
    fun setOnInvitedEvent(onInvitedListener: OnInvitedListener){
        this.onInvitedListener = onInvitedListener
    }
    override fun onBindViewHolder(announceViewHolder: AnnounceViewHolder, i: Int) {

        mAnnouncements?.get(i)?.let { announceViewHolder.bind(it, i) }
    }
    override fun getItemCount(): Int {
        return mAnnouncements?.size!!
    }

    inner class AnnounceViewHolder(var parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.announcement_item, parent, false)) ,
        View.OnClickListener, MenuItem.OnMenuItemClickListener {

        internal var binding: AnnouncementItemBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
        }

        fun bind(announcement: Item, i: Int) {
            var fields: List<Field>? = announcement.fields
            var title: String? = ""
            var text: String? = ""
            var timeStamp: String? = ""

            if (fields != null) {
                title = announcement.fields?.get(0)?.value
                text = announcement.fields?.get(1)?.value
                timeStamp = announcement.createdAt
            }
            binding?.txtAnnouncementTitleItem?.text = title
            binding?.txtAnnouncementMessage?.text = text
            binding?.txtDateItemCreated?.text = timeStamp
            binding?.imgHDot?.setOnClickListener(this)

        }
        override fun onClick(v: View?) {
            var popupMenu = PopupMenu(parent.context,binding?.imgHDot)
            if (v?.id == R.id.img_h_dot){
                popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)
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
                    mAnnouncements?.get(adapterPosition)?.let {onInvitedListener.onAccepted(it,"Accepted") }
                }
                R.id.popup_reject -> {
                    mAnnouncements?.get(adapterPosition)?.let {onInvitedListener.onRejected(it,"Rejected") }
                }
                R.id.popup_delete -> {
                    mAnnouncements?.get(adapterPosition)?.let {onInvitedListener.onDeleted(it,"Deleted") }
                }
            }

            return true
        }

    }
}