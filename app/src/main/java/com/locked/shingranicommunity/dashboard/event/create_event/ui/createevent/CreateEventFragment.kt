package com.locked.shingranicommunity.dashboard.event.create_event.ui.createevent

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.ViewModelProviderFactory
import com.locked.shingranicommunity.dashboard.event.create_event.CreateItemActivity
import com.locked.shingranicommunity.dashboard.event.create_event.UserListActivity
import com.locked.shingranicommunity.databinding.CreateEventFragmentBinding
import javax.inject.Inject


class CreateEventFragment : Fragment() ,View.OnClickListener{
    var numberOfGuests = 0
    override fun onClick(v: View?) {
        var type = mBinding.crEventType.getItemAtPosition(1).toString()
        if (v?.id == R.id.btn_cr_event){
            viewModel.createEvent(
                mBinding.crEventName.text.toString(),
                type,
                mBinding.crEventAddress.text.toString(),
                mBinding.crEventDateTime.text.toString(),
                mBinding.crEventDetails.text.toString(),
                "5d1134ab5609832210f63ef7, 5d1134ab5609832210f63ef7,5d1134ab5609832210f63ef7",
                "5d1134ab5609832210f63ef7"
            ).observe(this, Observer {

                var intent = Intent()
                intent.putExtra("response_message",it)
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            })


        }else if (v?.id == R.id.txt_user_selection) {
//            launch list of user
            var intent = Intent(activity, UserListActivity::class.java)
            startActivityForResult(intent, 101)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            if (requestCode == 101) {
                numberOfGuests = data.getIntExtra("number_of_guests",0)
                mBinding.txtUserSelection.text = "$numberOfGuests Guests are invited"
            }
        }

    }

    lateinit var mBinding: CreateEventFragmentBinding

    companion object {
        fun newInstance() = CreateEventFragment()
    }

    @Inject
    lateinit var viewModelProviders: ViewModelProviderFactory
    lateinit var viewModel: CreateEventViewModel

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
//        viewModel.lifecycleOwner = this
        mBinding = DataBindingUtil.inflate(inflater, R.layout.create_event_fragment,container,false)
        viewModel = ViewModelProviders.of(this, viewModelProviders).get(CreateEventViewModel::class.java)
        mBinding.btnCrEvent.setOnClickListener(this)

        mBinding.txtUserSelection.setOnClickListener(this)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as CreateItemActivity).dashboardCompunent.inject(this)
    }

}
