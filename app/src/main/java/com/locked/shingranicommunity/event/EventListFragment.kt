package com.locked.shingranicommunity.event

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.locked.shingranicommunity.Constant_Utils
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.FragmentEventListBinding
import com.locked.shingranicommunity.models.EventItem
import com.locked.shingranicommunity.models.Item
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class EventListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: EventListViewModel
    lateinit var binding: FragmentEventListBinding
    lateinit var adapter: EventListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_list, container, false)
        adapter = EventListAdapter(viewModel)
        setupViews()
        binding.progressEvent.visibility = View.VISIBLE
        return binding.root
    }

    private fun setupViews() {
        viewModel.list.observe(viewLifecycleOwner, Observer {
            adapter.list.apply {
                this.clear()
            }.also { list ->
                list.addAll(it)
                val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                binding.eventRecyclerView.layoutManager = layoutManager
                binding.eventRecyclerView.adapter = adapter
                binding.progressEvent.visibility = View.GONE
            }
            adapter.notifyDataSetChanged()
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as EventComponentProvider).eventComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EventListViewModel::class.java)
    }
}