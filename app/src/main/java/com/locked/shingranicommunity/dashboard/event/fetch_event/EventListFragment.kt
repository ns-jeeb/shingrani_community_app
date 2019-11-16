package com.locked.shingranicommunity.dashboard.event.fetch_event


import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.locked.shingranicommunity.R
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.locked.shingranicommunity.dashboard.event.EventsListAdapter
import com.locked.shingranicommunity.databinding.FragmentEventListBinding

/**
 * A simple [Fragment] subclass.
 */
class EventListFragment : Fragment() {

    interface OnEventFragmentTransaction {
        fun onFragmentInteraction(uri: Uri)
    }

    val ARG_PARAM1 = "param1"
    val ARG_PARAM2 = "param2"
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mListener: OnEventFragmentTransaction? = null
    private lateinit var mBinding : FragmentEventListBinding

    private val eventViewModel: EventViewModel by viewModels { EventViewModel.EventItemVMFactory }

    companion object{
        fun newInstance(param1: String, param2: String): EventListFragment {
            val fragment = EventListFragment()
            val args = Bundle()
            args.putString(EXTRA_MESSAGE, param1)
            args.putString(EXTRA_MESSAGE, param2)
            return fragment
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEventFragmentTransaction) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnEventFragmentTransaction")
        }
        eventViewModel.lifecycleOwner = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_event_list, container, false)

        eventViewModel.load()

        eventViewModel._fetchItems.let {
            it.let {
                eventViewModel.onRefresh()
            }
            Log.d("Test_lambda","$it").let {

            }
        }
        eventViewModel._fetchItems.observe(this, Observer {
            val adapter = EventsListAdapter(it)
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            mBinding!!.eventRecyclerView.layoutManager = layoutManager
            mBinding!!.eventRecyclerView.adapter = adapter
            mBinding.progressEvent.visibility = View.GONE
            adapter.notifyDataSetChanged()
        })

        return mBinding.root
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

}
