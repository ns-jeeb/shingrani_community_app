package com.locked.shingranicommunity.dashboard.event.fetch_event

import android.annotation.SuppressLint
import android.app.Dialog
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
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.locked.shingranicommunity.Constant_Utils
import com.locked.shingranicommunity.dashboard.DashBoardViewPagerActivity
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.dashboard.event.EventsListAdapter
import com.locked.shingranicommunity.dashboard.event.OnInvitedListener
import com.locked.shingranicommunity.dashboard.event.OnItemClickListener
import com.locked.shingranicommunity.dashboard.event.create_event.CreateItemActivity
import com.locked.shingranicommunity.dashboard.event.details.DetailsActivity
import com.locked.shingranicommunity.dashboard.event.details.DetailsFragment
import com.locked.shingranicommunity.databinding.FragmentEventListBinding
import javax.inject.Inject

class EventListFragment : Fragment(),OnInvitedListener,View.OnClickListener,OnItemClickListener {

    interface OnEventFragmentTransaction {
        fun onFragmentInteraction(uri: Uri)
    }

    val ARG_TOKEN = "token"
    val ARG_PARAM2 = "param2"
    private var mToken: String? = null
    private var mParam2: String? = null
    private var mListener: OnEventFragmentTransaction? = null
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
        mBinding.fabCreateEvent.setOnClickListener(this)
        if (eventViewModel.getAdminUser()?._id!= null){
            mBinding.fabCreateEvent.visibility =View.VISIBLE
        }else{
            mBinding.fabCreateEvent.visibility = View.GONE
        }
       setupListViewAdapter()

        return mBinding.root
    }
    private lateinit var adapter: EventsListAdapter

    @SuppressLint("SetTextI18n")
    fun setupListViewAdapter() {
        var hideDeleteMenu = true
        eventViewModel.itemsLoaded().observe(this, Observer {
            if (it != null) {
                (activity as DashBoardViewPagerActivity).hideOrShowProgress(false)
            } else {
                (activity as DashBoardViewPagerActivity).hideOrShowProgress(true)
            }
            if (eventViewModel.getAdminUser()?._id == eventViewModel.getCurrentUser()._id){
                hideDeleteMenu = false
            }
            adapter = eventViewModel.getCurrentUser()?.let { it1 -> EventsListAdapter(it, it1,hideDeleteMenu,this)}
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter.setOnInvitedEvent(this)
            mBinding.eventRecyclerView.layoutManager = layoutManager
            mBinding.eventRecyclerView.adapter = adapter
            mBinding.progressEvent.visibility = View.GONE
            adapter.notifyDataSetChanged()
        })

        if (eventViewModel.userManager.getAdminUser(eventViewModel.getCurrentUser()._id) != null) {
//                Log.d("Admin_user","user = ${it.admins[0].name}")
            mBinding.txtUserState.text = "${eventViewModel.userManager.getAdminUser(eventViewModel.getCurrentUser()._id)?.name}: Admin"
        } else {
            mBinding.txtUserState.text = "Regular User"
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
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
                mBinding.txtMessageEvent.visibility = View.GONE
            }else{
                mBinding.txtMessageEvent.visibility = View.VISIBLE
                mBinding.txtMessageEvent.text = it
            }
        })
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.fab_create_event){
            createItem()
        }
    }
    private fun createItem(){
        var inten = Intent(activity, CreateItemActivity::class.java)
        startActivityForResult(inten, Constant_Utils.ONE_00)
    }

    @SuppressLint("ResourceType")
    override fun onItemClick(position: Int, item: Item) {
       var intent: Intent = Intent(activity, DetailsActivity::class.java)
        intent.putExtra("extra_item",item)
        startActivity(intent)
    }
}
