package com.locked.shingranicommunity.dashboard.event.create_event

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.locked.shingranicommunity.Constant_Utils.CREATED_EVENT
import com.locked.shingranicommunity.Constant_Utils.INVITED_GUESTS
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.ViewModelProviderFactory
import com.locked.shingranicommunity.databinding.CreateEventFragmentBinding
import com.locked.shingranicommunity.members.MemberActivity
import javax.inject.Inject


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CreateEventFragment : Fragment() ,View.OnClickListener{
    var timeFragment : TimePickerFragment? = null
    var dateFragment : DatePickerFragment? = null
    var numberOfGuests = Bundle()
    var type:String?  =null

    override fun onClick(v: View?) {
        timeFragment?.let{mBinding.crEventTime.text = timeFragment!!.getTheTime()}
        dateFragment?.let{mBinding.crEventDate.text = dateFragment!!.getTheDate()}
        if (timeFragment != null && dateFragment!= null){
            if (v?.id == R.id.btn_cr_event){
                type?.let {
                    viewModel.createEvent(
                        mBinding.crEventName.text.toString(),
                        it,
                        mBinding.crEventAddress.text.toString(),

                        "${mBinding.crEventDate.text} : ${mBinding.crEventTime.text}" ,
                        mBinding.crEventDetails.text.toString(),
                        "",
                        "",
                        mBinding.crEventDetails.text.toString()
                    ).observe(this, Observer         {

                        var intent = Intent()
                        intent.putExtra("response_message",it)
                        activity?.setResult(Activity.RESULT_OK, intent)
                        activity?.finish()
                    })
                }


            }else if (v?.id == R.id.txt_user_selection) {
//            launch list of user
                var intent = Intent(activity, MemberActivity::class.java)
                intent.putExtra(CREATED_EVENT,true)
                startActivityForResult(intent, 101)
            }
        }

    }
    private fun getTypeItem():String{
        val adapter = ArrayAdapter(context,android.R.layout.simple_spinner_item, resources.getStringArray(R.array.events_arrays))
        mBinding.crEventType.adapter = adapter
        mBinding.crEventType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("ItemType","$position ${adapter.getItem(position)}")
            }

        }

        return ""
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            if (requestCode == 101) {
                numberOfGuests = data.extras
                var arrayMember = numberOfGuests.getStringArrayList(INVITED_GUESTS)
                mBinding.txtUserSelection.text = "${arrayMember.size} Guests are invited"
            }
        }

    }

    lateinit var mBinding: CreateEventFragmentBinding

    companion object {
        fun newInstance() = CreateEventFragment()
    }

    @Inject
    lateinit var viewModelProviders: ViewModelProviderFactory
    lateinit var viewModel: CreateEventViewModel

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
//        viewModel.lifecycleOwner = this
        mBinding = DataBindingUtil.inflate(inflater, R.layout.create_event_fragment,container,false)
        viewModel = ViewModelProviders.of(this, viewModelProviders).get(CreateEventViewModel::class.java)
        mBinding.btnCrEvent.setOnClickListener(this)
        mBinding.crEventDate.setOnClickListener{
            showDatePicker(it)
        }
        type =getTypeItem()
        mBinding.crEventTime.setOnClickListener{
            showTimePicker(it)
        }

        mBinding.txtUserSelection.setOnClickListener(this)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as CreateItemActivity).dashboardCompunent.inject(this)
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
        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }
    fun getTheTime():String{return time}

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        time = "$hourOfDay:$minute"
        txtView.text = "$hourOfDay:$minute"
    }

}

class DatePickerFragment(var txtView: TextView) : DialogFragment(),DatePickerDialog.OnDateSetListener {
    var time: String = ""
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of TimePickerDialog and return it
        return DatePickerDialog(activity, this, year, month, day)
    }
    fun getTheDate():String{return time}


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        time = "$year:$month:$dayOfMonth"
        txtView.text = "$year:$month:$dayOfMonth"
    }

}
