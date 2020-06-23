package com.locked.shingranicommunity.announcement

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.FragmentAnnouncementBinding
import javax.inject.Inject

class AnnouncementListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: AnnouncementListViewModel
    lateinit var binding: FragmentAnnouncementBinding
    lateinit var adapter: AnnouncementListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_announcement, container, false)
        adapter = AnnouncementListAdapter(viewModel, viewLifecycleOwner)
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        binding.progress.visibility = View.VISIBLE
        viewModel.list.observe(viewLifecycleOwner, Observer {
            adapter.list.apply {
                this.clear()
            }.also { list ->
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
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as AnnouncementComponentProvider).announcementComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AnnouncementListViewModel::class.java)
    }
}