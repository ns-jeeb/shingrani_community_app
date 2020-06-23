package com.locked.shingranicommunity.member

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
import com.locked.shingranicommunity.databinding.MemberItemBinding
import com.locked.shingranicommunity.locked.models.Member

class MemberListAdapter(
    private val viewModel: MemberListViewModel,
    private val lifeCycleOwner: LifecycleOwner
): RecyclerView.Adapter<MemberListAdapter.MemberViewHolder>() {

    val list: MutableList<Member> = mutableListOf()

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        return MemberViewHolder(parent)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val itemViewModel = viewModel.getItemViewModel(position)
        itemViewModel?.let {
            holder.bind(itemViewModel)
        }
    }

    inner class MemberViewHolder(val parent: ViewGroup) :
        RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.member_item, parent,false)),
        View.OnClickListener {

        private var binding: MemberItemBinding = DataBindingUtil.bind(itemView)!!
        private var itemViewModel: MemberListViewModel.ItemViewModel? = null

        fun bind(itemViewModel: MemberListViewModel.ItemViewModel) {
            this.itemViewModel = itemViewModel
            // todo
            // EMAIL
            itemViewModel.title.observe(lifeCycleOwner, Observer {
                binding.title.text = it
            })
            // ADMIN
            itemViewModel.showAdmin.observe(lifeCycleOwner, Observer {
                binding.admin.isVisible = it
            })
            // PHONE
            itemViewModel.showPhone.observe(lifeCycleOwner, Observer {
                binding.phone.isVisible = it
            })
            // TEXT
            itemViewModel.showText.observe(lifeCycleOwner, Observer {
                binding.text.isVisible = it
            })
            // EMAIL
            itemViewModel.showEmail.observe(lifeCycleOwner, Observer {
                binding.email.isVisible = it
            })
            binding.email.setOnClickListener { itemViewModel.sendEmail() }
            // BLOCK
            itemViewModel.showBlock.observe(lifeCycleOwner, Observer {
                binding.block.isVisible = it
            })
            itemViewModel.showBlockConfirmation.observe(lifeCycleOwner, Observer {
                if (it) {
                    showBlockAlert(itemViewModel)
                }
            })
            binding.block.setOnClickListener { itemViewModel.block() }
        }

        private fun showBlockAlert(itemViewModel: MemberListViewModel.ItemViewModel) {
            AlertDialog.Builder(itemView.context)
                .setTitle(itemViewModel.blockConfirmationTitle)
                .setMessage(itemViewModel.blockConfirmationDesc)
                .setPositiveButton(itemView.context.getString(R.string.alert_dialog_yes), object: DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        itemViewModel.block(true)
                    }
                }).setNegativeButton(itemView.context.getString(R.string.alert_dialog_no), object: DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {}
                }).show()
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.block -> {
                    itemViewModel?.block()
                }
            }
        }
    }
}