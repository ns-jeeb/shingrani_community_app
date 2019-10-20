package com.locked.shingranicommunity.dashboard.event


import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.locked.shingranicommunity.R
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.Toast

/**
 * A simple [Fragment] subclass.
 */
class EventFragment : Fragment() {

    interface OnEventFragmentTransaction {
        fun onFragmentInteraction(uri: Uri)
    }

    val ARG_PARAM1 = "param1"
    val ARG_PARAM2 = "param2"
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mListener: OnEventFragmentTransaction? = null

    companion object{
        fun newInstance(param1: String, param2: String): EventFragment {
            val fragment = EventFragment()
            val args = Bundle()
            args.putString(EXTRA_MESSAGE, param1)
            args.putString(EXTRA_MESSAGE, param2)
            return fragment
        }

    }

    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEventFragmentTransaction) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnEventFragmentTransaction")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Toast.makeText(context,"Announcement", Toast.LENGTH_LONG) .show()
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

}
