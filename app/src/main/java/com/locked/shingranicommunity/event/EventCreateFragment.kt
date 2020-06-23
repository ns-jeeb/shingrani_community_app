package com.locked.shingranicommunity.event

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.material.snackbar.Snackbar
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.CreateEventFragmentBinding
import javax.inject.Inject

class EventCreateFragment : Fragment(), TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {

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
        // PAGE TITLE
        requireActivity().title = viewModel.pageTitle
        // MESSAGE
        viewModel.message.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrBlank()) {
                Snackbar.make(binding.coordinator, it, Snackbar.LENGTH_SHORT).show()
                viewModel.messageHandled()
            }
        })
        // NAME
        viewModel.title.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty() && it != binding.eventName.text.toString()) {
                binding.eventName.setText(it)
            }
        })
        binding.eventName.doOnTextChanged { text, start, count, after ->
            binding.eventName.error = null
            viewModel.setTitle(text.toString())
        }
        viewModel.isTitleValid.observe(viewLifecycleOwner, Observer {
            binding.eventName.error = it
        })
        // LOCATION
        viewModel.location.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty() && it != binding.eventLocation.text.toString()) {
                binding.eventLocation.setText(it)
            }
        })
        binding.eventLocation.doOnTextChanged{text, start, count, after ->
            binding.eventLocation.error = null
            viewModel.setLocation(text.toString())
        }
        binding.eventLocationTi.setEndIconOnClickListener {
            viewModel.searchAddress()
        }
        viewModel.isLocationValid.observe(viewLifecycleOwner, Observer {
            binding.eventLocation.error = it
        })
        // DATE
        viewModel.date.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty() && it != binding.eventDate.text.toString()) {
                binding.eventDate.setText(it)
            }
        })
        binding.eventDate.setOnClickListener { it ->
            binding.eventDate.error = null
            showDatePicker(it)
        }
        viewModel.isDateValid.observe(viewLifecycleOwner, Observer {
            binding.eventDate.error = it
        })
        // TIME
        viewModel.time.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty() && it != binding.eventTime.text.toString()) {
                binding.eventTime.setText(it)
            }
        })
        binding.eventTime.setOnClickListener {it ->
            binding.eventTime.error = null
            showTimePicker(it)
        }
        viewModel.isTimeValid.observe(viewLifecycleOwner, Observer {
            binding.eventTime.error = it
        })
        // TYPE
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_menu_item,
            viewModel.eventTypes
        )
        binding.eventType.setAdapter(adapter)
        viewModel.type.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty() && it != binding.eventType.text.toString()) {
                binding.eventType.setText(it, false)
            }
        })
        binding.eventType.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, index: Int, p3: Long) {
                binding.eventType.error = null
                viewModel.setType(p0?.adapter?.getItem(index).toString())
            }
        }
        viewModel.isTypeValid.observe(viewLifecycleOwner, Observer {
            binding.eventType.error = it
        })
        // DETAIL
        viewModel.desc.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty() && it != binding.eventDetail.text.toString()) {
                binding.eventDetail.setText(it)
            }
        })
        binding.eventDetail.doOnTextChanged { text, start, count, after ->
            binding.eventDetail.error = null
            viewModel.setDescription(text.toString())
        }
        viewModel.isDescriptionValid.observe(viewLifecycleOwner, Observer {
            binding.eventDetail.error = it
        })
        // CREATE
        binding.create.setOnClickListener {
            viewModel.create()
        }
    }

    private fun showDatePicker(v: View) {
        activity?.supportFragmentManager?.let {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(requireContext(),
                this,
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.MONTH]
            )
            datePickerDialog.datePicker.minDate = calendar.timeInMillis
            datePickerDialog.show()
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        viewModel.setDate(year, month, dayOfMonth)
    }

    private fun showTimePicker(v: View){
        activity?.supportFragmentManager?.let {
            val calendar = Calendar.getInstance()
            TimePickerDialog(requireContext(), this,
                calendar[Calendar.HOUR_OF_DAY],
                calendar[Calendar.MINUTE],
                false).show()
        }
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        viewModel.setTime(hourOfDay, minute)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as EventComponentProvider).eventComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EventCreateViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (requestCode == EventConstants.REQUEST_CODE_SEARCH) {
                when (resultCode) {
                    RESULT_OK -> {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        viewModel.setLocation("${place.address}")
                    }
                }
            }
        }
    }
}