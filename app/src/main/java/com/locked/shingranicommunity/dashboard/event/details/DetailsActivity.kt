package com.locked.shingranicommunity.dashboard.event.details

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.locked.shingranicommunity.MyApplication
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.common.FragmentActivity
import com.locked.shingranicommunity.di2.item_details.ItemDetailsComponent
import com.locked.shingranicommunity.search_map.ItemDetailsComponentProvider

class DetailsActivity : FragmentActivity(), ItemDetailsComponentProvider {

    override lateinit var itemDetailsComponent: ItemDetailsComponent
    override fun onCreate(savedInstanceState: Bundle?) {
        itemDetailsComponent = MyApplication.instance.appComponent2.itemDetailsComponentFactory.create(this)
        itemDetailsComponent.inject(this)
        super.onCreate(savedInstanceState)
        var fragment: DetailsFragment? = supportFragmentManager.findFragmentById(R.id.fragment) as DetailsFragment?
        var transaction: FragmentTransaction? = null
        if (fragment == null){
            fragment  = DetailsFragment()
        }
        transaction = supportFragmentManager.beginTransaction().replace(R.id.fragment,fragment)
        transaction.commit()
    }
}
