package com.locked.shingranicommunity.details

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.EventDetailsFragmentBinding
import com.locked.shingranicommunity.models.EventItem
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
        viewModel = ViewModelProviders.of(this, viewModelProvider).get(ItemDetailsViewModel::class.java)
        mBinding.envelopOpen.setImageAssetsFolder("images/")

        mBinding.envelopOpen.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationEnd(animation: Animator?) {
                Log.d("Lottie_start","this where animation end")
                mBinding.attendees.text = "2"
                mBinding.going.text = "Going"
                mBinding.txtEventName.text = "in this event"
                mBinding.envelopOpen.isEnabled = false
                mBinding.imgEvent.setImageBitmap(viewModel.getBackgroundImage(requireContext()))
            }

            override fun onAnimationCancel(animation: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationStart(animation: Animator?) {
                Log.d("Lottie_start","this where animation start")
            }

        })
        mBinding.envelopOpen.playAnimation()
        return mBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as ItemDetailsComponentProvider).itemDetailsComponent.inject(this)
    }

}
