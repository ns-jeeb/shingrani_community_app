package com.locked.shingranicommunity.event

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.EventItemBinding
import com.locked.shingranicommunity.models.EventItem

class EventListAdapter(
    private val viewModel: EventListViewModel,
    private val lifeCycleOwner: LifecycleOwner
): RecyclerView.Adapter<EventListAdapter.EventViewHolder>() {

    val list: MutableList<EventItem> = mutableListOf()

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(parent)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val itemViewModel = viewModel.getItemViewModel(position)
        itemViewModel?.let {
            holder.bind(itemViewModel)
        }
    }

    inner class EventViewHolder(val parent: ViewGroup) :
        RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent,false)),
        View.OnClickListener {

        private var binding: EventItemBinding = DataBindingUtil.bind(itemView)!!
        private var itemViewModel: EventListViewModel.ItemViewModel? = null

        fun bind(itemViewModel: EventListViewModel.ItemViewModel) {
            this.itemViewModel = itemViewModel
            itemViewModel.name.observe(lifeCycleOwner, Observer {
                binding.txtEventName.text = it
            })
            itemViewModel.desc.observe(lifeCycleOwner, Observer {
                binding.eventDescription.text = it
            })
            itemViewModel.type.observe(lifeCycleOwner, Observer {
                binding.txtEventType.text = it
            })
            itemViewModel.showAccept.observe(lifeCycleOwner, Observer {
                binding.imgAcceptedAttending.isVisible = it!!
            })
            itemViewModel.showReject.observe(lifeCycleOwner, Observer {
                binding.imgRejectedAttending.isVisible = it!!
            })
            itemViewModel.attendees.observe(lifeCycleOwner, Observer {
                binding.txtMembersAttended.text = it.toString()
            })
            itemViewModel.showDelete.observe(lifeCycleOwner, Observer {
                binding.imgDeleteItem.isVisible = it!!
            })
            itemViewModel.showDeleteConfirmation.observe(lifeCycleOwner, Observer {
                if (it) showDeleteAlert(itemViewModel)
            })
            itemViewModel.showShare.observe(lifeCycleOwner, Observer {
                binding.imgShareEvent.isVisible = it!!
            })
            itemViewModel.showMap.observe(lifeCycleOwner, Observer {
                binding.imgEventLocation.isVisible = it!!
            })
            itemViewModel.date.observe(lifeCycleOwner, Observer {
                binding.txtEventDate.text = it!!
            })
            itemViewModel.time.observe(lifeCycleOwner, Observer {
                binding.txtEventTime.text = it!!
            })
            binding.imgDeleteItem.setOnClickListener(this)
            binding.imgAcceptedAttending.setOnClickListener(this)
            binding.imgRejectedAttending.setOnClickListener(this)
            binding.imgShareEvent.setOnClickListener(this)
            binding.imgEventLocation.setOnClickListener(this)
            itemView.setOnClickListener {
                itemViewModel.openDetails()
            }
        }

        private fun showDeleteAlert(itemViewModel: EventListViewModel.ItemViewModel) {
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
                R.id.img_accepted_attending -> {
                    itemViewModel?.accept()
                }
                R.id.img_rejected_attending -> {
                    itemViewModel?.reject()
                }
                R.id.img_delete_item -> {
                    itemViewModel?.delete()
                }
                R.id.img_event_location -> {
                    itemViewModel?.openMap()
                }
            }
        }
    }
}