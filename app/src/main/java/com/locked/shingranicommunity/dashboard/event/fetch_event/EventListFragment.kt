package com.locked.shingranicommunity.dashboard.event.fetch_event

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.locked.shingranicommunity.R
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.locked.shingranicommunity.Constant_Utils
import com.locked.shingranicommunity.dashboard.DashBoardViewPagerActivity
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.dashboard.event.EventsListAdapter
import com.locked.shingranicommunity.dashboard.event.OnInvitedListener
import com.locked.shingranicommunity.dashboard.event.create_event.CreateItemActivity
import com.locked.shingranicommunity.databinding.FragmentEventListBinding
import javax.inject.Inject

class EventListFragment : Fragment(),OnInvitedListener {

    val ARG_TOKEN = "token"
    val ARG_PARAM2 = "param2"
    private var mToken: String? = null
    private var mParam2: String? = null
    private lateinit var mBinding : FragmentEventListBinding
    private var isChecked= false

    lateinit var eventViewModel: EventViewModel
    @Inject
    lateinit var viewModelProviders: ViewModelProvider.Factory

    companion object{
        fun newInstance(isCreate: Boolean): EventListFragment {
            val fragment = EventListFragment()
            val args = Bundle()
            args.putBoolean(EXTRA_MESSAGE, isCreate)
            fragment.arguments = args
            return fragment
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity!! as DashBoardViewPagerActivity).dashboardComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mToken = arguments!!.getString(ARG_TOKEN)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null){
            if (requestCode == 100) {
                setupListViewAdapter()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("OnResume","call update here")
    }
    @SuppressLint("RestrictedApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_event_list, container, false)
        eventViewModel = ViewModelProviders.of(this,viewModelProviders).get(EventViewModel::class.java)
       setupListViewAdapter()

        return mBinding.root
    }
    private lateinit var adapter: EventsListAdapter
    @SuppressLint("SetTextI18n")
    fun setupListViewAdapter() {
        var hideDeleteMenu = true
        eventViewModel.itemsLoaded().observe(this, Observer {
            if (eventViewModel.getAdminUser()?._id == eventViewModel.getCurrentUser()._id){
                hideDeleteMenu = false
            }
            adapter = eventViewModel.getCurrentUser()?.let { it1 -> EventsListAdapter(it, it1,hideDeleteMenu)}
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter.setOnInvitedEvent(this)
            mBinding.eventRecyclerView.layoutManager = layoutManager
            mBinding.eventRecyclerView.adapter = adapter
            mBinding.progressEvent.visibility = View.GONE
            adapter.notifyDataSetChanged()
        })
    }

    override fun onAccepted(eventitem: Item, position: Int) {
        if (eventitem.creator != eventViewModel.getCurrentUser()._id){
            eventViewModel.accepted(eventitem)?.observe(this@EventListFragment, Observer {
                Toast.makeText(activity, it,Toast.LENGTH_LONG).show()
                if (it.isNullOrBlank()){
                    setupListViewAdapter()
                }
            })
        }else{
            Toast.makeText(context,"You can not accept your event",Toast.LENGTH_LONG).show()
        }
    }

    override fun onRejected(eventitem : Item) {
        if (eventitem.creator != eventViewModel.getCurrentUser()._id){
            eventViewModel.rejected(eventitem)?.observe(this@EventListFragment, Observer {
                Toast.makeText(activity,it,Toast.LENGTH_LONG).show()
                if (it.isNullOrBlank()){
                    setupListViewAdapter()
                }
            })
        }else{
            Toast.makeText(context,"You can not reject your event",Toast.LENGTH_LONG).show()
        }
    }

    override fun onUpdate(eventitem: Item, update: String) {
        TODO("Not yet implemented")
    }

    override fun onDeleted(eventitem: Item, deleted: String) {
        eventViewModel.itemDelete(eventitem._id!!)?.observe(this@EventListFragment, Observer {
            if (it.isNullOrBlank()){
                setupListViewAdapter()
                //mBinding.txtMessageEvent.visibility = View.GONE
            }else{
                //mBinding.txtMessageEvent.visibility = View.VISIBLE
                //mBinding.txtMessageEvent.text = it
            }
        })
    }
}