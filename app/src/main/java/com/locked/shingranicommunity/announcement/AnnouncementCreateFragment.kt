package com.locked.shingranicommunity.announcement

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.FragmentCreateAnnouncementBinding
import javax.inject.Inject

class AnnouncementCreateFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AnnouncementCreateViewModel
    private lateinit var binding: FragmentCreateAnnouncementBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_announcement, container, false)
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        // PAGE TITLE
        requireActivity().title = viewModel.pageTitle
        // MESSAGE
        viewModel.message.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrBlank()) {
                Snackbar.make(binding.coordinator, it, Snackbar.LENGTH_SHORT).show()
                viewModel.messageHandled()
            }
        })
        // TITLE
        viewModel.title.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty() && it != binding.edCreateAnnouncementTitle.text.toString()) {
                binding.edCreateAnnouncementTitle.setText(it)
            }
        })
        binding.edCreateAnnouncementTitle.doOnTextChanged { text, start, count, after ->
            binding.edCreateAnnouncementTitle.error = null
            viewModel.setTitle(text.toString())
        }
        viewModel.isTitleValid.observe(viewLifecycleOwner, Observer {
            binding.edCreateAnnouncementTitle.error = it
        })
        // DETAIL
        viewModel.text.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty() && it != binding.edCreateAnnouncementDetail.text.toString()) {
                binding.edCreateAnnouncementDetail.setText(it)
            }
        })
        binding.edCreateAnnouncementDetail.doOnTextChanged { text, start, count, after ->
            binding.edCreateAnnouncementDetail.error = null
            viewModel.setText(text.toString())
        }
        viewModel.isTextValid.observe(viewLifecycleOwner, Observer {
            binding.edCreateAnnouncementDetail.error = it
        })
        // CREATE
        binding.btnCrAnnounce.setOnClickListener {
            viewModel.create()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as AnnouncementComponentProvider).announcementComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AnnouncementCreateViewModel::class.java)
    }
}
