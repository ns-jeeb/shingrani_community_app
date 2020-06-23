package com.locked.shingranicommunity.announcement

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.AnnouncementItemBinding
import com.locked.shingranicommunity.locked.models.AnnouncementItem

class AnnouncementListAdapter(
    private val viewModel: AnnouncementListViewModel,
    private val lifeCycleOwner: LifecycleOwner
): RecyclerView.Adapter<AnnouncementListAdapter.AnnouncementViewHolder>() {

    val list: MutableList<AnnouncementItem> = mutableListOf()

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        return AnnouncementViewHolder(parent)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        val itemViewModel = viewModel.getItemViewModel(position)
        itemViewModel?.let {
            holder.bind(itemViewModel)
        }
    }

    inner class AnnouncementViewHolder(val parent: ViewGroup) :
        RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.announcement_item, parent,false)),
        View.OnClickListener {

        private var binding: AnnouncementItemBinding = DataBindingUtil.bind(itemView)!!
        private var itemViewModel: AnnouncementListViewModel.ItemViewModel? = null

        fun bind(itemViewModel: AnnouncementListViewModel.ItemViewModel) {
            this.itemViewModel = itemViewModel
            itemViewModel.title.observe(lifeCycleOwner, Observer {
                binding.txtAnnouncementTitleItem.text = it
            })
            itemViewModel.detail.observe(lifeCycleOwner, Observer {
                binding.txtAnnouncementMessage.text = it
            })
            itemViewModel.date.observe(lifeCycleOwner, Observer {
                binding.txtDateItemCreated.text = it!!
            })
            itemViewModel.showDeleteConfirmation.observe(lifeCycleOwner, Observer {
                if (it) showDeleteAlert(itemViewModel)
            })
            binding.imgDeleteItem.setOnClickListener(this)
        }

        private fun showDeleteAlert(itemViewModel: AnnouncementListViewModel.ItemViewModel) {
            AlertDialog.Builder(itemView.context)
                .setTitle(itemViewModel.deleteConfirmationTitle)
                .setMessage(itemViewModel.deleteConfirmationDesc)
                .setPositiveButton(itemView.context.getString(R.string.alert_dialog_yes), object: DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        itemViewModel.delete(true)
                    }
                }).setNegativeButton(itemView.context.getString(R.string.alert_dialog_no), object: DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {}
                }).show()
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.img_delete_item -> {
                    itemViewModel?.delete()
                }
            }
        }
    }
}