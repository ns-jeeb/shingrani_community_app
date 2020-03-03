package com.locked.shingranicommunity.members

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.ActivityMemberBinding
import com.locked.shingranicommunity.databinding.EventItemBinding
import com.locked.shingranicommunity.databinding.MemberItemBinding

class MemberActivity : AppCompatActivity() {

    lateinit var mBinding : ActivityMemberBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_member)

        var memberAdapter = MemberAdapter()
        var layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        mBinding.memberRecycler.layoutManager = layoutManager
        mBinding.memberRecycler.adapter = memberAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.member_menu, menu)

        return true
    }

    internal inner class MemberAdapter: RecyclerView.Adapter<MemberAdapter.MemberHolder>() {
        internal var binding: MemberItemBinding? = null
        inner class MemberHolder (parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.member_item,parent,false)){



            init {
                binding = DataBindingUtil.bind(itemView)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberHolder {
            return MemberHolder(parent)
        }

        override fun getItemCount(): Int {
            return 5
        }

        override fun onBindViewHolder(holder: MemberHolder, position: Int) {

            binding?.txtMemberName?.text = "Najeeb"
        }

    }
}
