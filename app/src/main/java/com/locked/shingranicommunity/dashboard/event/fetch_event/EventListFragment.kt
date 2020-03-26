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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.locked.shingranicommunity.dashboard.DashBoardViewPagerActivity
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.dashboard.event.EventsListAdapter
import com.locked.shingranicommunity.dashboard.event.OnInvitedListener
import com.locked.shingranicommunity.databinding.FragmentEventListBinding
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class EventListFragment : Fragment(),OnInvitedListener {

    interface OnEventFragmentTransaction {
        fun onFragmentInteraction(uri: Uri)
    }

    val ARG_TOKEN = "token"
    val ARG_PARAM2 = "param2"
    private var mToken: String? = null
    private var mParam2: String? = null
    private var mListener: OnEventFragmentTransaction? = null
    private lateinit var mBinding : FragmentEventListBinding

    lateinit var eventViewModel: EventViewModel
    @Inject
    lateinit var viewModelProviders: ViewModelProvider.Factory

    companion object{
        fun newInstance(token: String, isCreate: Boolean): EventListFragment {
            val fragment = EventListFragment()
            val args = Bundle()
            val ARG_TOKEN = "token"
            val ISTOKEN :Boolean = false
            args.putString(ARG_TOKEN, token)
            args.putBoolean(EXTRA_MESSAGE, isCreate)
            fragment.arguments = args
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
        (activity!! as DashBoardViewPagerActivity).dashboardCompunent.inject(this)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mToken = arguments!!.getString(ARG_TOKEN)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_event_list, container, false)
        eventViewModel = ViewModelProviders.of(this,viewModelProviders).get(EventViewModel::class.java)
        eventViewModel.itemsLoaded().observe(this, Observer {
            if (it != null) {

                (activity as DashBoardViewPagerActivity).hideOrShowProgress(false)
            }else{
                (activity as DashBoardViewPagerActivity).hideOrShowProgress(true)
            }
            val adapter = eventViewModel.getCurrentUser()?.let { it1 -> EventsListAdapter(it, it1) }
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter.setOnInvitedEvent(this)
            mBinding!!.eventRecyclerView.layoutManager = layoutManager
            mBinding!!.eventRecyclerView.adapter = adapter
            mBinding.progressEvent.visibility = View.GONE
            adapter.notifyDataSetChanged()
        })
        eventViewModel.getAdminUser().observe(this, Observer {
            if (it != null && it.admins[0]._id.contentEquals(eventViewModel.getCurrentUser()._id)){
                Log.d("Admin_user","user = ${it.admins[0].name}")
                mBinding.userState.text = "${it.admins[0].name}: Admin"
            }else{
                mBinding.userState.text = "Regular User"
            }
        })

        return mBinding.root
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onAccepted(eventitem : Item) {
        eventViewModel.updateItem(eventitem)
    }

    override fun onRejected(eventitem : Item) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}