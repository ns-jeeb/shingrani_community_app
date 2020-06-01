package com.locked.shingranicommunity.event

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.locked.shingranicommunity.Constant_Utils
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.CreateEventFragmentBinding
import javax.inject.Inject

class EventCreateFragment : Fragment() {

    private val TAG_Event = "EventCreateFragment"
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
            viewModel.searchAddress(requireActivity())
        }
        binding.crLocationSearch.doOnTextChanged{text, start, count, after ->
            viewModel.location.postValue(text.toString())
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (requestCode == Constant_Utils.ONE_03) {
                when (resultCode) {
                    RESULT_OK -> {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        binding.crLocationSearch.setText("${place.address}")
                    }
                    AutocompleteActivity.RESULT_ERROR -> {
                        val status = Autocomplete.getStatusFromIntent(data)
                    }
                    RESULT_CANCELED -> {
                        Log.i(TAG_Event, "user canceled searching")
                    }
                }
            }
        }
    }
}