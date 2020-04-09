package com.locked.shingranicommunity.members

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.LoginFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
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
        viewModel.getMember().observe(this, Observer {

            if (it != null) {
                if (it.size==0){
                   mBinding.txtMessage.text = "No Members are Invited"
                }
                var memberAdapter = viewModel.userManager.getCurrentUser()?._id?.let { it1 -> MemberAdapter(it1,it) }
                memberAdapter?.hidView(mBinding.fabInviteMember,displayCheckBox())
                memberAdapter?.setListener(this)
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

//    fun getJoinedOrPendingMember(members:ArrayList<ShingraniMember>): Boolean{
//        for (i in 0 until members.size){
//            when(members[i].state){
//                "Pending" -> mBinding.txtMessage.text = "this"
//            }
//        }
//    }
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

    @SuppressLint("ResourceType")
    override fun closeMemberFragment(tr : Boolean) {
        if (tr){
           mBinding.scrollableInvite.visibility = View.GONE
            viewModel.getMember()

        }
    }

    inner class MemberAdapter(val currentUserId: String, var members: ArrayList<ShingraniMember>): RecyclerView.Adapter<MemberAdapter.MemberHolder>() {
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
            R.layout.member_item,parent,false)),CompoundButton.OnCheckedChangeListener,View.OnClickListener {

            init {
                binding = DataBindingUtil.bind(itemView)
                binding?.chInviteGuest?.setOnCheckedChangeListener(this)
                binding?.imgMemberEmail?.setOnClickListener(this)
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

            override fun onClick(v: View?) {
                if (v?.id == binding?.imgMemberEmail?.id){
                    sendEmail(members[adapterPosition].email,"Test","this is a test email")
                }
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
            if (currentUserId != members[position]._id){
                binding?.txtMemberName?.text = members[position].email
                when(members[position].state){
                    "Invited" -> binding?.txtMemberStatus?.text = members[position].state
                }
            }
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
        private fun sendEmail(recipient: String, subject: String, message: String) {
            val mIntent = Intent(Intent.ACTION_SEND)
            mIntent.data = Uri.parse("mailto:")
            mIntent.type = "text/plain"
            mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            //put the Subject in the intent
            mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            //put the message in the intent
            mIntent.putExtra(Intent.EXTRA_TEXT, message)
            try {
                startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
            }
            catch (e: Exception){
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
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
    fun closeMemberFragment(tr : Boolean)
}