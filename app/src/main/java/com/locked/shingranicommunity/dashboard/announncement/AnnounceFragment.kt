package com.locked.shingranicommunity.dashboard.announncement

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.locked.shingranicommunity.Constant_Utils.ANNOUNCEMENT_CREATED
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.dashboard.DashBoardViewPagerActivity
import com.locked.shingranicommunity.dashboard.announncement.create_announce.CreateAnnouncementActivity
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.dashboard.event.OnInvitedListener
import com.locked.shingranicommunity.databinding.FragmentAnnouncementBinding
import javax.inject.Inject

class AnnounceFragment : Fragment(),View.OnClickListener,OnInvitedListener {
    companion object {
        fun newInstance() = AnnounceFragment()
    }
    private var mBinding: FragmentAnnouncementBinding? = null

    lateinit var viewModel: AnnounceViewModel
    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_announcement, container, false)

        viewModel = ViewModelProviders.of(this,viewModelProvider).get(AnnounceViewModel::class.java)
        viewModel.load( )
        viewModel._fetchItems.let {
            it.let {
                viewModel.onRefresh()
            }
        }
        if (viewModel.getAdminUser()?._id!= null){
            mBinding?.fabCreateAnnouncement?.visibility =View.VISIBLE
        }else{
            mBinding?.fabCreateAnnouncement?.visibility = View.GONE
        }
        mBinding?.fabCreateAnnouncement?.setOnClickListener(this)
        setupListViewAdapter()
        return mBinding!!.root
    }
    private fun setupListViewAdapter(){
        var hideDeleteMenu = true
        viewModel.loadedAnnouncements().observe(this, Observer {
            if (viewModel.getAdminUser()?._id == viewModel.userManager.getCurrentUser()?._id){
                hideDeleteMenu = false
            }
            val adapter = AnnounceListAdapter(it,hideDeleteMenu)
            adapter.setOnInvitedEvent(this)
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            mBinding!!.announcementRecyclerView.layoutManager = layoutManager
            mBinding!!.announcementRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()

        })
    }
    override fun onClick(v: View?) {
        if (v?.id == R.id.fab_create_announcement){
            var intent = Intent(activity, CreateAnnouncementActivity::class.java)
            startActivityForResult(intent, 102)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null){
            if (requestCode == 102 && data.getBooleanExtra(ANNOUNCEMENT_CREATED,false)){
                viewModel.loadedAnnouncements()
            }
        }

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as DashBoardViewPagerActivity).dashboardCompunent.inject(this)
    }
    override fun onAccepted(eventitem: Item, accepted: String) {
//        viewModel.updateItem(eventitem, accepted)
    }

    override fun onRejected(eventitem : Item, rejected: String) {
//        viewModel.updateItem(eventitem,rejected)
    }

    override fun onDeleted(eventitem: Item, deleted: String) {
        viewModel.deleteAnnounce(eventitem._id!!)?.observe(this@AnnounceFragment, Observer {
            if (it.isNullOrBlank()){
                viewModel.load()
                setupListViewAdapter()
            }else{
                mBinding?.txtErrorAnnounceMessage?.text = it
            }
        })
    }
}
