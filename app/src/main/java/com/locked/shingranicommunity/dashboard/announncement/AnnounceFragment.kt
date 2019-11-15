package com.locked.shingranicommunity.dashboard.announncement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.locked.shingranicommunity.R

import com.locked.shingranicommunity.databinding.FragmentAnnouncementBinding

class AnnounceFragment : Fragment(),View.OnClickListener {
    override fun onClick(v: View?) {

    }

    companion object {
        fun newInstance() = AnnounceFragment()
    }
    private var mBinding: FragmentAnnouncementBinding? = null

    private val viewModel: AnnounceViewModel by viewModels {ItemLiveDataVMFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_announcement, container, false)
        viewModel.lifecycleOwner = this

        viewModel.load( )
        viewModel._fetchItems.let {
            it.let {
                viewModel.onRefresh()
            }
        }

        viewModel._fetchItems.observe(this, Observer {
            val adapter = AnnounceListAdapter(it)
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            mBinding!!.announcementRecyclerView.layoutManager = layoutManager
            mBinding!!.announcementRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()

        })
        return mBinding!!.root
    }

}
