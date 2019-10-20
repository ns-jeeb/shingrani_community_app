package com.locked.shingranicommunity.dashboard.announncement

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.FragmentAnnouncementBinding

class AnnounceFragment : Fragment(),View.OnClickListener {
    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun newInstance() = AnnounceFragment()
    }
    private var mBinding: FragmentAnnouncementBinding? = null

    private lateinit var viewModel: AnnounceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(AnnounceViewModel::class.java)
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_announcement, container, false)
        return mBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpRecyclerAdapter()
    }

    fun setUpRecyclerAdapter(){

        val adapter = AnnounceListAdapter(viewModel.announce)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mBinding!!.announcementRecyclerView.layoutManager = layoutManager
        Toast.makeText(context,"Announcement", Toast.LENGTH_LONG) .show()
        mBinding!!.announcementRecyclerView.adapter = adapter
        mBinding!!.btnTest.setOnClickListener(this)
    }

}
