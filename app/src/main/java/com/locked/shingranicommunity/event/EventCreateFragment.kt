package com.locked.shingranicommunity.event

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.locked.shingranicommunity.Constant_Utils
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.dashboard.event.map.MapsActivity
import com.locked.shingranicommunity.databinding.CreateEventFragmentBinding
import com.locked.shingranicommunity.members.MemberActivity
import javax.inject.Inject

class EventCreateFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: EventCreateViewModel
    private lateinit var binding: CreateEventFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.create_event_fragment, container, false)
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        binding.crEventName.doOnTextChanged { text, start, count, after ->
            viewModel.title.postValue(text.toString())
        }
        binding.imgSearchLocation.setOnClickListener {
            val intent = Intent(activity, MapsActivity::class.java)
            intent.putExtra(Constant_Utils.CREATED_EVENT,true)
            startActivityForResult(intent, Constant_Utils.ONE_01)
        }
        binding.crEventTime.doOnTextChanged { text, start, count, after ->
            viewModel.time.postValue(text.toString())
        }
        binding.crEventDate.doOnTextChanged { text, start, count, after ->
            viewModel.date.postValue(text.toString())
        }
        binding.crEventType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.type.postValue(p0?.adapter?.getItem(p2).toString())
            }
        }
        binding.crEventDetails.doOnTextChanged { text, start, count, after ->
            viewModel.desc.postValue(text.toString())
        }
        binding.btnCrEvent.setOnClickListener {
            viewModel.create()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as EventComponentProvider).eventComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EventCreateViewModel::class.java)
    }
}