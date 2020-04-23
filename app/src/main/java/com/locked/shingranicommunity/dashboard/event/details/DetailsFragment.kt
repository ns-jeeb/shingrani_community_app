package com.locked.shingranicommunity.dashboard.event.details

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.ViewModelProviderFactory
import com.locked.shingranicommunity.dashboard.data.Item
import com.locked.shingranicommunity.databinding.EventDetailsFragmentBinding
import javax.inject.Inject
@SuppressLint("SetTextI18n")
class DetailsFragment : Fragment() {

    companion object {
        fun newInstance(item: Item) :DetailsFragment {
            var fragment = DetailsFragment()
            var bundle: Bundle = Bundle()
            bundle.putParcelable("extra_item",item)
            fragment.arguments = bundle
            return fragment
        }
    }
    @Inject
    lateinit var viewModel: DetailsViewModel
    lateinit var mBinding: EventDetailsFragmentBinding
    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.event_details_fragment, container, false)
        mBinding.attendees.text = viewModel.getItem()?.fields?.get(5)?.value?.split(",")?.size.toString()
        var item = arguments?.getParcelable<Item>("extra_item")
        viewModel = ViewModelProviders.of(this,viewModelProvider).get(DetailsViewModel::class.java)
        mBinding.attendees.text = "${item?.fields?.get(5)?.value?.split(",")?.size} : Attend/s"
        mBinding.notAttendees.text = "${item?.fields?.get(6)?.value?.split(",")?.size} : Not Attend"
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as DetailsActivity).dashboardComponent.inject(this)
    }

}
