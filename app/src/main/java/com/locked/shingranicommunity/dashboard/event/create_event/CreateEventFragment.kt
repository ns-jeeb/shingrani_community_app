package com.locked.shingranicommunity.dashboard.event.create_event

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.locked.shingranicommunity.Constant_Utils.CREATED_EVENT
import com.locked.shingranicommunity.Constant_Utils.ONE_01
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.di2.viewmodel.ViewModelProviderFactory
import com.locked.shingranicommunity.databinding.CreateEventFragmentBinding
import com.locked.shingranicommunity.members.MemberActivity
import java.io.IOException
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CreateEventFragment : Fragment() ,View.OnClickListener {
    var timeFragment : TimePickerFragment? = null
    var dateFragment : DatePickerFragment? = null
    lateinit var mBinding: CreateEventFragmentBinding
    @Inject
    lateinit var viewModelProviders: ViewModelProviderFactory
    lateinit var viewModel: CreateEventViewModel
    var type:String?  =null

    private fun getTypeItem():String{
        val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item, resources.getStringArray(R.array.events_arrays))
        mBinding.crEventType.adapter = adapter
        var itemEvent = ""
        mBinding.crEventType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("ItemType","$position ${adapter.getItem(position)}")
                type = adapter.getItem(position)
            }
        }

        return itemEvent
    }
    fun showTimePicker(v: View){
        activity?.supportFragmentManager?.let {
            timeFragment = TimePickerFragment(mBinding.crEventTime)
            timeFragment!!.show(it,"timePicker") }
    }

    fun showDatePicker(v: View){
        activity?.supportFragmentManager?.let {
            dateFragment = DatePickerFragment(mBinding.crEventDate)
            dateFragment!!.show(it,"datePicker")

        }
    }
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
//        viewModel.lifecycleOwner = this
        mBinding = DataBindingUtil.inflate(inflater, R.layout.create_event_fragment,container,false)
        viewModel = ViewModelProviders.of(this, viewModelProviders).get(CreateEventViewModel::class.java)
        mBinding.btnCrEvent.setOnClickListener(this)
        mBinding.crEventDate.setOnClickListener(this)
        type = getTypeItem()
        mBinding.crEventTime.setOnClickListener(this)
        mBinding.txtUserSelection.setOnClickListener(this)
        mBinding.imgSearchLocation.setOnClickListener(this)
        return mBinding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_cr_event -> {
                createEvent()
            }
            R.id.cr_event_time -> {
                showTimePicker(v)
            }
            R.id.cr_event_date -> {
                showDatePicker(v)
            }
            R.id.txt_user_selection -> {
                //            launch list of user
                val intent = Intent(activity, MemberActivity::class.java)
                intent.putExtra(CREATED_EVENT,true)
                startActivityForResult(intent, ONE_01)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as CreateItemActivity).dashboardCompunent.inject(this)
    }
    private fun createEvent(){
        timeFragment?.let{mBinding.crEventTime.text = timeFragment!!.getTheTime()}
        dateFragment?.let{mBinding.crEventDate.text = dateFragment!!.getTheDate()}
        if (timeFragment != null && dateFragment!= null) {
            var eventType: String = ""
            type?.let {
                eventType = it
            }
            viewModel.createEvent(
                mBinding.crEventName.text.toString(),
                eventType,
                mBinding.crLocationSearch.text.toString(),

                "${mBinding.crEventDate.text}T${mBinding.crEventTime.text}",
                mBinding.crEventDetails.text.toString(),
                "",
                "",
                mBinding.crEventDetails.text.toString()
            ).observe(this@CreateEventFragment, Observer {
                if (it.isNullOrBlank()) {
                    var intent = Intent()
                    activity?.setResult(Activity.RESULT_OK, intent)
                    activity?.finish()
                } else {

                }
            })
        }

    }
}

class TimePickerFragment(var txtView: TextView) : DialogFragment(),TimePickerDialog.OnTimeSetListener {
    var time: String = ""
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute,true)
    }
    fun getTheTime():String{return time}

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        time = "$hourOfDay:$minute"
        txtView.text = "$hourOfDay:$minute"
    }
}

class DatePickerFragment(var txtView: TextView) : DialogFragment(),DatePickerDialog.OnDateSetListener {
    var date: String = ""
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of TimePickerDialog and return it
        return DatePickerDialog(requireActivity(), this, year, month, day)
    }
    fun getTheDate():String{return date}


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        date = "$year-$month-$dayOfMonth"
        txtView.text = "$year-$month-$dayOfMonth"
    }

}
