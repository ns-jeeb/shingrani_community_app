package com.locked.shingranicommunity.members

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.locked.shingranicommunity.Constant_Utils.CREATED_EVENT
import com.locked.shingranicommunity.Constant_Utils.INVITED_GUESTS
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.ViewModelProviderFactory
import com.locked.shingranicommunity.databinding.ActivityMemberBinding
import com.locked.shingranicommunity.databinding.MemberItemBinding
import com.locked.shingranicommunity.di.MemberComponent
import com.locked.shingranicommunity.registration_login.registration.MyApplication
import javax.inject.Inject


class MemberActivity : AppCompatActivity() , View.OnClickListener, OnUserClickListener{

    lateinit var mBinding : ActivityMemberBinding
    lateinit var viewModel: MemberViewModel
    private var mToken : String? = ""
    var selectedUser = ArrayList<String>()
    @Inject
    lateinit var memberComponent: MemberComponent
    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        memberComponent = (application as MyApplication).appComponent.memberComponent().create()
        memberComponent.inject(this)

        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_member)

        mToken = getSharedPreferences("token", Context.MODE_PRIVATE).getString("token","")
        viewModel = ViewModelProviders.of(this,viewModelProvider).get(MemberViewModel::class.java)
        viewModel.getMember(mToken!!).observe(this, Observer {

            if (it != null) {
                if (it.size==0){
                   mBinding.txtMessage.text = "No Members are Invited"
                }
                var memberAdapter = MemberAdapter(it)
                memberAdapter.hidView(mBinding.fabInviteMember,displayCheckBox())
                memberAdapter.setListener(this)
                var layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                mBinding.memberRecycler.layoutManager = layoutManager
                mBinding.memberRecycler.adapter = memberAdapter
            }else{
                Toast.makeText(this,"No Member is available",Toast.LENGTH_LONG).show()
          }
        })
        mBinding.txtInvite.setOnClickListener(this)
        mBinding.fabInviteMember.setOnClickListener(this)
        mBinding.backPress.setOnClickListener(this)
    }

    override fun onBackPressed() {
        var intent = Intent()
        intent.putStringArrayListExtra(INVITED_GUESTS,selectedUser)
        setResult(101,intent)
        finish()
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.member_menu, menu)

        return true
    }


    override fun onUserCheckedListener(members: ArrayList<ShingraniMember>, position: Int) {
        var tempArray : ArrayList<String> = ArrayList()
        for (i in 0 until  members.size){
            tempArray.add(members[i]._id)
        }
        selectedUser = tempArray
    }


    inner class MemberAdapter(var members: ArrayList<ShingraniMember>): RecyclerView.Adapter<MemberAdapter.MemberHolder>() {
        internal var binding: MemberItemBinding? = null
        var mListener: OnUserClickListener? = null

//        var mMembers = members
        var mSelectedMembers =  ArrayList<ShingraniMember>()

        fun setListener(listener: OnUserClickListener?) {
            mListener = listener
        }
        fun hidView(view: View,show: Boolean){
            if (show) {
                view.visibility = View.VISIBLE
            }else{
                view.visibility = View.GONE
            }
        }

        inner class MemberHolder (parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.member_item,parent,false)),CompoundButton.OnCheckedChangeListener{

            init {
                binding = DataBindingUtil.bind(itemView)
                binding?.chInviteGuest?.setOnCheckedChangeListener(this)
               hidView((binding?.chInviteGuest as View),displayCheckBox())
            }
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if (p1) {
                    members?.get(adapterPosition)?._id?.let { mSelectedMembers.add(members!![adapterPosition]) }
                    Log.d("numbers","${mSelectedMembers.size}")
                } else {
                    var member : ShingraniMember? = null
                    for (i in 0 until mSelectedMembers.size){
                        member = mSelectedMembers[i]
                    }
                    if (member != null) {
                        checkSizeWithIndex(member._id)
                    }
                }
                mListener?.onUserCheckedListener(mSelectedMembers, adapterPosition)
            }
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberHolder {
            return MemberHolder(parent)
        }

        override fun getItemCount(): Int {
            if (members.isEmpty()){
                return 0
            }
            return members.size
        }

        override fun onBindViewHolder(holder: MemberHolder, position: Int) {
            binding?.txtMemberName?.text = members[position].email
            binding?.txtMemberStatus?.text = members[position].state
        }


        private fun checkSizeWithIndex(id: String) {

            if (mSelectedMembers.size > 0) {

                for (i in 0 until mSelectedMembers.size) {
                    if (id == mSelectedMembers[i]._id) {
                        mSelectedMembers.removeAt(i)
                    }
                }
                Log.d("Index_Of_user", "sel-!~ected user size ${mSelectedMembers.size}")
            }

        }
    }

    fun displayCheckBox(): Boolean{
        return intent.getBooleanExtra(CREATED_EVENT,false)
    }
    override fun onClick(v: View?) {
        when (v) {
            mBinding.backPress -> {
                mBinding.scrollableInvite.visibility = View.GONE
                onBackPressed()
            }
            mBinding.txtInvite -> {
                var dialogFragment = MemberFragment()
//                dialogFragment.show(supportFragmentManager,"MemberDialog")

//                mBinding.memberRecycler.visibility = View.GONE
                mBinding.txtMessage.visibility = View.GONE
                mBinding.scrollableInvite.visibility = View.VISIBLE
                var fragment = supportFragmentManager.findFragmentById(R.id.member_container)
                if (fragment == null) {
                    fragment = MemberFragment()
                }
                supportFragmentManager.beginTransaction().replace(R.id.member_container,fragment).commit()
            }
            else -> {
                mBinding.scrollableInvite.visibility = View.GONE
                mBinding.memberRecycler.visibility = View.VISIBLE
            }
        }
    }
}
interface OnUserClickListener {
    fun onUserCheckedListener(members: ArrayList<ShingraniMember>, position: Int)
}