package com.locked.shingranicommunity.event

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
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.FragmentEventListBinding
import com.locked.shingranicommunity.models.Item
import javax.inject.Inject

class EventListFragment : Fragment(), OnInvitedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var setUserPermission: OnSetUserPermission
    lateinit var viewModel: EventListViewModel
    lateinit var binding: FragmentEventListBinding
    lateinit var adapter: EventListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_list, container, false)
        adapter = EventListAdapter()
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        viewModel.list.observe(viewLifecycleOwner, Observer {
            setUserPermission = adapter
            adapter.list.apply {
                this.clear()
            }.also { list ->
                list.addAll(it)
                setUserPermission.setAdmin(viewModel.isAdminUser())
                setUserPermission.setCurrentUser(viewModel.currentUser()!!)
                val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter.setOnInvitedEvent(this)
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

    override fun onAccepted(eventitem: Item, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onRejected(eventitem: Item) {
        TODO("Not yet implemented")
    }

    override fun onUpdate(eventitem: Item, update: String) {
        TODO("Not yet implemented")
    }

    override fun onDeleted(eventitem: Item, deleted: String) {
        TODO("Not yet implemented")
    }
}