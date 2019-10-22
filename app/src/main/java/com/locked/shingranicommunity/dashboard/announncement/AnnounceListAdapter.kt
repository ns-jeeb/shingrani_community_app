package com.locked.shingranicommunity.dashboard.announncement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.AnnouncementItemBinding

class AnnounceListAdapter ( val mAnnouncements: MutableLiveData<List<Announcement>>?) : RecyclerView.Adapter<AnnounceListAdapter.AnnounceViewHolder>() {



        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): AnnounceViewHolder {
            return AnnounceViewHolder(viewGroup)
        }

        override fun onBindViewHolder(announceViewHolder: AnnounceViewHolder, i: Int) {

            mAnnouncements?.value?.get(i)?.let { announceViewHolder.bind(it) }
        }


        override fun getItemCount(): Int {
            return mAnnouncements?.value?.size!!
        }

        inner class AnnounceViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.announcement_item, parent, false)) {

            internal var binding: AnnouncementItemBinding? = null

            init {
                binding = DataBindingUtil.bind(itemView)
            }

            fun bind(announcement: Announcement) {
                binding!!.txtAnnouncementMessage.text = announcement.type
                binding!!.txtAnnouncementTitleItem.text = announcement.name
            }
        }
}