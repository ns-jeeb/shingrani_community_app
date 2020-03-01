package com.locked.shingranicommunity.dashboard.event.create_event

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.dashboard.data.Users
import com.locked.shingranicommunity.databinding.ActivityUserListBinding
import com.locked.shingranicommunity.databinding.FragmentUserSelectItemBinding


class UserListActivity : AppCompatActivity(), OnUserClickListener {

    lateinit var mBinding: ActivityUserListBinding
    var selectedUser = ArrayList<Users>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_list)
        var users = ArrayList<Users>()
        var user1 = Users("1","1","",null,"","Najeeb","na@jeeb","",
            "Sakhizada","Shingrini",
            "20 - 803 Esterbrooke Ave","North York",
            "ON","M2j3C2","4165541881","455-5545555","M","41",0)
        var user2 = Users("2","2","",null,"","Najeeb","na@jeeb","",
            "Sakhizada","Shingrini",
            "20 - 803 Esterbrooke Ave","North York",
            "ON","M2j3C2","4165541881","455-5545555","M","41",0)
        var user3 = Users("3","3","",null,"","Najeeb","na@jeeb","",
            "Sakhizada","Shingrini",
            "20 - 803 Esterbrooke Ave","North York",
            "ON","M2j3C2","4165541881","455-5545555","M","41",0)
        var user4 = Users("4","4","",null,"","Najeeb","na@jeeb","",
            "Sakhizada","Shingrini",
            "20 - 803 Esterbrooke Ave","North York",
            "ON","M2j3C2","4165541881","455-5545555","M","41",0)
        var user5 = Users("5","5","",null,"","Najeeb","na@jeeb","",
            "Sakhizada","Shingrini",
            "20 - 803 Esterbrooke Ave","North York",
            "ON","M2j3C2","4165541881","455-5545555","M","41",0)
        var user6 = Users("6","6","",null,"","Najeeb","na@jeeb","",
            "Sakhizada","Shingrini",
            "20 - 803 Esterbrooke Ave","North York",
            "ON","M2j3C2","4165541881","455-5545555","M","41",0)
        var user7 = Users("7","7","",null,"","Najeeb","na@jeeb","",
            "Sakhizada","Shingrini",
            "20 - 803 Esterbrooke Ave","North York",
            "ON","M2j3C2","4165541881","455-5545555","M","41",0)
        var user8 = Users("8","8","",null,"","Najeeb","na@jeeb","",
            "Sakhizada","Shingrini",
            "20 - 803 Esterbrooke Ave","North York",
            "ON","M2j3C2","4165541881","455-5545555","M","41",0)

        var user9 = Users("9","9","",null,"","Najeeb","na@jeeb","",
        "Sakhizada","Shingrini",
        "20 - 803 Esterbrooke Ave","North York",
        "ON","M2j3C2","4165541881","455-5545555","M","41",0)
        users.add(user1)
        users.add(user2)
        users.add(user3)
        users.add(user4)
        users.add(user5)
        users.add(user6)
        users.add(user7)
        users.add(user8)
        users.add(user9)
        var userAdapter = UserSelectionAdapter(users)
        userAdapter.setListener(this)
        var layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
        mBinding.userSelectionList.layoutManager = layoutManager
        mBinding.userSelectionList.adapter = userAdapter

    }

    override fun onBackPressed() {
        var intent = Intent()
        intent.putExtra("number_of_guests",selectedUser.size)
        setResult(101,intent)
        finish()
        super.onBackPressed()
    }

    class UserSelectionAdapter(private val users: ArrayList<Users>?) : RecyclerView.Adapter<UserSelectionAdapter.UserViewHolder>() {
        var mListener: OnUserClickListener? = null
        var mUsers = users
        var mSelectedUserId =  ArrayList<Users>()
        fun setListener(listener: OnUserClickListener?) {
            mListener = listener
        }

        @NonNull
        override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): UserViewHolder {
            return UserViewHolder(viewGroup)
        }

        override fun onBindViewHolder(@NonNull userViewHolder: UserViewHolder, i: Int) {
            userViewHolder.bind(mUsers!![i])
        }

        override fun getItemCount(): Int {
            return if (mUsers == null || mUsers!!.size === 0) {
                5
            } else mUsers!!.size
        }

        inner class UserViewHolder(@NonNull parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_user_select_item, parent, false)),
            View.OnClickListener,
            CompoundButton.OnCheckedChangeListener {
            var mBinding: FragmentUserSelectItemBinding?

            fun bind(user: Users?) {
                if (user != null ) {
                    mBinding?.selectUserName?.text = user.fName
                    mBinding?.selectLastName?.text = user.lName
                    mBinding?.lblCellPhone?.text = user.cellPhone
                    mBinding?.txtHomePhone?.text = user.homePhone
                    mBinding?.txtAddress?.text = user.address
                    mBinding?.lblAddress?.text = user.city

                    mBinding?.chUserSelect?.setOnCheckedChangeListener(this)
                    itemView.setOnClickListener(this)
                }
            }

            override fun onClick(view: View?) {
                if (mBinding?.userMoreInfoLayout?.visibility == View.VISIBLE) {
                    mBinding?.userMoreInfoLayout?.visibility = View.GONE
                } else {
                    mBinding?.userMoreInfoLayout?.visibility = View.VISIBLE
                }
            }


            override fun onCheckedChanged(compoundButton: CompoundButton, b: Boolean) {
                if (b) {
                    mUsers?.get(adapterPosition)?.id?.let { mSelectedUserId.add(mUsers!![adapterPosition]) }
                    Log.d("numbers","${mSelectedUserId.size}")
                    mListener?.onUserCheckedListener(mSelectedUserId, adapterPosition)
                } else {
                    var user : Users? = null
                    for (i in 0 until mSelectedUserId.size){
                        user = mSelectedUserId[i]
                    }
                    if (user != null) {
                        checkSizeWithIndex(user.id)
                    }
                }
            }

            init {
                mBinding = DataBindingUtil.bind(itemView)
            }

            private fun checkSizeWithIndex(id: String) {

                if (mSelectedUserId.size > 0) {

                    for (i in 0 until mSelectedUserId.size) {
                        if (id == mSelectedUserId[i].id) {
                            mSelectedUserId.removeAt(i)
                        }
                    }
                    Log.d("Index_Of_user", "selected user size ${mSelectedUserId.size}")
                }

            }
        }
    }

    override fun onUserCheckedListener(users: ArrayList<Users>, position: Int) {
        selectedUser = users
    }

}
interface OnUserClickListener {
    fun onUserCheckedListener(users: ArrayList<Users>, position: Int)
}
