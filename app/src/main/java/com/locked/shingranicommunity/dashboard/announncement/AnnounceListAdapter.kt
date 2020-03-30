package com.locked.shingranicommunity.dashboard.announncement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.dashboard.data.Field
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.databinding.AnnouncementItemBinding

class AnnounceListAdapter ( val mAnnouncements: List<Item>?) : RecyclerView.Adapter<AnnounceListAdapter.AnnounceViewHolder>() {



        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): AnnounceViewHolder {
            return AnnounceViewHolder(viewGroup)
        }

        override fun onBindViewHolder(announceViewHolder: AnnounceViewHolder, i: Int) {

            mAnnouncements?.get(i)?.let { announceViewHolder.bind(it,i) }
        }


        override fun getItemCount(): Int {
            return mAnnouncements?.size!!
        }

        inner class AnnounceViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.announcement_item, parent, false)) {

            internal var binding: AnnouncementItemBinding? = null

            init {
                binding = DataBindingUtil.bind(itemView)
            }

            fun bind(announcement: Item,i: Int) {
                var  fields: List<Field>? = announcement.fields
                var title: String? = ""
                var text: String? = ""
                var timeStamp: String? = ""

                if (fields != null) {
                    title = announcement.fields?.get(0)?.value
                    text = announcement.fields?.get(1)?.value
                    timeStamp = announcement.createdAt
                }
                binding!!.txtAnnouncementTitleItem.text = title
                binding!!.txtAnnouncementMessage.text = text
                binding!!.txtDateItemCreated.text = timeStamp

            }
        }
}