package com.locked.shingranicommunity.member

import android.util.Log
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
import com.locked.shingranicommunity.databinding.ItemMemberBinding
import com.locked.shingranicommunity.locked.models.Member
import com.locked.shingranicommunity.locked.models.MemberState

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
        RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent,false)),
        View.OnClickListener {

        private var binding: ItemMemberBinding = DataBindingUtil.bind(itemView)!!
        private var itemViewModel: MemberListViewModel.ItemViewModel? = null

        fun bind(itemViewModel: MemberListViewModel.ItemViewModel) {
            clearOldBindings()
            var invited = true
            this.itemViewModel = itemViewModel
            // EMAIL
            itemViewModel.title.observe(lifeCycleOwner, Observer {
                binding.title.text = it
            })
            // ADMIN
            itemViewModel.showAdmin.observe(lifeCycleOwner, Observer {
                binding.admin.visibility = if (it != null && it) View.VISIBLE else View.GONE
            })
            // PHONE
            itemViewModel.showPhoneAction.observe(lifeCycleOwner, Observer {
                if (it != null){
                    binding.phone.isEnabled = it
                    binding.phone.setOnClickListener {
                        itemViewModel.makePhoneCall()
                    }
                }
            })
            // TEXT
            itemViewModel.showTextAction.observe(lifeCycleOwner, Observer {
                binding.text.isEnabled = it
            })
            // EMAIL
            itemViewModel.showEmailAction.observe(lifeCycleOwner, Observer {
                binding.email.isEnabled = it
            })
            binding.email.setOnClickListener { itemViewModel.sendEmail() }
            // SETTING
            itemViewModel.showSettingsAction.observe(lifeCycleOwner, Observer {
                binding.settings.isVisible = it
            })
            binding.settings.setOnClickListener { itemViewModel.settings() }
            // INVITED
            itemViewModel.showInvited.observe(lifeCycleOwner, Observer {
                binding.invited.isVisible = it
                invited = it
            })
            // BLOCK
            itemViewModel.showBlocked.observe(lifeCycleOwner, Observer {
                binding.blocked.isVisible = it
            })
            itemViewModel.showBlockAction.observe(lifeCycleOwner, Observer {
                binding.block.isVisible = it
            })
            itemViewModel.showUnblockAction.observe(lifeCycleOwner, Observer {
                binding.unblock.isVisible = it
            })
            itemViewModel.showBlockConfirmation.observe(lifeCycleOwner, blockConfirmationObserver)
            binding.block.setOnClickListener { itemViewModel.block() }
            binding.unblock.setOnClickListener { itemViewModel.unblock() }

            //phone number
            itemViewModel.phoneNumber.observe(lifeCycleOwner, Observer {
                binding.phoneNumber.text = it
                Log.e("DEBUG_HIDE_PHONE_NUMBER", "${itemViewModel.member.user?.name} - >${itemViewModel.member.user?.hideNumber}")
                if (itemViewModel.member.isMe){
                    binding.phoneNumber.visibility = View.GONE
                }else if (itemViewModel.member.user?.hideNumber == true){

                    binding.phoneNumber.visibility = View.GONE
                }else if (invited){
                    binding.phoneNumber.visibility = View.GONE
                }else {
                    binding.phoneNumber.visibility = View.VISIBLE
                }


            })
        }

        private val blockConfirmationObserver: Observer<Boolean> = Observer {
            if (it) {
                showBlockAlert(itemViewModel!!)
            }
        }

        private fun showBlockAlert(itemViewModel: MemberListViewModel.ItemViewModel) {
            AlertDialog.Builder(itemView.context)
                .setTitle(itemViewModel.blockConfirmationTitle)
                .setMessage(itemViewModel.blockConfirmationDesc)
                .setPositiveButton(itemView.context.getString(R.string.alert_dialog_yes)) { p0, p1 -> itemViewModel.block(true) }
                .setNegativeButton(itemView.context.getString(R.string.alert_dialog_no)) { p0, p1 -> itemViewModel.cancelBlock() }
                .setOnCancelListener { itemViewModel.cancelBlock() }
                .show()
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.block -> {
                    itemViewModel?.block()
                }
            }
        }

        private fun clearOldBindings() {
            itemViewModel?.showBlockConfirmation?.removeObservers(lifeCycleOwner)
        }
    }
}