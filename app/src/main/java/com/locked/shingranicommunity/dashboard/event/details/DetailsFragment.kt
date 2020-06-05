package com.locked.shingranicommunity.dashboard.event.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.EventDetailsFragmentBinding
import com.locked.shingranicommunity.search_map.ItemDetailsComponentProvider
import com.locked.shingranicommunity.search_map.ItemDetailsViewModel
import javax.inject.Inject


class DetailsFragment : Fragment() {

    lateinit var viewModel: ItemDetailsViewModel
    lateinit var mBinding: EventDetailsFragmentBinding
    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.event_details_fragment, container, false)
        viewModel = ViewModelProviders.of(this,viewModelProvider).get(ItemDetailsViewModel::class.java)

        val lottieAnimationView = mBinding.envelopOpen
        lottieAnimationView.setImageAssetsFolder("images/")
        lottieAnimationView.setAnimation("data.json")
        lottieAnimationView.loop(false)
        lottieAnimationView.playAnimation()
        return mBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as ItemDetailsComponentProvider).itemDetailsComponent.inject(this)
    }

}
