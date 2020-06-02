package com.locked.shingranicommunity.dashboard.event.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.locked.shingranicommunity.MyApplication
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.di.DashboardComponent
import com.locked.shingranicommunity.models.Item

class DetailsActivity : AppCompatActivity() {

    lateinit var dashboardComponent: DashboardComponent
    override fun onCreate(savedInstanceState: Bundle?) {
        dashboardComponent = (application as MyApplication).appComponent.dashBoardComponent().create()
        dashboardComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
//        var item : Item = intent.getParcelableExtra("extra_item")
        var fragment: DetailsFragment? = supportFragmentManager.findFragmentById(R.id.details_container) as DetailsFragment?
        var transaction: FragmentTransaction? = null
        if (fragment == null){
            fragment  = DetailsFragment()
        }
        transaction = supportFragmentManager.beginTransaction().replace(R.id.details_container,fragment)
        transaction.commit()
    }
}
