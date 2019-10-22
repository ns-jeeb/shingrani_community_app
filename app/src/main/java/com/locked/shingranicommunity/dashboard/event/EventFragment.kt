package com.locked.shingranicommunity.dashboard.event


import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.locked.shingranicommunity.R
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.locked.shingranicommunity.dashboard.announncement.Announcement
import com.locked.shingranicommunity.databinding.EventItemBinding
import com.locked.shingranicommunity.databinding.FragmentEventBinding

/**
 * A simple [Fragment] subclass.
 */
class EventFragment : Fragment() {

    interface OnEventFragmentTransaction {
        fun onFragmentInteraction(uri: Uri)
    }

    val ARG_PARAM1 = "param1"
    val ARG_PARAM2 = "param2"
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mListener: OnEventFragmentTransaction? = null
    private lateinit var mBinding : FragmentEventBinding

    private lateinit var viewModel: EventViewModel

    companion object{
        fun newInstance(param1: String, param2: String): EventFragment {
            val fragment = EventFragment()
            val args = Bundle()
            args.putString(EXTRA_MESSAGE, param1)
            args.putString(EXTRA_MESSAGE, param2)
            return fragment
        }

    }

    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEventFragmentTransaction) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnEventFragmentTransaction")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EventViewModel::class.java)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Toast.makeText(context,"Announcement", Toast.LENGTH_LONG) .show()
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_event, container, false)
        setUpRecyclerAdapter()
        return mBinding.root
    }

    fun setUpRecyclerAdapter(){

        val adapter = EventsListAdapter(viewModel.event)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mBinding!!.eventRecyclerView.layoutManager = layoutManager
        Toast.makeText(context,"Announcement", Toast.LENGTH_LONG) .show()
        mBinding!!.eventRecyclerView.adapter = adapter

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    class EventListAdapter (val mEvents: MutableLiveData<List<Announcement>>?) : RecyclerView.Adapter<EventListAdapter.EventViewHolder>() {



        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): EventViewHolder {
            return EventViewHolder(viewGroup)
        }

        override fun onBindViewHolder(announceViewHolder: EventViewHolder, i: Int) {

            mEvents?.value?.get(i)?.let { announceViewHolder.bind(it) }
        }


        override fun getItemCount(): Int {
            return mEvents?.value?.size!!
        }

        inner class EventViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)) {

            internal var binding: EventItemBinding? = null

            init {
                binding = DataBindingUtil.bind(itemView)
            }

            fun bind(event: Announcement) {
                binding!!.txtEventName.text = "Wedding"
                binding!!.txtEventDate.text = "July 22"
                binding!!.txtEventTime.text = "6PM"
                binding!!.txtEventType.text = "Event"
                binding!!.txtEventAddress.text = "50 Esterbrooke Ave"
            }
        }
    }

}
