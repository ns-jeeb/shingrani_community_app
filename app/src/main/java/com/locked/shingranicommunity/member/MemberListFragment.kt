package com.locked.shingranicommunity.member

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.common.ToolbarProvider
import com.locked.shingranicommunity.databinding.FragmentMemberListBinding
import javax.inject.Inject

class MemberListFragment : Fragment(), ToolbarProvider {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MemberListViewModel
    private lateinit var binding: FragmentMemberListBinding
    private lateinit var adapter: MemberListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_member_list, container, false)
        adapter = MemberListAdapter(viewModel, viewLifecycleOwner)
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        // PAGE TITLE
        requireActivity().title = viewModel.pageTitle
        // LIST
        binding.progress.visibility = View.VISIBLE
        viewModel.list.observe(viewLifecycleOwner, Observer {
            adapter.list.apply {
                this.clear()
            }.also { list ->
                if (it.isNotEmpty()){
                    // TODO: we have add a field for hideNumber on Member Model then we remove this
                    it[0].user?.hideNumber = true
                    it[1].user?.hideNumber = false
                    it[2].user?.hideNumber = true
                    it[3].user?.hideNumber = false
                    it[4].user?.hideNumber = true
                    it[5].user?.hideNumber = false
                }
                list.addAll(it)
                val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                binding.recyclerView.layoutManager = layoutManager
                binding.recyclerView.adapter = adapter
                binding.progress.visibility = View.GONE
            }
            adapter.notifyDataSetChanged()
        })
        viewModel.message.observe(viewLifecycleOwner, Observer {
            it?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                viewModel.messageHandled()
            }
        })
        // INVITE
        viewModel.showInvite.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.invite.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
        binding.invite.setOnClickListener {
            viewModel.invitePressed()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MemberComponentProvider).memberComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MemberListViewModel::class.java)
    }

    override fun provideToolbar(): Toolbar {
        return binding.toolbar
    }
}