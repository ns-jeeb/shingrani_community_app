package com.locked.shingranicommunity.members

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.ViewModelProviderFactory
import com.locked.shingranicommunity.databinding.ActivityMemberBinding
import com.locked.shingranicommunity.databinding.MemberItemBinding
import com.locked.shingranicommunity.di.MemberComponent
import com.locked.shingranicommunity.registration_login.registration.MyApplication
import javax.inject.Inject


class MemberActivity : AppCompatActivity() , View.OnClickListener{

    lateinit var mBinding : ActivityMemberBinding
    lateinit var viewModel: MemberViewModel
    private var mToken : String? = ""
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
                var memberAdapter = MemberAdapter(it)
                var layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                mBinding.memberRecycler.layoutManager = layoutManager
                mBinding.memberRecycler.adapter = memberAdapter
            }else{
                Toast.makeText(this,"No Member is available",Toast.LENGTH_LONG).show()
          }
        })
        mBinding.imageViewplaces.setOnClickListener(this)
        mBinding.fabInviteMember.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.member_menu, menu)

        return true
    }

    inner class MemberAdapter(var members: ArrayList<ShingraniMember>): RecyclerView.Adapter<MemberAdapter.MemberHolder>() {
        internal var binding: MemberItemBinding? = null
        inner class MemberHolder (parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.member_item,parent,false)){

            init {
                binding = DataBindingUtil.bind(itemView)
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

    }
    override fun onClick(v: View?) {
        when (v) {
            mBinding.imageViewplaces -> {
                onBackPressed()
            }
            mBinding.fabInviteMember -> {
                mBinding.memberRecycler.visibility = View.GONE
                var fragment = supportFragmentManager.findFragmentById(R.id.member_container)
                if (fragment == null) {
                    fragment = MemberFragment()
                }
                supportFragmentManager.beginTransaction().replace(R.id.member_container,fragment).commit()
            }
            else -> {
                mBinding.memberRecycler.visibility = View.VISIBLE
            }
        }
    }
}